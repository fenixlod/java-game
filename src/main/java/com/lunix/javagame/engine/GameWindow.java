package com.lunix.javagame.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.graphic.PickingTexture;
import com.lunix.javagame.engine.graphic.Renderer;
import com.lunix.javagame.engine.ui.UiLayer;

public class GameWindow {
	private static final Logger logger = LogManager.getLogger(GameWindow.class);
	private final WindowConfigs windowConfigs;
	private long windowHandle;// memory address of the window
	private Vector2i windowSize;
	private Vector2i viewPortSize;
	private Vector2i viewPortOffset;
	private UiLayer uiLayer;
	private PickingTexture pickingTexture;

	public GameWindow(WindowConfigs windowConfigs) {
		this.windowConfigs = windowConfigs;
		windowSize = new Vector2i();
		viewPortSize = new Vector2i();
		viewPortOffset = new Vector2i();
		pickingTexture = new PickingTexture();
	}

	/**
	 * Create new window.
	 * 
	 * @param mouseListener
	 * @param keyboardListener
	 * @throws Exception
	 */
	public void create(MouseListener mouseListener, KeyboardListener keyboardListener) throws Exception {
		logger.info("Creating game window using LWJGL version: {} ...", Version.getVersion());

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
		glfwWindowHint(GLFW_MAXIMIZED, windowConfigs.maximized() ? GLFW_TRUE : GLFW_FALSE); // the window will be
																							// maximized
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		// Create the window
		windowHandle = glfwCreateWindow(windowConfigs.width(), windowConfigs.height(), windowConfigs.title(), NULL,
				NULL);
		if (windowHandle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		windowSize.set(windowConfigs.width(), windowConfigs.height());

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(windowHandle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(windowHandle, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(windowHandle);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(windowHandle);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		clearColor(1f, 1f, 1f, 1f);

		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_ALPHA_TEST);

		uiLayer = new UiLayer(this);
		uiLayer.init();

		// Setup a mouse button callback. It will be called every time a mouse button is
		// pressed or released.
		glfwSetCursorPosCallback(windowHandle, mouseListener::positionCallback);
		
		glfwSetMouseButtonCallback(windowHandle, (win, button, action, mod) ->
			uiLayer.mouseButtonCallback(win, button, action, mod, mouseListener::buttonCallback)
		);
		
		glfwSetScrollCallback(windowHandle, (win, xScroll, yScroll) ->
			uiLayer.scrollCallback(win, xScroll, yScroll, mouseListener::scrollCallback)
		);

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(windowHandle, (win, key, sCode, action, mods) ->
			uiLayer.keyCallback(win, key, sCode, action, mods, keyboardListener::keyCallback)
		);

		// Add window resize callback
		glfwSetWindowSizeCallback(windowHandle, (win, newWidth, newHeight) -> {
			windowSize.set(newWidth, newHeight);
			glViewport(0, 0, newWidth, newHeight);
			viewPortSize.set(windowSize);
		});

		IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(windowHandle, width, height);
		windowSize.set(width.get(), height.get());
		glViewport(0, 0, windowSize.x, windowSize.y);
		viewPortSize.set(windowSize);
		pickingTexture.init(windowSize.x, windowSize.y);
	}

	/**
	 * Indicate the start of new frame. This function should be called at the start
	 * of the new game cycle.
	 */
	public void newFrame(Scene currentScene) {
		currentScene.newFrame();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	/**
	 * Refresh the display. Will draw all screen elements.
	 */
	public void render() {
		// Swap frame buffers. Fore drawing of all rendered elements
		glfwSwapBuffers(windowHandle); // swap the color buffers
	}

	/**
	 * Destroy the current window.
	 */
	public void destroy() {
		// Free the memory
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * Close the window
	 */
	public void close() {
		glfwSetWindowShouldClose(windowHandle, true);
	}

	/**
	 * Check if the window is opened.
	 * 
	 * @return
	 */
	public boolean isOpened() {
		return !glfwWindowShouldClose(windowHandle);
	}

	/**
	 * Set clear color. All empty spaces will be filled with this color.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 */
	public void clearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}

	public long handle() {
		return windowHandle;
	}

	public Vector2i windowSize() {
		return windowSize;
	}

	/**
	 * Update the window UI.
	 * 
	 * @param dt
	 * @param currentScene
	 */
	public void update(Scene currentScene) {
		currentScene.endFrame();
		uiLayer.update(currentScene);
	}

	public UiLayer uiLayer() {
		return uiLayer;
	}

	public float targerAspectRatio() {
		return (float) windowConfigs.width() / windowConfigs.height();
	}
	
	public Vector2i viewPortSize() {
		return viewPortSize;
	}

	public void viewPortSize(int viewPortWidth, int viewPortHeight) {
		viewPortSize.set(viewPortWidth, viewPortHeight);
	}

	public Vector2i viewPortOffset() {
		return viewPortOffset;
	}

	public void viewPortOffset(int viewPortOffsetX, int viewPortOffsetY) {
		viewPortOffset.set(viewPortOffsetX, viewPortOffsetY);
	}

	public void createPickingTexture(Scene currentScene) throws Exception {
		glDisable(GL_BLEND);
		pickingTexture.enableWrithing();
		glViewport(0, 0, windowSize.x, windowSize.y);
		clearColor(0f, 0f, 0f, 0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer
		Renderer.overrideShader(ResourcePool.getShader(ShaderType.PICKING));
		currentScene.render();
		pickingTexture.disableWrithing();
		Renderer.overrideShader(null);
		glEnable(GL_BLEND);
	}

	public long pickObject(Vector2i pos) {
		return pickingTexture.readPixel(pos);
	}

	/**
	 * Add string suffix to the window title
	 * 
	 * @param suffix
	 */
	public void setTitleSuffix(String suffix) {
		glfwSetWindowTitle(windowHandle, windowConfigs.title() + suffix);
	}
}
