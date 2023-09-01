package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.util.VectorUtil;

public class Transform {
	private Vector3f position;
	private Vector3f direction;
	private Vector3f scale;

	public Transform(Vector3f position, Vector3f direction, Vector3f scale) {
		this.position = position;
		this.direction = direction;
		this.scale = scale;
	}

	public Transform(Vector3f position, Vector3f direction) {
		this(position, direction, new Vector3f(1f, 1f, 1f));
	}

	public Transform(Vector3f position) {
		this(position, VectorUtil.X());
	}

	public Transform() {
		this(new Vector3f());
	}

	public Transform position(Vector3f newPosition) {
		this.position = newPosition;
		return this;
	}

	public Transform direction(Vector3f newDirection) {
		this.direction = newDirection;
		return this;
	}

	public Transform scale(Vector3f newScale) {
		this.scale = newScale;
		return this;
	}

	public Vector3f position() {
		return position;
	}

	public Vector3f scale() {
		return scale;
	}

	public Vector3f direction() {
		return direction;
	}

	public Transform move(Vector3f offset) {
		this.position.add(offset);
		return this;
	}

	public Transform copy() {
		return new Transform(new Vector3f(position), new Vector3f(direction), new Vector3f(scale));
	}

	public void copy(Transform dest) {
		dest.position(new Vector3f(position)).direction(new Vector3f(direction)).scale(new Vector3f(scale));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof Transform trans) {
			return this.position.equals(trans.position()) && this.direction.equals(trans.direction())
					&& this.scale.equals(trans.scale());
		} else
			return false;
	}
}
