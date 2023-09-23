package com.lunix.javagame.engine.components;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.GameInstance;

public final class MouseDragging extends Component {
	private transient boolean isPicked;

	public MouseDragging() {
		temporary = true;
	}

	public MouseDragging pickup() {
		isPicked = true;
		return this;
	}

	public void place() {
		isPicked = false;
	}

	@Override
	public void update(float deltaTime) {
		if (!isPicked)
			return;

		Vector3f worldPos = GameInstance.get().mouse().positionInWorldProjected();
		owner.transform().position(worldPos);
		if (GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
			place();
		}
	}

	public boolean isPicked() {
		return isPicked;
	}

	/**
	 * All Components needs to implement this method. This value determine the order
	 * of execution of components within a game object. The lower the priority value
	 * = the sooner this component will be executed. Priority of 1 - first to
	 * execute, 1000 - last to execute.
	 * 
	 * @return
	 */
	public static int priority() {
		return 200;
	}
}
