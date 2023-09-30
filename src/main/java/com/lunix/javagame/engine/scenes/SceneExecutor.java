package com.lunix.javagame.engine.scenes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;

public abstract class SceneExecutor {
	protected static final Logger logger = LogManager.getLogger(SceneExecutor.class);
	private boolean isInitialized;
	protected Scene currentScene;
	protected GameInstance game;

	public void init() throws Exception {
		game = GameInstance.get();
		isInitialized = true;
	}

	public void start(Scene scene) throws Exception {
		currentScene = scene;
		currentScene.start();
	}

	public void destroy() {
		currentScene.destroy();
	}

	public abstract void update(float deltaTime) throws Exception;

	public void newFrame() {
	}

	public void ui() {
	}

	public void endFrame() {
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public Scene currentScene() {
		return currentScene;
	}
}
