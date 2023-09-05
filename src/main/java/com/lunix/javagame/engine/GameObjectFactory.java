package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.util.VectorUtil;

public class GameObjectFactory {

	public static GameObject GroundTile(Vector3f position, TextureType texture, int width, int height) {
		return new GameObject("Ground", position.add(0f, -height / 2f, 0f))
		.addComponent(
			new SpriteRenderer(width, height)
				.widthDirection(VectorUtil.X())
				.heightDirection(VectorUtil.Y())
				.sprite(new Sprite(texture))
		);
	}
}
