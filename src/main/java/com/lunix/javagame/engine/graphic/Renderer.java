package com.lunix.javagame.engine.graphic;

import java.util.ArrayList;
import java.util.List;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	List<RenderBatch> batches;

	public Renderer() {
		this.batches = new ArrayList<>();
	}

	/**
	 * Add game object for drawing.
	 * 
	 * @param obj
	 * @throws ResourceNotFound
	 */
	public void add(GameObject obj) throws ResourceNotFound {
		SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);

		if (sprite != null)
			add(sprite);
	}

	/**
	 * Add sprite for rendering.
	 * 
	 * @param sprite
	 * @throws ResourceNotFound
	 */
	private void add(SpriteRenderer sprite) throws ResourceNotFound {
		for (RenderBatch batch : this.batches) {
			if (batch.haveRoom() && sprite.shader() == batch.shader()) {
				TextureType texture = sprite.texture();
				if (texture == TextureType.NONE || batch.hasTexture(texture) || batch.haveTextureRoom()) {
					batch.addSprite(sprite);
					return;
				}
			}
		}

		RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.shader());
		newBatch.start();
		newBatch.addSprite(sprite);
		this.batches.add(newBatch);
	}

	/**
	 * Render all screen elements.
	 * 
	 * @throws Exception
	 */
	public void render() throws Exception {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
}
