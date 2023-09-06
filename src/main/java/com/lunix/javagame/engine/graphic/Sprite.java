package com.lunix.javagame.engine.graphic;

import org.joml.Vector2f;

public class Sprite {
	private final Vector2f[] textureCoords;
	private final Texture texture;
	private int width;
	private int height;

	public Sprite(Texture texture, int width, int height) {
		this.texture = texture;
		this.textureCoords = new Vector2f[] {
				new Vector2f(0f, 1f),
				new Vector2f(1f, 1f),
				new Vector2f(1f, 0f),
				new Vector2f(0f, 0f),
		};
		this.width = width;
		this.height = height;
	}

	public Sprite(Texture texture, Vector2f[] textureCoords, int width, int height) {
		this.texture = texture;
		this.textureCoords = textureCoords;
		this.width = width;
		this.height = height;
	}
	
	public Sprite() {
		this(null, 0, 0);
	}

	public Texture texture() {
		return this.texture;
	}

	public Vector2f[] textureCoords() {
		return this.textureCoords;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof Sprite other) {
			return this.textureCoords.equals(other.textureCoords())
					&& ((this.texture == null && other.texture() == null)
							|| (this.texture.type() == other.texture().type()));
		} else
			return false;
	}

	public int width() {
		if (this.width == 0)
			this.width = texture.width();

		return this.width;
	}

	public int height() {
		if (this.height == 0)
			this.height = texture.height();

		return this.height;
	}
}
