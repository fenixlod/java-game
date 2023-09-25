package com.lunix.javagame.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.lunix.javagame.engine.graphic.Renderer;
import com.lunix.javagame.engine.physics.Physics;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);
	private boolean active = false;
	private final List<GameObject> objects;
	private final Renderer renderer;
	protected final GameInstance game;
	protected boolean loaded;
	private Physics physics;

	protected Scene() {
		active = false;
		objects = new ArrayList<>();
		renderer = new Renderer();
		game = GameInstance.get();
		physics = new Physics();
	}

	/**
	 * Initialize the scene.
	 * 
	 * @throws Exception
	 */
	public void init(boolean doLoad) throws Exception {
		logger.info("Start initializing scene: {}", this.getClass().getSimpleName());
		// TODO: Display loading screen?
		loaded = false;
		active = false;
		// physics.init();

		if (doLoad)
			load();
	}

	/**
	 * Start the scene. All game objects will be rendered and updated.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		// TODO: Hide loading screen?
		for (GameObject obj : objects) {
			obj.start();
			renderer.add(obj);
			physics.addGameObject(obj);
		}

		active = true;
	}

	/**
	 * Update the scene.
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public void update(float deltaTime, boolean isPlaying) throws Exception {
		if (!active)
			return;

		physics.update(deltaTime, isPlaying);
		GameObject objectToRemove = null;
		for (GameObject obj : objects) {
			obj.update(deltaTime, isPlaying);
			if (obj.isDestriyed())
				objectToRemove = obj;
		}

		if (objectToRemove != null) {
			if (objects.remove(objectToRemove)) {
				renderer.remove(objectToRemove);
				physics.removeGameObject(objectToRemove);
			}
		}
	}

	/**
	 * Stop the scene. This scene will no longer receive updates.
	 */
	public void stop() {
		active = false;
	}

	/**
	 * Resume the scene. This scene will start receiving updates again.
	 */
	public void resume() {
		active = true;
	}

	/**
	 * Destroy the scene.
	 */
	public void destroy() {
		logger.info("Destroyng scene : {}", this.getClass().getSimpleName());
		objects.forEach(GameObject::destroy);
		objects.clear();
		renderer.destroy();
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
			renderer.add(object);
			physics.addGameObject(object);
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
			deserialize(json);
			loaded = true;
		}
	}

	public void deserialize(String json) throws Exception {
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

		GameInstance.nextId(maxFoundId);
		sceneLoaded(data);
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
		logger.info("Save current level to: {}", levelsFile);
		try (FileWriter writer = new FileWriter(levelsFile.toFile())) {
			writer.write(serialize());
		}
	}

	/**
	 * Serialize this scene game objects.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String serialize() throws IOException {
		List<GameObject> filteredObjects = objects.stream()
				.filter(o -> !o.isTemporary())
				.collect(Collectors.toList());
		
		return game.save(filteredObjects);
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
		renderer.render();
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

	public List<GameObject> objects() {
		return objects;
	}

	public boolean isActive() {
		return active;
	}
}
