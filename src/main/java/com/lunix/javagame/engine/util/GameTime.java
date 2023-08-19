package com.lunix.javagame.engine.util;

public class GameTime {
	private final long startTime = System.nanoTime();
	private long frameStartTime = startTime;
	private long timeBetweenFrames;

	/**
	 * Return elapsed time in seconds since game start.
	 * 
	 * @return
	 */
	public float getElapsedTime() {
		return (float) ((System.nanoTime() - startTime) * 1E-9);
	}

	/**
	 * Indicate start of frame.
	 */
	public void tick() {
		long currentTime = System.nanoTime();
		timeBetweenFrames = currentTime - frameStartTime;
		frameStartTime = currentTime;
	}

	/**
	 * Return elapsed time in seconds since the last frame.
	 * 
	 * @return
	 */
	public float getDeltaTime() {
		return (float) (timeBetweenFrames * 1E-9);
	}
}
