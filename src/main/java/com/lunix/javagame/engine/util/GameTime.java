package com.lunix.javagame.engine.util;

import static org.lwjgl.glfw.GLFW.*;

public class GameTime {
	private final double startTime = glfwGetTime();
	private double frameStartTime = startTime;
	private double timeBetweenFrames;

	/**
	 * Return elapsed time in seconds since game start.
	 * 
	 * @return
	 */
	public float elapsedTime() {
		return (float) (glfwGetTime() - startTime);
	}

	/**
	 * Indicate start of frame.
	 */
	public void tick() {
		double currentTime = glfwGetTime();
		timeBetweenFrames = currentTime - frameStartTime;
		frameStartTime = currentTime;
	}

	/**
	 * Return elapsed time in seconds since the last frame.
	 * 
	 * @return
	 */
	public float deltaTime() {
		return (float) timeBetweenFrames;
	}
}
