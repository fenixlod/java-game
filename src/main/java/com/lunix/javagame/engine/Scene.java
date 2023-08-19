package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);

	public void init(GameInstance game) {
		logger.info("Start initializing scene");
	}

	public abstract void update(float deltaTime, GameInstance game);
}
