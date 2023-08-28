package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.util.GameTime;

@Component
public class GameInstance {
	private static final Logger logger = LogManager.getLogger(GameInstance.class);
	private final MouseListener mouse;
	private final KeyboardListener keyboard;
	private final GameTime timer;
	private final SceneManager scenes;
	private final GameWindow window;
	private final Resources resources;
	private final Camera camera;
	private static GameInstance currentInstance;

	public GameInstance(WindowConfigs windowConfigs, Resources resources, CameraConfigs cameraConfig) {
		this.window = new GameWindow(windowConfigs);
		this.mouse = new MouseListener();
		this.keyboard = new KeyboardListener();
		this.timer = new GameTime();
		this.scenes = new SceneManager();
		this.resources = resources;
		this.camera = new Camera(cameraConfig);
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
		scenes.changeScene(GameSceneType.TEST);
	}

	private void loop() {
		logger.info("Starting the game loop...");
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (window.isOpened()) {
			timer.tick();
			window.refresh();
			// TODO: display the mouse state in the window when in dev mode
			// logger.info("Game FPS: {}", 1 / timer.getDeltaTime());
			scenes.update(timer.getDeltaTime());
			window.draw();
			mouse.reset();
		}
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

}
