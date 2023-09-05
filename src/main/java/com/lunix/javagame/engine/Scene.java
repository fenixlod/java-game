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

import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.graphic.Renderer;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);
	private GameSceneType type;
	private boolean active = false;
	private final List<GameObject> objects;
	private final Renderer renderer;
	protected final GameInstance game;
	protected boolean loaded;

	protected Scene(GameSceneType type) {
		this.active = false;
		this.objects = new ArrayList<>();
		this.renderer = new Renderer();
		this.game = GameInstance.get();
		this.type = type;
	}

	/**
	 * Initialize the scene.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		logger.info("Start initializing scene");
		// TODO: Display loading screen?
	}

	/**
	 * Start the scene. All game objects will be rendered and updated.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		// TODO: Hide loading screen?
		for (GameObject obj : this.objects) {
			obj.start();
			this.renderer.add(obj);
		}

		this.active = true;
	}

	/**
	 * Stop the scene. This scene will no longer receive updates.
	 */
	public void stop() {
		this.active = false;
	}

	/**
	 * Update the scene.
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public void update(float deltaTime) throws Exception {
		if (!active)
			return;

		for (GameObject obj : objects) {
			obj.update(deltaTime);
		}
		this.renderer.render();
	}

	/**
	 * Add game object to the scene.
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void addGameObject(GameObject object) throws Exception {
		objects.add(object);

		if (active) {
			object.start();
			this.renderer.add(object);
		}
	}

	public void ui() {
	}

	/**
	 * Load the scene from saved file.
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		if (true) // TODO: remove
			return;

		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), type.toString() + ".json");

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

	/**
	 * This function will be called when the scene is loaded from file.
	 * 
	 * @param loadedData
	 */
	protected void sceneLoaded(GameObject[] loadedData) {
	}

	/**
	 * Save the scene to file.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), type.toString() + ".json");

		try (FileWriter writer = new FileWriter(levelsFile.toFile())) {
			writer.write(game.save(this.objects));
		}
	}
}
