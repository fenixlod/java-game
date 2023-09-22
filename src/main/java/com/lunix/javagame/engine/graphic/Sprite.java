package com.lunix.javagame.engine.graphic;

import org.joml.Vector2f;

import com.lunix.javagame.engine.enums.TextureType;

public class Sprite {
	private final Vector2f[] textureCoords;
	private final TextureType texture;

	public Sprite(TextureType texture) {
		this.texture = texture;
		textureCoords = new Vector2f[] {
				new Vector2f(0f, 0f),
				new Vector2f(1f, 0f),
				new Vector2f(1f, 1f),
				new Vector2f(0f, 1f),
		};
	}

	public Sprite(TextureType texture, Vector2f[] textureCoords) {
		this.texture = texture;
		this.textureCoords = textureCoords;
	}
	
	public Sprite() {
		this(TextureType.NONE);
	}

	public TextureType texture() {
		return texture;
	}

	public Vector2f[] textureCoords() {
		return textureCoords;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof Sprite other) {
			return textureCoords.equals(other.textureCoords())
					&& ((texture == null && other.texture() == null)
							|| (texture == other.texture()));
		} else
			return false;
	}
}
