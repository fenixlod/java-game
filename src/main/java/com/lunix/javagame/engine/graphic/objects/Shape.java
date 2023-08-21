package com.lunix.javagame.engine.graphic.objects;

import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Vertex;

public abstract class Shape {
	protected Vertex[] vertices;

	public Shape(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public final float[] getVertexArray(Color defaultColor) {
		float[] vertexArray = new float[9 * vertices.length];
		int offset = 0;
		for (int i = 0; i < vertices.length; i++) {
			offset = vertices[i].toVertexArray(vertexArray, offset, defaultColor);
		}
		return vertexArray;
	}

	abstract public int[] getElementArray();
}
