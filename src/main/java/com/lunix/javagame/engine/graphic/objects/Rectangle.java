package com.lunix.javagame.engine.graphic.objects;

import org.joml.Vector3f;

import com.lunix.javagame.engine.graphic.Vertex;

/**
 * Vertex should be ordered in counter-clockwise direction
 */
public class Rectangle extends Shape {

	public Rectangle(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		super(new Vertex[4]);
		vertices(v1, v2, v3, v4);
	}

	public static Rectangle centered(Vector3f center, float width, Vector3f widthDirection, float hight,
			Vector3f hightDirection) {

		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);
		Vector3f p4 = new Vector3f(center);

		p1.add(widthDirection.mul(-width / 2, new Vector3f())).add(hightDirection.mul(-hight / 2, new Vector3f()));
		p2.add(widthDirection.mul(width / 2, new Vector3f())).add(hightDirection.mul(-hight / 2, new Vector3f()));
		p3.add(widthDirection.mul(width / 2, new Vector3f())).add(hightDirection.mul(hight / 2, new Vector3f()));
		p4.add(widthDirection.mul(-width / 2, new Vector3f())).add(hightDirection.mul(hight / 2, new Vector3f()));

		return new Rectangle(new Vertex(p1).uv(0, 1),
				new Vertex(p2).uv(1, 1),
				new Vertex(p3).uv(1, 0), 
				new Vertex(p4).uv(0, 0));
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
