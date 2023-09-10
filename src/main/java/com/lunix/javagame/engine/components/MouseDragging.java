package com.lunix.javagame.engine.components;

import static org.lwjgl.glfw.GLFW.*;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.GameInstance;

public class MouseDragging extends Component {
	private boolean isPicked;

	public void pickup() {
		this.isPicked = true;
	}

	public void place() {
		this.isPicked = false;
	}

	@Override
	public void update(float deltaTime) {
		if (isPicked) {
			owner.transform().position(GameInstance.get().mouse().worldPositionProjected());
			if (GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
				place();
			}
		}
	}
}
