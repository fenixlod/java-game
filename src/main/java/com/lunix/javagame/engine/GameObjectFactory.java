package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.util.VectorUtil;

public class GameObjectFactory {

	/**
	 * Create new ground tile.
	 * 
	 * @param position
	 * @param texture
	 * @param width
	 * @param height
	 * @return
	 */
	public static GameObject groundTile(Vector3f position, int width, int height, TextureType texture, int textureIndex) {
		return new GameObject("Ground", position.add(0f, -height / 2f, 0f))
		.addComponent(
			new SpriteRenderer(width, height)
				.widthDirection(VectorUtil.X())
				.heightDirection(VectorUtil.Y())
				.sprite(ResourcePool.getSprite(texture, textureIndex))
		);
	}
}
