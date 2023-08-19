package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;

public class TestScene extends Scene {
	float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;

	@Override
	public void init(GameInstance game) {
		super.init(game);
		game.window().setColor(r, g, b, a);
	}

	@Override
	public void update(float deltaTime, GameInstance game) {
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		if (game.keyboard().isKeyPressed(GLFW_KEY_SPACE)) {
			r = Math.max(r - deltaTime / 3, 0);
			g = Math.max(g - deltaTime / 3, 0);
			b = Math.max(b - deltaTime / 3, 0);
			game.window().setColor(r, g, b, a);
		}

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}
	}
}
