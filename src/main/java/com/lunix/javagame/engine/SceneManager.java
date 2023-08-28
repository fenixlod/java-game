package com.lunix.javagame.engine;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.scenes.MainMenuScene;
import com.lunix.javagame.engine.scenes.TestScene;
import com.lunix.javagame.engine.scenes.WorldScene;

public class SceneManager {
	private static final Logger logger = LogManager.getLogger(GameWindow.class);

	private Scene currentScene;
	private Map<GameSceneType, Scene> scenes = new HashMap<>();


	public void changeScene(GameSceneType newSceneType) {
		logger.info("Changing scene to: " + newSceneType.toString());
		// TODO: Display loading screen
		Scene newScene = scenes.get(newSceneType);

		if (newScene == null) {
			newScene = createNewScene(newSceneType);
			newScene.init();
			scenes.put(newSceneType, newScene);
		}

		currentScene = newScene;
		// TODO: Remove loading screen
		currentScene.start();
	}

	private Scene createNewScene(GameSceneType newSceneType) {
		return switch (newSceneType) {
		case MAIN_MENU -> new MainMenuScene();
		case WORLD -> new WorldScene();
		case TEST -> new TestScene();
		default -> throw new IllegalStateException("Unknown scene with index: " + newSceneType.toString());
		};
	}

	public void update(float deltaTime) {
		currentScene.update(deltaTime);
	}
}
