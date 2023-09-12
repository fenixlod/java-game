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
import com.lunix.javagame.configs.EditorConfigs;
import com.lunix.javagame.configs.PathsConfigs;
import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.graphic.FrameBuffer;
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
	private FrameBuffer frameBuffer;

	public GameInstance(WindowConfigs windowConfigs, ResourcePool resources, CameraConfigs cameraConfig,
			PathsConfigs pathsConfig, EditorConfigs editorConfig) {
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
		this.window.create(this.mouse, this.keyboard);
		this.frameBuffer = new FrameBuffer((int) window.size()[0], (int) window.size()[1]);
		this.resources.init();
		this.sceneManager.changeScene(GameSceneType.EDITOR);
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
		while (this.window.isOpened()) {
			this.timer.tick();
			this.frameBuffer.bind();
			this.window.newFrame();
			Debugger.beginFrame();
			Debugger.display(false, "Game FPS: {}", 1 / this.timer.deltaTime());
			Debugger.display(false, this.mouse);
			Debugger.display(false, this.keyboard);
			Debugger.display(false, "Time elapsed: {}", this.timer.elapsedTime());
			Debugger.display(false, "Delta time: {}", this.timer.deltaTime());
			Debugger.draw();
			this.sceneManager.update(this.timer.deltaTime());
			this.frameBuffer.unbind();
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

	public static long getNextId() {
		return nextObjectId++;
	}

	public static void nextId(long maxId) {
		nextObjectId = maxId + 1;
	}

	public EditorConfigs editorConfig() {
		return editorConfig;
	}

	public FrameBuffer frameBuffer() {
		return this.frameBuffer;
	}
}
