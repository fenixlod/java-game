package com.lunix.javagame.engine.scenes;

import java.util.Optional;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.VectorUtil;

public class TestScene extends Scene {
	private GameObject playerObject;

	public TestScene() {
		super();
		fileName = "test.json";
	}

	@Override
	public void init(Optional<String> loadFile) throws Exception {
		super.init(loadFile);
		game.window().clearColor(1f, 1f, 1f, 1f);
		game.camera().reset();
		ResourcePool.loadResources(ShaderType.DEFAULT, TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE);

		if (sceneLoaded)
			return;

		playerObject = new GameObject("Player")
				.addComponent(
						new SpriteRenderer(4, 5)
							.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
				);
		playerObject.addComponent(new Animation());
			addGameObject(playerObject);
			
		for (int j = 0; j < 4; j++) {
			int reverse = j > 1 ? -1 : 1;
			int isXAxis = j % 2 * reverse;
			int isYAxis = (1 - Math.abs(isXAxis)) * reverse;
			for (int i = 0; i < 100; i++) {
				GameObject obj = new GameObject("Marker " + i + " - " + j, new Vector3f(i * isXAxis * 5f, i * isYAxis * 5f, 0f))
					.addComponent(
						new SpriteRenderer(1,1)
							.color(isXAxis != 0 ? Color.blue() : Color.red())
							.widthDirection(VectorUtil.X())
							.heightDirection(VectorUtil.Y())
					);
				addGameObject(obj);
			}
		}
		
		GameObject enemy = new GameObject("Enemy", new Vector3f(-5f, 5f, 0f))
			.addComponent(
						new SpriteRenderer(2, 4)
					.color(Color.red())
			);
		addGameObject(enemy);
		
		// draw cuboid with dimensions: x=20, y=20, z=20
		// front
		GameObject rectangle = new GameObject("Cube1", new Vector3f(1f, 0f, 0f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.blue())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		// right
		rectangle = new GameObject("Cube2", new Vector3f(2f, 1f, 0f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.red())
					.widthDirection(VectorUtil.Y())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);

		//back
		rectangle = new GameObject("Cube3", new Vector3f(1f, 2f, 0f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.black())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		//left
		rectangle = new GameObject("Cube4", new Vector3f(0f, 1f, 0f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.yellow())
					.widthDirection(VectorUtil.minusY())
					.heightDirection(VectorUtil.Z())
					);
		addGameObject(rectangle);
		
		//top
		rectangle = new GameObject("Cube5", new Vector3f(1f, 0f, 2f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.cyan())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);
		
		
		//bottom
		rectangle = new GameObject("Cube6", new Vector3f(1f, 2f, 0f))
			.addComponent(
						new SpriteRenderer(2, 2)
					.color(Color.magenta())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.minusY())
			);
		addGameObject(rectangle);
		
		enemy = new GameObject("Enemy1", new Vector3f(0f, -10f, 0f))
				.addComponent(
						new SpriteRenderer(5, 10)
						.color(Color.red())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 0))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy2", new Vector3f(10f, -10f, 0f))
				.addComponent(
						new SpriteRenderer(5, 10)
						.color(Color.green())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 1))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy3", new Vector3f(20f, -10f, 0f))
				.addComponent(
						new SpriteRenderer(5, 10)
						.color(Color.blue())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 2))
				);
		addGameObject(enemy);
	}
}
