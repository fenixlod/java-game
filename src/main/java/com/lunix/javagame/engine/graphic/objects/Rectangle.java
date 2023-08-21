package com.lunix.javagame.engine.graphic.objects;

import com.lunix.javagame.engine.graphic.Vertex;

/**
 * Vertex should be ordered in counter-clockwise direction
 */
public class Rectangle extends Shape {

	public Rectangle(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		super(new Vertex[4]);
		vertices(v1, v2, v3, v4);
	}

	public Rectangle v1(Vertex v) {
		this.vertices[0] = v;
		return this;
	}

	public Rectangle v2(Vertex v) {
		this.vertices[1] = v;
		return this;
	}

	public Rectangle v3(Vertex v) {
		this.vertices[2] = v;
		return this;
	}

	public Rectangle v4(Vertex v) {
		this.vertices[3] = v;
		return this;
	}

	public Rectangle vertices(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		this.vertices[0] = v1;
		this.vertices[1] = v2;
		this.vertices[2] = v3;
		this.vertices[3] = v4;
		return this;
	}

	public Vertex v1() {
		return this.vertices[0];
	}

	public Vertex v2() {
		return this.vertices[1];
	}

	public Vertex v3() {
		return this.vertices[2];
	}

	public Vertex v4() {
		return this.vertices[3];
	}

	@Override
	public int[] getElementArray() {
		return new int[] { 0, 1, 2, 2, 3, 0 };
	}
}
