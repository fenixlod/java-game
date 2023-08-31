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


	public void changeScene(GameSceneType newSceneType) throws Exception {
		logger.info("Changing scene to: " + newSceneType.toString());
		Scene newScene = scenes.get(newSceneType);

		if (newScene == null) {
			newScene = createNewScene(newSceneType);
			newScene.init();
			newScene.start();
			scenes.put(newSceneType, newScene);
		}

		if (currentScene != null)
			currentScene.stop();

		currentScene = newScene;
	}

	private Scene createNewScene(GameSceneType newSceneType) {
		return switch (newSceneType) {
			case MAIN_MENU -> new MainMenuScene();
			case WORLD -> new WorldScene();
			case TEST -> new TestScene();
			default -> throw new IllegalStateException("Unknown scene with index: " + newSceneType.toString());
		};
	}

	public void update(float deltaTime) throws Exception {
		currentScene.update(deltaTime);
	}
}
