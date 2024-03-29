package com.lunix.javagame.engine;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.configs.EditorConfigs;
import com.lunix.javagame.configs.PathsConfigs;
import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.GameSceneMode;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.scenes.SceneExecutor;
import com.lunix.javagame.engine.util.Debugger;
import com.lunix.javagame.engine.util.GameTime;

@Component
public class GameInstance {
	private static final Logger logger = LogManager.getLogger(GameInstance.class);
	private final MouseListener mouse;
	private final KeyboardListener keyboard;
	private final GameTime timer;
	private final SceneManager sceneManager;
	private final GameWindow window;
	private final ResourcePool resources;
	private final Camera camera;
	private static GameInstance currentInstance;
	private final ObjectMapper objMapper;
	private final PathsConfigs pathsConfig;
	private final EditorConfigs editorConfig;
	private static long nextObjectId = 0;

	public GameInstance(WindowConfigs windowConfigs, ResourcePool resources, CameraConfigs cameraConfig,
			PathsConfigs pathsConfig, EditorConfigs editorConfig) {
		window = new GameWindow(windowConfigs);
		mouse = new MouseListener();
		keyboard = new KeyboardListener();
		timer = new GameTime();
		sceneManager = new SceneManager();
		this.resources = resources;
		camera = new Camera(cameraConfig);
		objMapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.pathsConfig = pathsConfig;
		this.editorConfig = editorConfig;
		currentInstance = this;
	}

	/**
	 * Start the game.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		init();
		loop();
		destroy();
	}

	/**
	 * Initialize the game. Load resources, create scenes and objects.
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		logger.info("Initializing the game engine...");
		window.create(mouse, keyboard);
		resources.init();
		sceneManager.changeScene(GameSceneType.WORLD, GameSceneMode.EDIT, Optional.of("world.json"));
		Debugger.init();
		ResourcePool.loadResources(ShaderType.DEBUG);
	}

	/**
	 * Start the main game loop
	 * 
	 * @throws Exception
	 */
	private void loop() throws Exception {
		logger.info("Starting the game loop...");
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (window.isOpened()) {
			timer.tick();
			SceneExecutor currentSceneExecutor = sceneManager.currentExecutor();
			// Render pass 1. Create the picking texture
			window.createPickingTexture(currentSceneExecutor.currentScene());
			// Render pass 2. Render the actual game
			window.newFrame(currentSceneExecutor);
			Debugger.beginFrame();
			Debugger.infoInTitle(true, this);
			Debugger.display(false, keyboard);
			Debugger.display(false, "Time elapsed: {}", timer.elapsedTime());
			Debugger.display(false, "Delta time: {}", timer.deltaTime());
			Debugger.draw();
			sceneManager.update(timer.deltaTime());
			window.update(currentSceneExecutor);
			window.render();
			mouse.reset();
		}
	}

	/**
	 * Destroy the game. Free all allocated resources.
	 */
	private void destroy() {
		logger.info("Clean before close...");
		window.destroy();
	}

	public MouseListener mouse() {
		return mouse;
	}

	public KeyboardListener keyboard() {
		return keyboard;
	}

	public Camera camera() {
		return camera;
	}

	public static GameInstance get() {
		return currentInstance;
	}

	public GameWindow window() {
		return window;
	}

	public String save(Object obj) throws IOException {
		return objMapper.writeValueAsString(obj);
	}

	public <T> T load(String value, Class<T> classType) throws IOException {
		return objMapper.readValue(value, classType);
	}

	public PathsConfigs pathsConfig() {
		return pathsConfig;
	}

	public static long getNextId() {
		return nextObjectId++;
	}

	public static void nextId(long maxId) {
		nextObjectId = maxId + 1;
	}

	public EditorConfigs editorConfig() {
		return editorConfig;
	}

	public GameTime timer() {
		return timer;
	}

	public SceneManager sceneManager() {
		return sceneManager;
	}
}
