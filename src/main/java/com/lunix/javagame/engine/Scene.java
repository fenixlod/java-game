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

	protected Scene() {
		this.active = false;
		this.objects = new ArrayList<>();
		this.renderer = new Renderer();
	}

	public void init() {
		logger.info("Start initializing scene");
	}

	public void addGameObject(GameObject object) {
		objects.add(object);

		if (active) {
			object.start();
			this.renderer.add(object);
		}
	}

	public void start() {
		for (GameObject obj : objects) {
			obj.start();
			this.renderer.add(obj);
		}

		active = true;
	}

	public void update(float deltaTime) {
		for (GameObject obj : objects) {
			obj.update(deltaTime);
		}
		this.renderer.render();
	}
}
