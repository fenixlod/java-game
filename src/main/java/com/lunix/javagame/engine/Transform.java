package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.enums.ObjectEventType;
import com.lunix.javagame.engine.util.VectorUtil;

public class Transform {
	private Vector3f position;
	private Vector3f facing;
	private Vector3f scale;
	protected transient GameObject owner;

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

	public void owner(GameObject owner) {
		this.owner = owner;
	}

	public Transform position(Vector3f newPosition) {
		if (!position.equals(newPosition)) {
			position.set(newPosition);
			owner.sendEvent(ObjectEventType.POSITION_CHANGED);
		}
		return this;
	}

	/**
	 * Returns copy of the position; This copy cannot be used to set position to
	 * this transform.
	 * 
	 * @return
	 */
	public Vector3f positionCopy() {
		return new Vector3f(position);
	}

	/**
	 * Returns copy of the facing; This copy cannot be used to set facing to this
	 * transform.
	 * 
	 * @return
	 */
	public Transform facing(Vector3f newFacing) {
		if (!facing.equals(newFacing)) {
			facing.set(newFacing);
			owner.sendEvent(ObjectEventType.FACING_CHANGED);
		}
		return this;
	}

	public Vector3f facingCopy() {
		return new Vector3f(facing);
	}

	/**
	 * Returns copy of the scale; This copy cannot be used to set scale to this
	 * transform.
	 * 
	 * @return
	 */
	public Transform scale(Vector3f newScale) {
		if (!scale.equals(newScale)) {
			scale.set(newScale);
			owner.sendEvent(ObjectEventType.SCALE_CHANGED);
		}
		return this;
	}

	public Vector3f scaleCopy() {
		return new Vector3f(scale);
	}

	public Transform move(Vector3f offset) {
		if (offset.lengthSquared() > 0) {
			position.add(offset);
			owner.sendEvent(ObjectEventType.POSITION_CHANGED);
		}
		return this;
	}

	public void copy(Transform dest) {
		dest.position(new Vector3f(position)).facing(new Vector3f(facing)).scale(new Vector3f(scale));
	}
}
