package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.util.VectorUtil;

public class Transform {
	private Vector3f position;
	private Vector3f facing;
	private Vector3f scale;

	public Transform() {
		this(new Vector3f());
	}

	public Transform(Vector3f position) {
		this(position, VectorUtil.X());
	}

	public Transform(Vector3f position, Vector3f facing) {
		this(position, facing, new Vector3f(1f, 1f, 1f));
	}

	public Transform(Vector3f position, Vector3f facing, Vector3f scale) {
		this.position = position;
		this.facing = facing;
		this.scale = scale;
	}

	public Transform position(Vector3f newPosition) {
		this.position = newPosition;
		return this;
	}

	public Vector3f position() {
		return position;
	}

	public Transform facing(Vector3f newFacing) {
		this.facing = newFacing;
		return this;
	}

	public Vector3f facing() {
		return facing;
	}

	public Transform scale(Vector3f newScale) {
		this.scale = newScale;
		return this;
	}

	public Vector3f scale() {
		return scale;
	}

	public Transform move(Vector3f offset) {
		this.position.add(offset);
		return this;
	}

	public Transform copy() {
		return new Transform(new Vector3f(position), new Vector3f(facing), new Vector3f(scale));
	}

	public void copy(Transform dest) {
		dest.position(new Vector3f(position)).facing(new Vector3f(facing)).scale(new Vector3f(scale));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof Transform trans) {
			return this.position.equals(trans.position()) && this.facing.equals(trans.facing())
					&& this.scale.equals(trans.scale());
		} else
			return false;
	}
}
