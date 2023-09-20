package com.lunix.javagame.engine.editor;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.components.SpriteRenderer;

public class TranslateGizmo extends Gizmo {
	private Vector3f direction;

	public TranslateGizmo(String name, SpriteRenderer spriteRenderer, Vector3f direction) {
		super(name, spriteRenderer);
		this.direction = direction;
	}

	private final float DRAG_SENSITIVITY = 0.5f;// .15f;

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (this.attachedTo == null || !isSelected)
			return;

		if (GameInstance.get().mouse().dragging() && GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector3f offset = GameInstance.get().mouse().deltaInWorld().mul(DRAG_SENSITIVITY, new Vector3f());
			Vector3f change = direction.mul(offset.dot(direction), new Vector3f());
			this.transform().move(change);
			this.attachedTo.transform().move(change);
		}
	}
}
