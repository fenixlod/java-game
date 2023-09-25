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
		Vector3f center = owner.transform().position().add(offset(), new Vector3f());
		Debugger.addBoxCentered(center, size.x, size.y, Color.black(), 1);
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
