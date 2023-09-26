package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.Debugger;
import com.lunix.javagame.engine.util.VectorUtil;

public class CircleCollider extends Collider {
	private float radius;

	public CircleCollider() {
		this.radius = 1;
	}

	public CircleCollider(float radius) {
		this.radius = radius;
	}

	public float radius() {
		return radius;
	}

	public CircleCollider radius(float radius) {
		this.radius = radius;
		return this;
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		Vector3f center = owner.transform().positionCopy().add(offset());
		Debugger.addCircle(center, VectorUtil.Z(), radius, Color.black(), 1);
	}
}
