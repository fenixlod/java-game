package com.lunix.javagame.engine.graphic;

import org.joml.Vector3f;

public class Point {
	private float[] coords = new float[3];

	public static final Point absoluteZero = new Point(0.0f, 0.0f, 0.0f);

	public Point() {
		this(0.0f, 0.0f, 0.0f);
	}

	public Point(float xValue, float yValue, float zValue) {
		coords(xValue, yValue, zValue);
	}

	public Point(Vector3f point) {
		coords(point.x, point.y, point.z);
	}

	public Point(Point point) {
		coords(point);
	}

	public Point x(float xValue) {
		this.coords[0] = xValue;
		return this;
	}

	public Point y(float yValue) {
		this.coords[1] = yValue;
		return this;
	}

	public Point z(float zValue) {
		this.coords[2] = zValue;
		return this;
	}

	public Point coords(float xValue, float yValue, float zValue) {
		this.coords[0] = xValue;
		this.coords[1] = yValue;
		this.coords[2] = zValue;
		return this;
	}

	public Point coords(Point point) {
		return coords(point.x(), point.y(), point.z());
	}

	public float x() {
		return this.coords[0];
	}

	public float y() {
		return this.coords[1];
	}

	public float z() {
		return this.coords[2];
	}

	@Override
	public String toString() {
		return "Point [x=" + coords[0] + ", y=" + coords[1] + ", z=" + coords[2] + "]";
	}

}
