package com.lunix.javagame.engine.graphic;

public class Vertex {
	private Point position;
	private Color color;
	private float[] textureUV;

	public Vertex(Point position) {
		this.position = position;
	}

	public Vertex(float xPos, float yPos, float zPos) {
		this.position = new Point(xPos, yPos, zPos);
	}

	public Vertex(float xPos, float yPos, float zPos, Color color) {
		this.position = new Point(xPos, yPos, zPos);
		this.color = color;
	}

	public Vertex(Point position, Color color) {
		this.position = position;
		this.color = color;
	}

	public Vertex(Point position, Color color, float[] textureUV) {
		this.position = position;
		this.color = color;
		this.textureUV = textureUV;
	}

	public Vertex position(Point position) {
		this.position = position;
		return this;
	}

	public Vertex color(Color color) {
		this.color = color;
		return this;
	}

	public Vertex textureMapping(float[] uv) {
		this.textureUV = uv;
		return this;
	}

	/**
	 * Add Vertex to vertexArray for drawing
	 * 
	 * @param vertexArray - the array
	 * @param offset      - offset to place the data
	 * @return the new offset
	 */
	public int toVertexArray(float[] vertexArray, int offset, Color defaultColor) {
		vertexArray[offset++] = position.x();
		vertexArray[offset++] = position.y();
		vertexArray[offset++] = position.z();

		vertexArray[offset++] = color == null ? defaultColor.r() : color.r();
		vertexArray[offset++] = color == null ? defaultColor.g() : color.g();
		vertexArray[offset++] = color == null ? defaultColor.b() : color.b();
		vertexArray[offset++] = color == null ? defaultColor.a() : color.a();

		vertexArray[offset++] = textureUV == null ? 0.0f : textureUV[0];
		vertexArray[offset++] = textureUV == null ? 0.0f : textureUV[1];
		return offset;
	}
}
