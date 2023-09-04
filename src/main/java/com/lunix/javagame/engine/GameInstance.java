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
	private final SceneManager scenes;
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
		this.scenes = new SceneManager();
		this.resources = resources;
		this.camera = new Camera(cameraConfig);
		this.objMapper = new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.pathsConfig = pathsConfig;
		currentInstance = this;
	}

	public void run() throws Exception {
		init();
		loop();
		clean();
	}

	private void init() throws Exception {
		logger.info("Initializing the game engine...");
		window.create(mouse, keyboard);
		resources.init();
		scenes.changeScene(GameSceneType.EDITOR);
	}

	private void loop() throws Exception {
		logger.info("Starting the game loop...");
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (window.isOpened()) {
			timer.tick();
			window.newFrame();

			Debugger.display(false, "Game FPS: {}", 1 / timer.getDeltaTime());
			Debugger.display(false, mouse);
			Debugger.display(false, keyboard);
			Debugger.display(false, "Time elapsed: {}", timer.getElapsedTime());
			Debugger.display(false, "Delta time: {}", timer.getDeltaTime());

			scenes.update(timer.getDeltaTime());
			window.update(timer.getDeltaTime(), scenes.current());
			window.render();
			mouse.reset();
		}

		scenes.current().save();
	}

	private void clean() {
		logger.info("Clean before close...");
		window.destroy();
	}

	public MouseListener mouse() {
		return mouse;
	}

	public KeyboardListener keyboard() {
		return keyboard;
	}

	public SceneManager scenes() {
		return scenes;
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
}
