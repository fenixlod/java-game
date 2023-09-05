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
import org.lwjgl.BufferUtils;
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
	private UiLayer uiLayer;

	public GameWindow(WindowConfigs windowConfigs) {
		this.windowConfigs = windowConfigs;
	}

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
		glfwWindowHint(GLFW_MAXIMIZED, windowConfigs.maximized() ? GLFW_TRUE : GLFW_FALSE); // the window will be
																							// maximized
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		// glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

		// Create the window
		windowHandle = glfwCreateWindow(windowConfigs.width(), windowConfigs.height(), windowConfigs.title(), NULL,
				NULL);
		if (windowHandle == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

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
		mouseListener.bindToWindow(windowHandle, UiLayer::mouseButtonCallback, UiLayer::scrollCallback);

		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		keyboardListener.bindToWindow(windowHandle, UiLayer::keyCallback);
	}

	public void newFrame() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	public void render() {
		glfwSwapBuffers(windowHandle); // swap the color buffers
	}

	public void destroy() {
		// Free the memory
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void close() {
		glfwSetWindowShouldClose(windowHandle, true);
	}

	public boolean isOpened() {
		return !glfwWindowShouldClose(windowHandle);
	}

	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}

	public long handle() {
		return this.windowHandle;
	}

	public float[] size() {
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(windowHandle, w, h);
		return new float[] { w.get(0), h.get(0) };
	}

	public void update(float dt, Scene currentScene) {
		this.uiLayer.update(dt, currentScene);
	}
}
