package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SnapToGrid;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.AnimationStateType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.struct.AnimationState;
import com.lunix.javagame.engine.util.VectorUtil;

public class Prefabs {

	/**
	 * Create new ground tile.
	 * 
	 * @param position
	 * @param texture
	 * @param width
	 * @param height
	 * @return
	 */
	public static GameObject groundTile(Vector3f position, int width, int height, String spriteName) {
		return new GameObject("Ground", position)
		.addComponent(
			new SpriteRenderer(width, height)
				.widthDirection(VectorUtil.X())
				.heightDirection(VectorUtil.Y())
				.sprite(ResourcePool.getSprite(spriteName))
				.offset(new Vector3f(0f, -height / 2f, 0f)))
		.addComponent(new SnapToGrid(10));
	}
	
	public static GameObject player(Vector3f position) {
		return new GameObject("Player")
			.addComponent(
				new SpriteRenderer(4, 5)
					.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
			)
			.addComponent(
				new Animation()
					.defaultState(AnimationStateType.IDLE)
					.addState(
						new AnimationState(AnimationStateType.IDLE)
							.addFrame(ResourcePool.getSprite(TextureType.PLAYER_IDLE, 0), 0.3f)
							.addFrame(ResourcePool.getSprite(TextureType.PLAYER_IDLE, 1), 0.3f)
							.addFrame(ResourcePool.getSprite(TextureType.PLAYER_IDLE, 2), 0.3f)
							.addFrame(ResourcePool.getSprite(TextureType.PLAYER_IDLE, 3), 0.3f)
							.looping(true)
					)
			);
	}
}
