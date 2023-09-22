package com.lunix.javagame.engine.editor;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.util.VectorUtil;

public class MoveGizmo extends Gizmo {

	public MoveGizmo(String name, SpriteRenderer spriteRenderer) {
		super(name, spriteRenderer);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (attachedTo == null || !isSelected)
			return;

		if (GameInstance.get().mouse().dragging() && GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector3f mousePos = new Vector3f(GameInstance.get().mouse().positionInWorldProjected());
			transform().position(mousePos.add(VectorUtil.viewDirection().mul(offsetFromObject), new Vector3f()));
			attachedTo.transform().position(mousePos);
		}
	}
}
