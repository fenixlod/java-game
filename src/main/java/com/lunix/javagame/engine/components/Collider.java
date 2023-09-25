package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;

public abstract class Collider extends Component {
	private Vector3f offset;

	public Collider() {
		offset = new Vector3f();
	}

	public Vector3f offset() {
		return offset;
	}

	public void offset(Vector3f offset) {
		this.offset = offset;
	}
}
