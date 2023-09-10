package com.lunix.javagame.engine.graphic;

import org.joml.Vector3f;

public class Line {
	private Vector3f start;
	private Vector3f end;
	private Color color;
	private int lifetime;

	public Line(Vector3f start, Vector3f end, Color color, int lifetime) {
		this.start = start;
		this.end = end;
		this.color = color;
		this.lifetime = lifetime;
	}

	public int beginFrame() {
		return this.lifetime--;
	}

	public Vector3f start() {
		return start;
	}

	public Line start(Vector3f start) {
		this.start = start;
		return this;
	}

	public Vector3f end() {
		return end;
	}

	public Line end(Vector3f end) {
		this.end = end;
		return this;
	}

	public Color color() {
		return color;
	}

	public Line color(Color color) {
		this.color = color;
		return this;
	}

	public int lifetime() {
		return lifetime;
	}

	public Line lifetime(int lifetime) {
		this.lifetime = lifetime;
		return this;
	}
}
