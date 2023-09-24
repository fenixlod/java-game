package com.lunix.javagame.engine.graphic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	private static Shader overrideShader;

	public Renderer() {
		batches = new ArrayList<>();
	}

	/**
	 * Initialize the renderer
	 */
	public void init() {
		batches.clear();
	}

	/**
	 * Add game object for drawing.
	 * 
	 * @param obj
	 * @throws ResourceNotFound
	 * @throws IOException
	 */
	public void add(GameObject obj) throws ResourceNotFound, IOException {
		SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);

		if (sprite != null)
			add(sprite);
	}

	/**
	 * Add sprite for rendering.
	 * 
	 * @param sprite
	 * @throws ResourceNotFound
	 * @throws IOException
	 */
	private void add(SpriteRenderer sprite) throws ResourceNotFound, IOException {
		for (RenderBatch batch : batches) {
			if (batch.haveRoom() && sprite.shader() == batch.shader()) {
				TextureType texture = sprite.textureType();
				if (texture == TextureType.NONE || batch.hasTexture(texture) || batch.haveTextureRoom()) {
					batch.addSprite(sprite);
					return;
				}
			}
		}

		RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.shader());
		newBatch.start();
		newBatch.addSprite(sprite);
		batches.add(newBatch);
	}

	/**
	 * Render all screen elements.
	 * 
	 * @throws Exception
	 */
	public void render() throws Exception {
		for (RenderBatch batch : batches) {
			batch.render(overrideShader);
		}
	}

	public static void overrideShader(Shader shader) {
		overrideShader = shader;
	}

	/**
	 * Remove game object for drawing.
	 * 
	 * @param obj
	 * @throws ResourceNotFound
	 * @throws IOException
	 */
	public void remove(GameObject obj) throws ResourceNotFound, IOException {
		SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);

		for (RenderBatch batch : batches) {
			if (batch.removeSprite(sprite))
				break;
		}
	}
}
