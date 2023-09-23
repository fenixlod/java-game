package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

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
}
