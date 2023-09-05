package com.lunix.javagame.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.lunix.javagame.engine.graphic.Renderer;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);
	private boolean active = false;
	private final List<GameObject> objects;
	private final Renderer renderer;
	protected final GameInstance game;
	protected boolean loaded;

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

	public void ui() {
	}

	public void load() throws Exception {
		if (true) // TODO: remove
			return;

		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), "editor.json");

		if (!levelsFile.toFile().exists())
			return;

		String json = new String(Files.readAllBytes(levelsFile));

		if (StringUtils.hasText(json)) {
			GameObject[] data = game.load(json, GameObject[].class);
			for (GameObject obj : data) {
				addGameObject(obj);
			}
			this.loaded = true;
			sceneLoaded(data);
		}
	}

	protected void sceneLoaded(GameObject[] loadedData) {
	}

	public void save() throws IOException {
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), "editor.json");

		try (FileWriter writer = new FileWriter(levelsFile.toFile())) {
			writer.write(game.save(this.objects));
		}
	}
}
