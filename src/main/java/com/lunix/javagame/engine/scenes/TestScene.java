package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.Debugger;
import com.lunix.javagame.engine.util.VectorUtil;

public class TestScene extends Scene {
	private GameObject playerObject;

	public TestScene(GameSceneType type) {
		super(type);
	}

	@Override
	public void init() throws Exception {
		super.init();
		game.window().clearColor(1f, 1f, 1f, 1f);
		game.camera().setOrthoProjection();
		// game.camera().setPerspectiveProjection(size[0] / size[1]);
		game.camera().position(new Vector3f());
		
		ResourcePool.loadResources(ShaderType.DEFAULT, TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE);

		this.playerObject = new GameObject("Player")
				.addComponent(
						new SpriteRenderer(40, 50)
							.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
				);
		playerObject.addComponent(new Animation(ResourcePool.getSprites(TextureType.PLAYER_IDLE), 0.3f));
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
		
		GameObject enemy = new GameObject("Enemy", new Vector3f(-50f, 50f, 0f))
			.addComponent(
				new SpriteRenderer(20, 40)
					.color(Color.red())
			);
		addGameObject(enemy);
		
		// draw cuboid with dimensions: x=20, y=20, z=20
		// front
		GameObject rectangle = new GameObject("Cube1", new Vector3f(10f, 0f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.blue())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		// right
		rectangle = new GameObject("Cube2", new Vector3f(20f, 10f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.red())
					.widthDirection(VectorUtil.Y())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);

		//back
		rectangle = new GameObject("Cube3", new Vector3f(10f, 20f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.black())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		//left
		rectangle = new GameObject("Cube4", new Vector3f(0f, 10f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.yellow())
					.widthDirection(VectorUtil.minusY())
					.heightDirection(VectorUtil.Z())
					);
		addGameObject(rectangle);
		
		//top
		rectangle = new GameObject("Cube5", new Vector3f(10f, 0f, 20f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.cyan())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);
		
		
		//bottom
		rectangle = new GameObject("Cube6", new Vector3f(10f, 20f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.magenta())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.minusY())
			);
		addGameObject(rectangle);
		
		enemy = new GameObject("Enemy1", new Vector3f(0f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.red())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 0))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy2", new Vector3f(100f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.green())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 1))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy3", new Vector3f(200f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.blue())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY.name() + 2))
				);
		addGameObject(enemy);
		
//		logger.info("Creating game objects...");
//		for (int i = 0; i < 200; i++) {
//			float yPos = (100 - i) * 100f;
//			for(int j = 0; j < 200; j++) {
//				float xPos = (100 - j) * 100f;
//				GameObject ground = new GameObject("Ground " + i + " - " + j, new Vector3f(xPos, yPos, 0f))
//						.addComponent(
//							new SpriteRenderer(100, 100)
//								.heightDirection(VectorUtil.Y())
//								.color(new Color(0f, 1f, 0f, 0.5f))
//								.shader(ShaderType.DEFAULT)
//								.isStatic(true)
//						);
//				addGameObject(ground);
//			}
//		}
	}

	@Override
	public void update(float deltaTime) throws Exception {
		Vector3f offset = new Vector3f();

		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT))
			offset.x += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT))
			offset.x -= 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_UP))
			offset.y += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN))
			offset.y -= 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP))
			offset.z += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN))
			offset.z -= 50f * deltaTime;
		
		float zoomChange = (float) game.mouse().scroll().y * 0.1f;
		game.camera().move(offset);
		if (zoomChange != 0f)
			game.camera().changeZoom(zoomChange);
		
		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		Debugger.display(false, "X={}, Y={}, Z={}", game.camera().position().x, game.camera().position().y,	game.camera().position().z);

		this.playerObject.move(offset);
		super.update(deltaTime);
	}
}
