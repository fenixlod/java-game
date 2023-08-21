package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.Vertex;
import com.lunix.javagame.engine.graphic.objects.Rectangle;
import com.lunix.javagame.engine.graphic.objects.ScreenElement;
import com.lunix.javagame.engine.graphic.objects.Triangle;

public class TestScene extends Scene {
	private Shader defaultShader;
	private ScreenElement triangle, rectangle;

	@Override
	public void init(GameInstance game) {
		super.init(game);
		game.window().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		defaultShader = new Shader();
		defaultShader.compile();

		triangle = new ScreenElement();
		triangle.shape(
					new Triangle(
							new Vertex(0.75f, 0.75f, 0.0f)
								.color(Color.red), 
							new Vertex(0.0f, 0.75f, 0.0f), 
							new Vertex(0.75f, 0.0f, 0.0f)
					)
				)
				.color(Color.green)
				.shader(defaultShader);
		
		rectangle = new ScreenElement();
		rectangle.shape(
				new Rectangle(
						new Vertex(0.5f, -0.5f, 0.0f)
							.color(Color.blue),
						new Vertex(0.5f,  0.5f, 0.0f)
							.color(Color.green),
						new Vertex(-0.5f,  0.5f, 0.0f),
						new Vertex(-0.5f, -0.5f, 0.0f)
				)
			)
			.color(Color.red)
			.shader(defaultShader);
	}

	@Override
	public void update(float deltaTime, GameInstance game) {
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		rectangle.draw();
		triangle.draw();
	}
}
