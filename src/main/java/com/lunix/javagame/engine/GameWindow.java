package com.lunix.javagame.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.IntBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.ui.UiLayer;

public class GameWindow {
	private static final Logger logger = LogManager.getLogger(GameWindow.class);
	private final WindowConfigs windowConfigs;
	private long windowHandle;// memory address of the window
	private float[] size;
	private UiLayer uiLayer;

	public GameWindow(WindowConfigs windowConfigs) {
		this.windowConfigs = windowConfigs;
		this.size = new float[2];
	}

	/**
	 * Create new window.
	 * 
	 * @param mouseListener
	 * @param keyboardListener
	 * @throws IOException
	 */
	public void create(MouseListener mouseListener, KeyboardListener keyboardListener) throws IOException {
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
		glfwWindowHint(GLFW_MAXIMIZED, this.windowConfigs.maximized() ? GLFW_TRUE : GLFW_FALSE); // the window will be
																							// maximized
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

		// Create the window
		this.windowHandle = glfwCreateWindow(this.windowConfigs.width(), this.windowConfigs.height(),
				this.windowConfigs.title(), NULL, NULL);
		if (this.windowHandle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		this.size[0] = this.windowConfigs.width();
		this.size[1] = this.windowConfigs.height();

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(this.windowHandle, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(this.windowHandle, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(this.windowHandle);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(this.windowHandle);

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1f, 1f, 1f, 1f);

		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glAlphaFunc(GL_GREATER, 0.9f);
		glEnable(GL_ALPHA_TEST);

		this.uiLayer = new UiLayer(this);
		this.uiLayer.init();

		// Setup a mouse button callback. It will be called every time a mouse button is
		// pressed or released.
		glfwSetCursorPosCallback(this.windowHandle, mouseListener::positionCallback);
		
		glfwSetMouseButtonCallback(this.windowHandle, (win, button, action, mod) ->
			UiLayer.mouseButtonCallback(win, button, action, mod, mouseListener::buttonCallback)
		);
		
		glfwSetScrollCallback(this.windowHandle, (win, xScroll, yScroll) -> 
			UiLayer.scrollCallback(win, xScroll, yScroll, mouseListener::scrollCallback)
		);

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(this.windowHandle, (win, key, sCode, action, mods) ->
			UiLayer.keyCallback(win, key, sCode, action, mods, keyboardListener::keyCallback)
		);

		// Add window resize callback
		glfwSetWindowSizeCallback(this.windowHandle, (win, newWidth, newHeight) -> {
			this.size[0] = newWidth;
			this.size[1] = newHeight;
		});
	}

	/**
	 * Indicate the start of new frame. This function should be called at the start
	 * of the new game cycle.
	 */
	public void newFrame() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	/**
	 * Refresh the display. Will draw all screen elements.
	 */
	public void render() {
		glfwSwapBuffers(this.windowHandle); // swap the color buffers
	}

	/**
	 * Destroy the current window.
	 */
	public void destroy() {
		// Free the memory
		glfwFreeCallbacks(this.windowHandle);
		glfwDestroyWindow(this.windowHandle);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * Close the window
	 */
	public void close() {
		glfwSetWindowShouldClose(this.windowHandle, true);
	}

	/**
	 * Check if the window is opened.
	 * 
	 * @return
	 */
	public boolean isOpened() {
		return !glfwWindowShouldClose(this.windowHandle);
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
		return this.windowHandle;
	}

	public float[] size() {
		return this.size;
	}

	/**
	 * Update the window UI.
	 * 
	 * @param dt
	 * @param currentScene
	 */
	public void update(float dt, Scene currentScene) {
		this.uiLayer.update(dt, currentScene);
	}
}
