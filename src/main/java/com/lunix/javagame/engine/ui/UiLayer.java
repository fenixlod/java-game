package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

import org.lwjgl.glfw.Callbacks;
import org.springframework.core.io.ClassPathResource;

import com.lunix.javagame.engine.GameWindow;
import com.lunix.javagame.engine.Scene;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGui;

public class UiLayer {
	private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

	private String glslVersion = "#version 330 core";
	private long glfwWindow;
	private final GameWindow window;
	public static ImGuiIO io;

	// Mouse cursors provided by GLFW
	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

	public UiLayer(GameWindow window) {
		this.glfwWindow = window.handle();
		this.window = window;
	}

	public void init() throws IOException {
		initImGui();
		imGuiGlfw.init(glfwWindow, true);
		imGuiGl3.init(glslVersion);
	}

	public void destroy() {
		imGuiGlfw.dispose();
		imGuiGl3.dispose();
		ImGui.destroyContext();
		Callbacks.glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		glfwTerminate();
	}

	private void initImGui() throws IOException {
		ImGui.createContext();

		// ------------------------------------------------------------
		// Initialize ImGuiIO config
		io = ImGui.getIO();

		io.setIniFilename("src/main/resources/imgui.ini"); // We don't want to save .ini file
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
		io.setBackendPlatformName("imgui_java_impl_glfw");

		// ------------------------------------------------------------
		// Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[]
		// array.
		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
		keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
		keyMap[ImGuiKey.End] = GLFW_KEY_END;
		keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
		keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
		keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
		keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = GLFW_KEY_A;
		keyMap[ImGuiKey.C] = GLFW_KEY_C;
		keyMap[ImGuiKey.V] = GLFW_KEY_V;
		keyMap[ImGuiKey.X] = GLFW_KEY_X;
		keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
		keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
		io.setKeyMap(keyMap);

		// ------------------------------------------------------------
		// Mouse cursors mapping
		mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

		// ------------------------------------------------------------
		// GLFW callbacks to handle user input

		glfwSetCharCallback(glfwWindow, (w, c) -> {
			if (c != GLFW_KEY_DELETE) {
				io.addInputCharacter(c);
			}
		});

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
		fontAtlas.addFontFromFileTTF(new ClassPathResource("assets/fonts/arialbd.ttf").getFile().getAbsolutePath(), 24,
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

	public void update(float dt, Scene currentScene) {
		startFrame(dt);

		imGuiGlfw.newFrame();
		ImGui.newFrame();

		currentScene.ui();
		// Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
		ImGui.showDemoWindow();

		ImGui.render();
		imGuiGl3.renderDrawData(ImGui.getDrawData());
	}

	private void startFrame(final float deltaTime) {
		// Get window properties and mouse position
		float[] size = window.size();
		float[] winWidth = { size[0] };
		float[] winHeight = { size[1] };
		double[] mousePosX = { 0 };
		double[] mousePosY = { 0 };
		glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

		// We SHOULD call those methods to update Dear ImGui state for the current frame
		final ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(winWidth[0], winHeight[0]);
		io.setDisplayFramebufferScale(1f, 1f);
		io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
		io.setDeltaTime(deltaTime);

		// Update the mouse cursor
		final int imguiCursor = ImGui.getMouseCursor();
		glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
		glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	public static void mouseButtonCallback(long window, int button, int action, int mods) {
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
	}

	public static void scrollCallback(long window, double xOffset, double yOffset) {
		io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
		io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
	}

	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			io.setKeysDown(key, true);
		} else if (action == GLFW_RELEASE) {
			io.setKeysDown(key, false);
		}

		io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
		io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
		io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
		io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
	}
}
