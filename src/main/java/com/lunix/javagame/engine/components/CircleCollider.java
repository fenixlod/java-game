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
		Vector3f center = owner.transform().position().add(offset(), new Vector3f());
		Debugger.addCircle(center, VectorUtil.Z(), radius, Color.black(), 1);
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
		return 50;
	}
}
