package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.VectorUtil;

public class TestScene extends Scene {
	private GameInstance game;
	private GameObject playerObject;

	@Override
	public void init() {
		super.init();
		this.game = GameInstance.get();
		game.window().setClearColor(1f, 1f, 1f, 1f);
		// game.camera().setOrthoProjection();
		game.camera().setPerspectiveProjection(game.window().getAspectRatio());
		game.camera().setPosition(new Vector3f());
		
		logger.info("Creating game objects...");
		this.playerObject = new GameObject("Player")
			.addComponent(
				new SpriteRenderer(20, 40)
					.color(Color.green)
					.shader(ShaderType.NO_PERSPECTIVE)
					.offset(new Vector3f(0f, 0f, 20f))
			);
		addGameObject(playerObject);
		
		GameObject enemy =  new GameObject("Enemy", new Vector3f(-50f, 50f, 0f))
			.addComponent(
				new SpriteRenderer(10, 20)
					.color(Color.red)
					.shader(ShaderType.DEFAULT)
					.offset(VectorUtil.Z().mul(10))
			);
		addGameObject(enemy);
		
		// draw cuboid with dimensions: x=50, y=30, z=10
		// front
		GameObject rectangle =  new GameObject("Cube1", new Vector3f(25f, 15f, 0f))
			.addComponent(
				new SpriteRenderer(50, 30)
					.color(Color.blue)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);
		
		// right
		rectangle =  new GameObject("Cube2", new Vector3f(50f, 15f, 5f))
			.addComponent(
				new SpriteRenderer(10, 30)
					.color(Color.red)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.minusZ())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);

		//back
		rectangle =  new GameObject("Cube3", new Vector3f(25f, 15f, 10f))
			.addComponent(
				new SpriteRenderer(50, 30)
					.color(Color.black)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);
		
		//left
		rectangle =  new GameObject("Cube4", new Vector3f(0f, 15f, 5f))
			.addComponent(
				new SpriteRenderer(10, 30)
					.color(Color.yellow)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.Z())
					.heightDirection(VectorUtil.Y())
					);
		addGameObject(rectangle);
		
		//top
		rectangle =  new GameObject("Cube5", new Vector3f(25f, 30f, 5f))
			.addComponent(
				new SpriteRenderer(50, 10)
					.color(Color.cyan)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.minusZ())
			);
		addGameObject(rectangle);
		
		
		//bottom
		rectangle =  new GameObject("Cube6", new Vector3f(25f, 0f, 5f))
			.addComponent(
				new SpriteRenderer(50, 10)
					.color(Color.magenta)
					.shader(ShaderType.DEFAULT)
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		for (int j = 0; j < 4; j++) {
			int reverse = j > 1 ? -1 : 1;
			int isXAxis = j % 2 * reverse;
			int isYAxis = (1 - Math.abs(isXAxis)) * reverse;
			for (int i = 0; i < 100; i++) {
				GameObject obj = new GameObject("Marker " + i + " - " + j, new Vector3f(i * isXAxis * 5f, i * isYAxis * 5f, 0f))
					.addComponent(
						new SpriteRenderer(1,1)
							.color(isXAxis != 0 ? Color.blue : Color.red)
							.shader(ShaderType.DEFAULT)
							.heightDirection(VectorUtil.Y())
					);
				addGameObject(obj);
			}
		}
		super.start();
	}

	@Override
	public void update(float deltaTime) {
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
		
		float zoomChange = (float) game.mouse().getScrollY() * -0.1f;
		game.camera().move(offset);
		if (zoomChange != 0f)
			game.camera().changeZoom(zoomChange);
		
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		//logger.info("X={}, Y={}, Z={}", game.camera().position().x, game.camera().position().y,	game.camera().position().z);

		this.playerObject.move(offset);
		super.update(deltaTime);
	}
}
