package com.lunix.javagame.engine.components;

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
}
