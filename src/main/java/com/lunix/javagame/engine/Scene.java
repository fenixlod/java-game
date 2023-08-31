package com.lunix.javagame.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.graphic.Renderer;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);
	private boolean active = false;
	private final List<GameObject> objects;
	private final Renderer renderer;
	protected final GameInstance game;

	protected Scene() {
		this.active = false;
		this.objects = new ArrayList<>();
		this.renderer = new Renderer();
		this.game = GameInstance.get();
	}

	public void init() throws Exception {
		logger.info("Start initializing scene");
		// TODO: Display loading screen?
	}

	public void start() throws Exception {
		// TODO: Hide loading screen?
		for (GameObject obj : objects) {
			obj.start();
			this.renderer.add(obj);
		}

		active = true;
	}

	public void stop() {
		active = false;
	}

	public void update(float deltaTime) throws Exception {
		if (!active)
			return;

		for (GameObject obj : objects) {
			obj.update(deltaTime);
		}
		this.renderer.render();
	}

	public void addGameObject(GameObject object) throws Exception {
		objects.add(object);

		if (active) {
			object.start();
			this.renderer.add(object);
		}
	}
}
