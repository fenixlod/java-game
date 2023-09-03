package com.lunix.javagame.engine.util;

import org.joml.Vector3f;

public class VectorUtil {
	static private Vector3f viewX;
	static private Vector3f viewY;
	static private Vector3f viewZ;

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

	public static Vector3f viewX() {
		return viewX;
	}

	public static Vector3f viewY() {
		return viewY;
	}

	public static Vector3f viewZ() {
		return viewZ;
	}
	public static void setView(Vector3f view) {
		Vector3f viewDir = view.normalize(new Vector3f()).mul(-1f);
		viewX = viewDir.cross(VectorUtil.Z(), new Vector3f());
		viewZ = viewX.cross(viewDir, new Vector3f());
		viewY = Z().cross(viewX, new Vector3f());
	}
}
