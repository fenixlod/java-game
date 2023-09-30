package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.Debugger;

public class BoxCollider extends Collider {
	private Vector3f size;

	public BoxCollider() {
		this(1);
	}

	public BoxCollider(float size) {
		this.size = new Vector3f(1);
	}

	public Vector3f size() {
		return size;
	}

	public BoxCollider size(Vector3f size) {
		this.size = size;
		return this;
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		Vector3f center = owner.transform().positionCopy().add(offset());

		if (!isPlaying)
			Debugger.addBoxCentered(center, size.x, size.y, Color.black(), 1);
	}
}
