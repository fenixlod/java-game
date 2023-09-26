package com.lunix.javagame.engine.editor;

import static org.lwjgl.glfw.GLFW.*;

import java.text.DecimalFormat;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.components.SpriteRenderer;

public class ScaleGizmo extends Gizmo {
	private static final DecimalFormat df = new DecimalFormat("0.0");
	private Vector3f direction;
	private Vector3f baseOffset;

	public ScaleGizmo(String name, SpriteRenderer spriteRenderer, Vector3f direction) {
		super(name, spriteRenderer);
		this.direction = direction;
		baseOffset = new Vector3f(spriteRenderer.positionOffset());
	}

	private final float SCALE_SENSITIVITY = 1f;// .15f;

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		super.update(deltaTime, isPlaying);

		if (attachedTo == null || !isSelected || isPlaying)
			return;

		if (GameInstance.get().mouse().dragging() && GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector3f offset = GameInstance.get().mouse().deltaInWorld();
			Vector3f change = direction.mul(offset.dot(direction), new Vector3f()).mul(SCALE_SENSITIVITY);
			spriteRenderer.offset(spriteRenderer.positionOffset()
					.add(change.mul(1 / SCALE_SENSITIVITY, new Vector3f()), new Vector3f()));

			change.set(Float.parseFloat(df.format(change.x)), Float.parseFloat(df.format(change.y)),
					Float.parseFloat(df.format(change.z)));
			attachedTo.transform().scale(attachedTo.transform().scaleCopy().add(change));
		}
	}

	@Override
	public void attach(GameObject object) {
		super.attach(object);
		Vector3f change = direction.mul(attachedTo.transform().scaleCopy().dot(direction), new Vector3f())
				.mul(2.5f / SCALE_SENSITIVITY);
		spriteRenderer.offset(baseOffset.add(change, new Vector3f()));
	}
}
