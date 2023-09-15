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

	/**
	 * Initialize the scene.
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		logger.info("Start initializing scene");
		// TODO: Display loading screen?
		load();
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
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), "world.json");

		if (!levelsFile.toFile().exists())
			return;

		String json = new String(Files.readAllBytes(levelsFile));

		if (StringUtils.hasText(json)) {
			long maxFoundId = -1;
			GameObject[] data = game.load(json, GameObject[].class);
			for (GameObject obj : data) {
				addGameObject(obj);
				if (obj.id() > maxFoundId)
					maxFoundId = obj.id();

				long maxComponentId = obj.components().stream().mapToLong(Component::id).max().getAsLong();
				if (maxComponentId > maxFoundId)
					maxFoundId = maxComponentId;
			}
			this.loaded = true;
			sceneLoaded(data);
			GameInstance.nextId(maxFoundId);
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
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), "world.json");

		try (FileWriter writer = new FileWriter(levelsFile.toFile())) {
			writer.write(game.save(this.objects));
		}
	}

	/**
	 * This function will be called when new frame begin.
	 * 
	 */
	public void newFrame() {
	}

	/**
	 * This function will be called when the frame ends.
	 * 
	 */
	public void endFrame() {
	}

	/**
	 * Render the scene.
	 * 
	 * @throws Exception
	 * 
	 */
	public void render() throws Exception {
		this.renderer.render();
	}

	/**
	 * Remove game object from the scene.
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void removeGameObject(GameObject object) throws Exception {
		if (objects.remove(object))
			this.renderer.remove(object);
	}

	/**
	 * Get game object by id.
	 * 
	 * @param objectID
	 * @throws Exception
	 */
	public GameObject getGameObject(long objectID) {
		return objects.stream().filter(o -> o.id() == objectID).findFirst().orElse(null);
	}
}
