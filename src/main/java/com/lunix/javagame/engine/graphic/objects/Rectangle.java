package com.lunix.javagame.engine.graphic.objects;

import org.joml.Vector3f;

import com.lunix.javagame.engine.graphic.Point;
import com.lunix.javagame.engine.graphic.Vertex;

/**
 * Vertex should be ordered in counter-clockwise direction
 */
public class Rectangle extends Shape {

	public Rectangle(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		super(new Vertex[4]);
		vertices(v1, v2, v3, v4);
	}

	public static Rectangle sized(Vector3f center, float width, Vector3f widthDirection, float hight,
			Vector3f hightDirection) {
		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);
		Vector3f p4 = new Vector3f(center);

		p1.add(widthDirection.x * width * 0.5f, widthDirection.y * width * 0.5f, widthDirection.z * width * 0.5f);
		p1.add(hightDirection.x * hight * -0.5f, hightDirection.y * hight * -0.5f, hightDirection.z * hight * -0.5f);

		p2.add(widthDirection.x * width * 0.5f, widthDirection.y * width * 0.5f, widthDirection.z * width * 0.5f);
		p2.add(hightDirection.x * hight * 0.5f, hightDirection.y * hight * 0.5f, hightDirection.z * hight * 0.5f);

		p3.add(widthDirection.x * width * -0.5f, widthDirection.y * width * -0.5f, widthDirection.z * width * -0.5f);
		p3.add(hightDirection.x * hight * 0.5f, hightDirection.y * hight * 0.5f, hightDirection.z * hight * 0.5f);

		p4.add(widthDirection.x * width * -0.5f, widthDirection.y * width * -0.5f, widthDirection.z * width * -0.5f);
		p4.add(hightDirection.x * hight * -0.5f, hightDirection.y * hight * -0.5f, hightDirection.z * hight * -0.5f);

		return new Rectangle(
				new Vertex(new Point(p1)), 
				new Vertex(new Point(p2)), 
				new Vertex(new Point(p3)),
				new Vertex(new Point(p4)));
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
