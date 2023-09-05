package com.lunix.javagame.engine;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.configs.PathsConfigs;
import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.GameSceneType;
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

	public GameInstance(WindowConfigs windowConfigs, ResourcePool resources, CameraConfigs cameraConfig,
			PathsConfigs pathsConfig) {
		this.window = new GameWindow(windowConfigs);
		this.mouse = new MouseListener();
		this.keyboard = new KeyboardListener();
		this.timer = new GameTime();
		this.sceneManager = new SceneManager();
		this.resources = resources;
		this.camera = new Camera(cameraConfig);
		this.objMapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.pathsConfig = pathsConfig;
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
		this.window.create(this.mouse, this.keyboard);
		this.resources.init();
		this.sceneManager.changeScene(GameSceneType.EDITOR);
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
		while (this.window.isOpened()) {
			this.timer.tick();
			this.window.newFrame();

			Debugger.display(false, "Game FPS: {}", 1 / this.timer.deltaTime());
			Debugger.display(false, this.mouse);
			Debugger.display(false, this.keyboard);
			Debugger.display(false, "Time elapsed: {}", this.timer.elapsedTime());
			Debugger.display(false, "Delta time: {}", this.timer.deltaTime());

			this.sceneManager.update(this.timer.deltaTime());
			this.window.update(this.timer.deltaTime(), this.sceneManager.currentScene());
			this.window.render();
			this.mouse.reset();
		}

		this.sceneManager.currentScene().save();
	}

	/**
	 * Destroy the game. Free all allocated resources.
	 */
	private void destroy() {
		logger.info("Clean before close...");
		this.window.destroy();
	}

	public MouseListener mouse() {
		return this.mouse;
	}

	public KeyboardListener keyboard() {
		return this.keyboard;
	}

	public Camera camera() {
		return this.camera;
	}

	public static GameInstance get() {
		return currentInstance;
	}

	public GameWindow window() {
		return this.window;
	}

	public String save(Object obj) throws IOException {
		return this.objMapper.writeValueAsString(obj);
	}

	public <T> T load(String value, Class<T> classType) throws IOException {
		return this.objMapper.readValue(value, classType);
	}

	public PathsConfigs pathsConfig() {
		return this.pathsConfig;
	}
}
