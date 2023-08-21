package com.lunix.javagame.engine.graphic.objects;

import com.lunix.javagame.engine.graphic.Vertex;

/**
 * Vertex should be ordered in counter-clockwise direction
 */
public class Triangle extends Shape {

	public Triangle(Vertex v1, Vertex v2, Vertex v3) {
		super(new Vertex[3]);
		vertices(v1, v2, v3);
	}

	public Triangle v1(Vertex v) {
		this.vertices[0] = v;
		return this;
	}

	public Triangle v2(Vertex v) {
		this.vertices[1] = v;
		return this;
	}

	public Triangle v3(Vertex v) {
		this.vertices[2] = v;
		return this;
	}

	public Triangle vertices(Vertex v1, Vertex v2, Vertex v3) {
		this.vertices[0] = v1;
		this.vertices[1] = v2;
		this.vertices[2] = v3;
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

	@Override
	public int[] getElementArray() {
		return new int[] { 0, 1, 2 };
	}
}
