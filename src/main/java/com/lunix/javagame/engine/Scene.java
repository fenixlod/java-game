package com.lunix.javagame.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.lunix.javagame.engine.enums.SceneEventType;
import com.lunix.javagame.engine.graphic.Renderer;

public abstract class Scene {
	protected static final Logger logger = LogManager.getLogger(Scene.class);
	private boolean active = false;
	private final List<GameObject> objects;
	private final List<GameObject> pendingObjects;
	private final Renderer renderer;
	protected final GameInstance game;
	protected boolean sceneLoaded;
	protected String fileName;

	protected Scene() {
		active = false;
		objects = new ArrayList<>();
		pendingObjects = new ArrayList<>();
		renderer = new Renderer();
		game = GameInstance.get();
	}

	/**
	 * Initialize the scene.
	 * 
	 * @throws Exception
	 */
	public void init(Optional<String> loadFile) throws Exception {
		// TODO: Display loading screen?
		logger.info("Start initializing scene: {}", this.getClass().getSimpleName());
		objects.clear();
		pendingObjects.clear();
		renderer.destroy();
		sceneLoaded = false;
		active = false;

		if (loadFile.isPresent())
			load(loadFile.get());
	}

	/**
	 * Load the scene from saved file.
	 * 
	 * @throws Exception
	 */
	private void load(String fileName) throws Exception {
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), fileName);

		if (!levelsFile.toFile().exists())
			return;

		String json = new String(Files.readAllBytes(levelsFile));

		if (StringUtils.hasText(json)) {
			deserialize(json);
			sceneLoaded = true;
		}
	}

	/**
	 * Start the scene. All game objects will be rendered and updated.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		objects.addAll(pendingObjects);
		pendingObjects.clear();
		for (GameObject obj : objects) {
			obj.start();
			renderer.add(obj);
			notify(SceneEventType.OBJECT_STARTED, obj);
		}

		active = true;
		// TODO: Hide loading screen?
	}

	/**
	 * Update the scene.
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public final void update(float deltaTime, boolean isPlaying) throws Exception {
		if (!active)
			return;

		for (GameObject go : pendingObjects) {
			insertGameObject(go);
		}

		pendingObjects.clear();

		scenePreUpdate(deltaTime, isPlaying);
		GameObject objectToRemove = null;
		for (GameObject obj : objects) {
			obj.update(deltaTime, isPlaying);
			if (obj.isDestriyed())
				objectToRemove = obj;
		}

		if (objectToRemove != null) {
			if (objects.remove(objectToRemove)) {
				renderer.remove(objectToRemove);
				notify(SceneEventType.OBJECT_REMOVED, objectToRemove);
			}
		}
		scenePostUpdate(deltaTime, isPlaying);
	}

	protected void scenePreUpdate(float deltaTime, boolean isPlaying) throws Exception {
	}

	protected void scenePostUpdate(float deltaTime, boolean isPlaying) throws Exception {
	}

	/**
	 * Destroy the scene.
	 */
	public void destroy() {
		logger.info("Destroyng scene : {}", this.getClass().getSimpleName());
		objects.forEach(GameObject::destroy);
		objects.clear();
		pendingObjects.clear();
		renderer.destroy();
		active = false;
	}

	/**
	 * Add game object to the scene.
	 * 
	 * @param object
	 * @throws Exception
	 */
	private void insertGameObject(GameObject object) throws Exception {
		objects.add(object);

		if (active) {
			object.start();
			renderer.add(object);
			notify(SceneEventType.OBJECT_ADDED, object);
		}
	}

	/**
	 * Add game object to the scene pending objects list. These objects will be
	 * inserted at the tart of the next update
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void addGameObject(GameObject object) throws Exception {
		pendingObjects.add(object);
	}

	private void deserialize(String json) throws Exception {
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
	}

	/**
	 * Save the scene to file.
	 * 
	 * @throws IOException
	 */
	public void save(Optional<String> fileName) throws IOException {
		Path levelsFile = Paths.get(game.pathsConfig().save().get("levels"), fileName.orElse("world.json"));
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
	private String serialize() throws IOException {
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
	 * Display scene specific UI.
	 */
	public void ui() {
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

	protected void notify(SceneEventType eventType, GameObject object) {
	}

	public String fileName() {
		return fileName;
	}
}
