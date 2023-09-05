package com.lunix.javagame.engine.util;

import org.joml.Vector3f;

public class VectorUtil {
	static private Vector3f viewX;
	static private Vector3f viewY;
	static private Vector3f viewZ;
	static private Vector3f viewDirection;

	public static Vector3f X() {
		return new Vector3f(1f, 0f, 0f);
	}

	public static Vector3f Y() {
		return new Vector3f(0f, 1f, 0f);
	}

	public static Vector3f Z() {
		return new Vector3f(0f, 0f, 1f);
	}

	public static Vector3f minusX() {
		return new Vector3f(-1f, 0f, 0f);
	}

	public static Vector3f minusY() {
		return new Vector3f(0f, -1f, 0f);
	}

	public static Vector3f minusZ() {
		return new Vector3f(0f, 0f, -1f);
	}

	/**
	 * Screen X (along the width) direction vector in world coordinated. This vector
	 * is parallel to the monitor width.
	 * 
	 * @return
	 */
	public static Vector3f viewX() {
		return new Vector3f(viewX);
	}

	/**
	 * Screen Y (along the height) direction vector in world coordinated. This
	 * vector is parallel to the monitor height. This vector is similar to the
	 * "viewZ" vector but is projected on the ground.
	 * 
	 * @return
	 */
	public static Vector3f viewY() {
		return new Vector3f(viewY);
	}

	/**
	 * Screen Z (along the height) direction vector in world coordinated. This
	 * vector is parallel to the monitor height.
	 * 
	 * @return
	 */
	public static Vector3f viewZ() {
		return new Vector3f(viewZ);
	}

	public static Vector3f viewDirection() {
		return new Vector3f(viewDirection);
	}

	public static void setView(Vector3f view) {
		viewDirection = view.normalize(new Vector3f()).mul(-1f);
		viewX = viewDirection.cross(VectorUtil.Z(), new Vector3f());
		viewZ = viewX.cross(viewDirection, new Vector3f());
		viewY = Z().cross(viewX, new Vector3f());
	}
}
