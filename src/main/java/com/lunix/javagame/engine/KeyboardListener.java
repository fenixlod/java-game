package com.lunix.javagame.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardListener {
	private boolean keyPressed[] = new boolean[350];

	/**
	 * Execute this callback function every time when key is pressed.
	 * 
	 * @param key
	 * @param scanCode
	 * @param action
	 * @param modifiers
	 */
	public void keyCallback(long window, int key, int scanCode, int action, int modifiers) {
		if (key >= keyPressed.length)
			return;

		if (action == GLFW_PRESS)
			keyPressed[key] = true;
		else if (action == GLFW_RELEASE) {
			keyPressed[key] = false;
		}
	}

	public boolean isKeyPressed(int button) {
		if (button >= keyPressed.length)
			throw new IllegalStateException("Invalid keyboard key: " + button);

		return keyPressed[button];
	}

	@Override
	public String toString() {
		List<Integer> pressedKeys = new ArrayList<>();
		for (int key = 0; key < keyPressed.length; key++) {
			if (keyPressed[key] == true) {
				pressedKeys.add(key);
			}
		}
		
		return "KeyboardListener [keysPressed=" + Arrays.toString(pressedKeys.toArray()) + "]";
	}
}
