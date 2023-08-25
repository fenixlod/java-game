package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.util.GameTime;

@Component
public class GameInstance {
	private static final Logger logger = LogManager.getLogger(GameInstance.class);
	private final MouseListener mouse;
	private final KeyboardListener keyboard;
	private final GameTime timer;
	private final SceneManager scenes;
	private final Display display;

	public GameInstance(Display display) {
		this.display = display;
		this.mouse = new MouseListener();
		this.keyboard = new KeyboardListener();
		this.timer = new GameTime();
		this.scenes = new SceneManager(this);
	}

	public void run() throws Exception {
		init();
		loop();
		clean();
	}

	private void init() throws Exception {
		logger.info("Initializing the game engine...");
		display.init(mouse, keyboard);
		scenes.changeScene(GameSceneType.TEST);
	}

	private void loop() {
		logger.info("Starting the game loop...");
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (display.isWindowOpen()) {
			timer.tick();
			display.refresh();
			// TODO: display the mouse state in the window when in dev mode
			// logger.info("Game FPS: {}", 1 / timer.getDeltaTime());
			scenes.update(timer.getDeltaTime());
			display.draw();
			mouse.reset();
		}
	}

	private void clean() {
		logger.info("Clean before close...");
		display.clean();
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
		return display.camera();
	}

	public Display display() {
		return display;
	}
}
