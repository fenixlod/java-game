package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.springframework.core.io.ClassPathResource;

import com.lunix.javagame.engine.GameWindow;
import com.lunix.javagame.engine.Scene;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.ImGuiViewport;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

public class UiLayer {
	private final ImGuiImplGlfw imGuiGlfw;
	private final ImGuiImplGl3 imGuiGl3;
	private String glslVersion;
	private long glfwWindow;
	private final GameWindow window;
	public static ImGuiIO io;

	// Mouse cursors provided by GLFW
	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

	public UiLayer(GameWindow window) {
		this.glfwWindow = window.handle();
		this.window = window;
		this.imGuiGlfw = new ImGuiImplGlfw();
		this.imGuiGl3 = new ImGuiImplGl3();
		this.glslVersion = "#version 330 core";
	}

	/**
	 * Initialize the UI
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		initImGui();
		this.imGuiGlfw.init(this.glfwWindow, true);
		this.imGuiGl3.init(this.glslVersion);
	}

	/**
	 * Destroy the UI, free all allocated resources.
	 */
	public void destroy() {
		this.imGuiGlfw.dispose();
		this.imGuiGl3.dispose();
		ImGui.destroyContext();
		Callbacks.glfwFreeCallbacks(this.glfwWindow);
		glfwDestroyWindow(this.glfwWindow);
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
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
		io.setConfigFlags(ImGuiConfigFlags.DockingEnable);// Enable docking
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
		this.mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		this.mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

		// ------------------------------------------------------------
		// GLFW callbacks to handle user input

//		glfwSetCharCallback(this.glfwWindow, (w, c) -> {
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
		
		//Custom styling
		ImGuiStyle style = ImGui.getStyle();
		float[][] colors = style.getColors();
		colors[ImGuiCol.TextSelectedBg] = new float[] { 0f, 0f, 1f, 1f };
		colors[ImGuiCol.FrameBg] = new float[] { 0.16f, 0.29f, 0.48f, 1f };
		colors[ImGuiCol.DockingPreview] = new float[] { 0.16f, 0.29f, 0.48f, 1f };
		style.setColors(colors);
	}

	/**
	 * Update the UI for the current scene.
	 * 
	 * @param dt
	 * @param currentScene
	 */
	public void update(float dt, Scene currentScene) {
		startFrame(dt);

		this.imGuiGlfw.newFrame();
		ImGui.newFrame();
		setupDockspace();
		currentScene.ui();
		// Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
		ImGui.showDemoWindow();
		ImGui.end();
		ImGui.render();
		this.imGuiGl3.renderDrawData(ImGui.getDrawData());
	}

	/**
	 * Indicate the start of the new frame.
	 * 
	 * @param deltaTime
	 */
	private void startFrame(final float deltaTime) {
		// Get window properties and mouse position
		float[] size = this.window.size();
		float[] winWidth = { size[0] };
		float[] winHeight = { size[1] };
		double[] mousePosX = { 0 };
		double[] mousePosY = { 0 };
		glfwGetCursorPos(this.glfwWindow, mousePosX, mousePosY);

		// We SHOULD call those methods to update Dear ImGui state for the current frame
		final ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(winWidth[0], winHeight[0]);
		io.setDisplayFramebufferScale(1f, 1f);
		io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
		io.setDeltaTime(deltaTime);

		// Update the mouse cursor
		final int imguiCursor = ImGui.getMouseCursor();
		glfwSetCursor(this.glfwWindow, mouseCursors[imguiCursor]);
		glfwSetInputMode(this.glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	/**
	 * Mouse button callbacks.
	 * 
	 * @param window
	 * @param button
	 * @param action
	 * @param mods
	 */
	public static void mouseButtonCallback(long window, int button, int action, int mods,
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

		if (!io.getWantCaptureMouse() && mouseBtnCb != null)
			mouseBtnCb.invoke(window, button, action, mods);
	}

	/**
	 * Mouse scroll callbacks.
	 * 
	 * @param window
	 * @param xOffset
	 * @param yOffset
	 */
	public static void scrollCallback(long window, double xOffset, double yOffset, GLFWScrollCallbackI scrollCb) {
		io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
		io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

		if (!io.getWantCaptureMouse() && scrollCb != null)
			scrollCb.invoke(window, xOffset, yOffset);
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
	public static void keyCallback(long window, int key, int scancode, int action, int mods, GLFWKeyCallbackI keyCb) {
		if (action == GLFW_PRESS) {
			io.setKeysDown(key, true);
		} else if (action == GLFW_RELEASE) {
			io.setKeysDown(key, false);
		}

		io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
		io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
		io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
		io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

		if (!io.getWantCaptureKeyboard() && keyCb != null)
			keyCb.invoke(window, key, scancode, action, mods);
	}

	private void setupDockspace() {
		int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
		ImGuiViewport mainViewport = ImGui.getMainViewport();
		ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY(), ImGuiCond.Always);
		ImGui.setNextWindowSize(mainViewport.getWorkSizeX(), mainViewport.getWorkSizeY());
		ImGui.setNextWindowViewport(mainViewport.getID());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

		ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
		ImGui.popStyleVar(2);

        // Dockspace
		ImGui.dockSpace(ImGui.getID("Dockspace"));
	}
}
