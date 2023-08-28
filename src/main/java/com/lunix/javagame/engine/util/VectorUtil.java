package com.lunix.javagame.engine.util;

import org.joml.Vector3f;

public class VectorUtil {
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
}
