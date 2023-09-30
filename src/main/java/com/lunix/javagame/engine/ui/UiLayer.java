package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.springframework.core.io.ClassPathResource;

import com.lunix.javagame.engine.GameWindow;
import com.lunix.javagame.engine.scenes.SceneExecutor;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGui;

public class UiLayer {
	private final ImGuiImplGlfw imGuiGlfw;
	private final ImGuiImplGl3 imGuiGl3;
	private String glslVersion;
	private long glfwWindow;
	public ImGuiIO io;
	private GameViewWindow viewWindow;

	public UiLayer(GameWindow window) {
		glfwWindow = window.handle();
		imGuiGlfw = new ImGuiImplGlfw();
		imGuiGl3 = new ImGuiImplGl3();
		glslVersion = "#version 330 core";
	}

	/**
	 * Initialize the UI
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		initImGui();
		imGuiGlfw.init(glfwWindow, true);
		imGuiGl3.init(glslVersion);
	}

	/**
	 * Destroy the UI, free all allocated resources.
	 */
	public void destroy() {
		imGuiGlfw.dispose();
		imGuiGl3.dispose();
		ImGui.destroyContext();
		Callbacks.glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
	}

	/**
	 * Initialize the ImGui overlay.
	 * 
	 * @throws IOException
	 */
	private void initImGui() throws IOException {
		ImGui.createContext();

		// ------------------------------------------------------------
		// Initialize ImGuiIO config
		io = ImGui.getIO();

		io.setIniFilename("src/main/resources/imgui.ini"); // We don't want to save .ini file
		io.setConfigFlags(ImGuiConfigFlags.DockingEnable);// Enable docking
		io.setBackendPlatformName("imgui_java_impl_glfw");

		// ------------------------------------------------------------
		// GLFW callbacks to handle user input

//		glfwSetCharCallback(glfwWindow, (w, c) -> {
//			if (c != GLFW_KEY_DELETE) {
//				io.addInputCharacter(c);
//			}
//		});

		io.setSetClipboardTextFn(new ImStrConsumer() {
			@Override
			public void accept(final String s) {
				glfwSetClipboardString(glfwWindow, s);
			}
		});

		io.setGetClipboardTextFn(new ImStrSupplier() {
			@Override
			public String get() {
				final String clipboardString = glfwGetClipboardString(glfwWindow);
				if (clipboardString != null) {
					return clipboardString;
				} else {
					return "";
				}
			}
		});

		// ------------------------------------------------------------
		// Fonts configuration
		// Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

		final ImFontAtlas fontAtlas = io.getFonts();
		final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

		// Glyphs could be added per-font as well as per config used globally like here
		fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

		// Add a default font, which is 'ProggyClean.ttf, 13px'
		// fontAtlas.addFontDefault();

		// Fonts merge example
		// fontConfig.setMergeMode(true); // When enabled, all fonts added with this
		// config would be merged with the
										// previously added font
		fontConfig.setPixelSnapH(true);
		fontAtlas.addFontFromFileTTF(new ClassPathResource("assets/fonts/arialbd.ttf").getFile().getAbsolutePath(), 18,
				fontConfig);

		fontConfig.destroy(); // After all fonts were added we don't need this config more

		fontAtlas.build();
		// ------------------------------------------------------------
		// Use freetype instead of stb_truetype to build a fonts texture
		// ImGuiFreeType.buildFontAtlas(fontAtlas,
		// ImGuiFreeType.RasterizerFlags.LightHinting);


		// Method initializes LWJGL3 renderer.
		// This method SHOULD be called after you've initialized your ImGui
		// configuration (fonts and so on).
		// ImGui context should be created as well.
	}

	/**
	 * Update the UI for the current scene.
	 * 
	 * @param dt
	 * @param currentScene
	 */
	public void update(SceneExecutor currentSceneExecutor) {
		imGuiGlfw.newFrame();
		ImGui.newFrame();

		currentSceneExecutor.ui();
		// Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods

		ImGui.render();
		imGuiGl3.renderDrawData(ImGui.getDrawData());
	}

	/**
	 * Mouse button callbacks.
	 * 
	 * @param window
	 * @param button
	 * @param action
	 * @param mods
	 */
	public void mouseButtonCallback(long window, int button, int action, int mods,
			GLFWMouseButtonCallbackI mouseBtnCb) {
		final boolean[] mouseDown = new boolean[5];
		mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
		mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
		mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
		mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
		mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

		io.setMouseDown(mouseDown);

		if (!io.getWantCaptureMouse() && mouseDown[1]) {
			ImGui.setWindowFocus(null);
		}

		if (!io.getWantCaptureMouse() || (viewWindow != null && viewWindow.getWantCaptureMouse())) {
			if (mouseBtnCb != null)
				mouseBtnCb.invoke(window, button, action, mods);
		}
	}

	/**
	 * Mouse scroll callbacks.
	 * 
	 * @param window
	 * @param xOffset
	 * @param yOffset
	 */
	public void scrollCallback(long window, double xOffset, double yOffset, GLFWScrollCallbackI scrollCb) {
		io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
		io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

		if (!io.getWantCaptureMouse() || (viewWindow != null && viewWindow.getWantCaptureMouse())) {
			if (scrollCb != null)
				scrollCb.invoke(window, xOffset, yOffset);
		}
	}

	/**
	 * Keyboard callbacks.
	 * 
	 * @param window
	 * @param key
	 * @param scancode
	 * @param action
	 * @param mods
	 */
	public void keyCallback(long window, int key, int scancode, int action, int mods, GLFWKeyCallbackI keyCb) {
		if (action == GLFW_PRESS) {
			io.setKeysDown(key, true);
		} else if (action == GLFW_RELEASE) {
			io.setKeysDown(key, false);
		}

		io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
		io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
		io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
		io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

		if (!io.getWantCaptureKeyboard()) {
			if (keyCb != null/* && !GameViewWindow.getWantCaptureMouse() */)
				keyCb.invoke(window, key, scancode, action, mods);
		}
	}

	public void setViewWindow(GameViewWindow viewWindow) {
		this.viewWindow = viewWindow;
	}
}
