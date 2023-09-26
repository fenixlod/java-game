package com.lunix.javagame.engine.components;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.GameInstance;

public final class MouseDragging extends Component {
	private transient boolean isPicked;

	public MouseDragging pickup() {
		isPicked = true;
		return this;
	}

	public void place() {
		isPicked = false;
		destroyed = true;
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		if (!isPicked)
			return;

		Vector3f worldPos = GameInstance.get().mouse().positionInWorldProjected();
		owner.transform().position(worldPos);
		if (GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
			place();
		} else if (GameInstance.get().keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			owner.destroy();
		}
	}

	public boolean isPicked() {
		return isPicked;
	}
}
