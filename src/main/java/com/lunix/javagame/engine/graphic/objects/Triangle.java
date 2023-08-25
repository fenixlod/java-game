package com.lunix.javagame.engine.graphic.objects;

import org.joml.Vector3f;

import com.lunix.javagame.engine.graphic.Point;
import com.lunix.javagame.engine.graphic.Vertex;

/**
 * Vertex should be ordered in counter-clockwise direction
 */
public class Triangle extends Shape {

	public Triangle(Vertex v1, Vertex v2, Vertex v3) {
		super(new Vertex[3]);
		vertices(v1, v2, v3);
	}
	
	public static Triangle equilateral(Vector3f center, float size, Vector3f widthDirection, Vector3f hightDirection) {
		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);

		p1.add(widthDirection.x * size * 0.5f, widthDirection.y * size * 0.5f, widthDirection.z * size * 0.5f);
		p1.add(hightDirection.x * size * -0.5f, hightDirection.y * size * -0.5f, hightDirection.z * size * -0.5f);

		p2.add(hightDirection.x * size * 0.5f, hightDirection.y * size * 0.5f, hightDirection.z * size * 0.5f);

		p3.add(widthDirection.x * size * -0.5f, widthDirection.y * size * -0.5f, widthDirection.z * size * -0.5f);
		p3.add(hightDirection.x * size * -0.5f, hightDirection.y * size * -0.5f, hightDirection.z * size * -0.5f);

		return new Triangle(
				new Vertex(new Point(p1)), 
				new Vertex(new Point(p2)), 
				new Vertex(new Point(p3)));
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
