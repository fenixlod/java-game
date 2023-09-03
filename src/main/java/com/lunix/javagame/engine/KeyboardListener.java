package com.lunix.javagame.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class KeyboardListener {
	private boolean keyPressed[] = new boolean[350];

	public void bindToWindow(long windowHandler, GLFWKeyCallbackI keyCb) {
		glfwSetKeyCallback(windowHandler, (win, key, sCode, action, mods) -> {
			keyCallback(key, sCode, action, mods);
			keyCb.invoke(win, key, sCode, action, mods);
		});
	}

	private void keyCallback(int key, int scanCode, int action, int modifiers) {
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
