package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Camera;
import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.objects.Rectangle;
import com.lunix.javagame.engine.graphic.objects.ScreenElement;

public class TestScene extends Scene {
	private ScreenElement rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6;
	private Camera camera;

	@Override
	public void init(GameInstance game) {
		super.init(game);
		game.window().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// draw cuboid with dimensions: x=50, y=30, z=10
		// front
		rectangle1 = new ScreenElement();
		rectangle1.shape(Rectangle.sized(new Vector3f(25.0f, 15.0f, 0.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.red)
			.shader(game.getResources().getShader("default"))
			.upload();
		
		// right
		rectangle2 = new ScreenElement();
		rectangle2.shape(Rectangle.sized(new Vector3f(50.0f, 15.0f, -5.0f),
				10, new Vector3f(0.0f, 0.0f, -1.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.green)
			.shader(game.getResources().getShader("default"))
			.upload();

		//back
		rectangle3 = new ScreenElement();
		rectangle3.shape(Rectangle.sized(new Vector3f(25.0f, 15.0f, -10.0f),
				50, new Vector3f(-1.0f, 0.0f, 0.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.black)
			.shader(game.getResources().getShader("default"))
			.upload();
		
		//left
		rectangle4 = new ScreenElement();
		rectangle4.shape(Rectangle.sized(new Vector3f(0.0f, 15.0f, -5.0f),
				10, new Vector3f(0.0f, 0.0f, 1.0f),
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.blue)
			.shader(game.getResources().getShader("default"))
			.upload();
		
		//top
		rectangle5 = new ScreenElement();
		rectangle5.shape(Rectangle.sized(new Vector3f(25.0f, 30.0f, -5.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f), 
				10, new Vector3f(0.0f, 0.0f, -1.0f)))
			.color(Color.cyan)
			.shader(game.getResources().getShader("default"))
			.upload();
		
		rectangle6 = new ScreenElement();
		rectangle6.shape(Rectangle.sized(new Vector3f(25.0f, 0.0f, -5.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f),
				10, new Vector3f(0.0f, 0.0f, 1.0f)))
			.color(Color.magenta)
			.shader(game.getResources().getShader("default"))
			.upload();

		;
		camera = new Camera(new Vector3f(), new Vector3f(game.getCameraConfig().xOffset(),
				game.getCameraConfig().yOffset(), game.getCameraConfig().zOffset()), game.window().getAspectRatio());
	}

	@Override
	public void update(float deltaTime, GameInstance game) {
		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT)) {
			camera.move(new Vector3f(50.0f * deltaTime, 0.0f, 0.0f));
		}
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT)) {
			camera.move(new Vector3f(-50.0f * deltaTime, 0.0f, 0.0f));
		}
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_UP)) {
			camera.move(new Vector3f(0.0f, 0.0f, -50.0f * deltaTime));
		}
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN)) {
			camera.move(new Vector3f(0.0f, 0.0f, 50.0f * deltaTime));
		}
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP)) {
			camera.move(new Vector3f(0.0f, 50.0f * deltaTime, 0.0f));
		}
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN)) {
			camera.move(new Vector3f(0.0f, -50.0f * deltaTime, 0.0f));
		}
		
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		rectangle1.draw(camera);
		rectangle2.draw(camera);
		rectangle3.draw(camera);
		rectangle4.draw(camera);
		rectangle5.draw(camera);
		rectangle6.draw(camera);
	}
}
