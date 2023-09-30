package com.lunix.javagame.engine;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.enums.GameSceneMode;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;
import com.lunix.javagame.engine.observers.Observer;
import com.lunix.javagame.engine.scenes.MainMenuScene;
import com.lunix.javagame.engine.scenes.SceneEditor;
import com.lunix.javagame.engine.scenes.SceneExecutor;
import com.lunix.javagame.engine.scenes.ScenePlayer;
import com.lunix.javagame.engine.scenes.TestScene;
import com.lunix.javagame.engine.scenes.WorldScene;

public class SceneManager implements Observer {
	private static final Logger logger = LogManager.getLogger(SceneManager.class);
	private SceneExecutor currentSceneExecutor;
	private SceneEditor sceneEditor;
	private ScenePlayer scenePlayer;
	private GameSceneType currentSceneType;
	private GameSceneMode currentSceneMode;

	public SceneManager() {
		EventSystem.addObserver(this);
		sceneEditor = new SceneEditor();
		scenePlayer = new ScenePlayer();
	}

	/**
	 * Change to scene scene. If the scene is not yet created it will be created.
	 * 
	 * @param newSceneType
	 * @throws Exception
	 */
	public void changeScene(GameSceneType newSceneType, GameSceneMode sceneMode, Optional<String> fileName)
			throws Exception {
		logger.info("Changing scene to: {}", newSceneType.toString());
		
		if (currentSceneExecutor != null)
			currentSceneExecutor.destroy();

		currentSceneExecutor = getSceneExecutor(sceneMode);
		if (!currentSceneExecutor.isInitialized())
			currentSceneExecutor.init();

		Scene newScene = createNewScene(newSceneType);
		newScene.init(fileName);
		currentSceneExecutor.start(newScene);
		currentSceneType = newSceneType;
		currentSceneMode = sceneMode;
	}

	private SceneExecutor getSceneExecutor(GameSceneMode sceneMode) {
		return switch(sceneMode) {
			case EDIT -> sceneEditor;
			case PLAY -> scenePlayer;
			default -> throw new IllegalStateException("Unknown scene mode: " + sceneMode.toString());
		};
	}
	/**
	 * Create new scene of given type.
	 * 
	 * @param newSceneType
	 * @return
	 */
	private Scene createNewScene(GameSceneType newSceneType) {
		return switch (newSceneType) {
			case MAIN_MENU -> new MainMenuScene();
			case WORLD -> new WorldScene();
			case TEST -> new TestScene();
			default -> throw new IllegalStateException("Unknown scene with type: " + newSceneType.toString());
		};
	}

	private void playScene() throws Exception {
		Scene currentScene = currentSceneExecutor.currentScene();
		currentScene.save(Optional.of("tmp.json"));
		changeScene(currentSceneType, GameSceneMode.PLAY, Optional.of("tmp.json"));
	}

	private void editScene() throws Exception {
		changeScene(currentSceneType, GameSceneMode.EDIT, Optional.of("tmp.json"));
	}

	/**
	 * Update and render the current scene.
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public void update(float deltaTime) throws Exception {
		currentSceneExecutor.update(deltaTime);
	}

	public SceneExecutor currentExecutor() {
		return currentSceneExecutor;
	}

	@Override
	public void onNotify(Event e) {
		switch (e.type()) {
		case GAME_START_PLAY:
			try {
				logger.info("Start playing the scene {} ...", currentSceneType.toString());
				playScene();
			} catch (Exception e1) {
				logger.error("Unable to start the game", e1);
			}
			break;
		case GAME_END_PLAY:
			try {
				logger.info("Stop playing the scene {} ...", currentSceneType.toString());
				editScene();
			} catch (Exception e1) {
				logger.error("Unable to stop the game", e1);
			}
			break;
		case LOAD_LEVEL:
			try {
				changeScene(currentSceneType, currentSceneMode,
						Optional.of(currentSceneExecutor.currentScene().fileName()));
				logger.info("Load current level {} ...", currentSceneType.toString());
			} catch (Exception e1) {
				logger.error("Unable to load lavel", e1);
			}
			break;
		case SAVE_LEVEL:
			try {
				logger.info("Save current level {} ...", currentSceneType.toString());
				currentSceneExecutor.currentScene().save(Optional.empty());
			} catch (Exception e1) {
				logger.error("Unable to save lavel", e1);
			}
			break;
		case USER_EVENT:
			break;
		case SET_SCENE_WORLD:
			try {
				logger.info("Change current level to {} ...", GameSceneType.WORLD.toString());
				changeScene(GameSceneType.WORLD, currentSceneMode, Optional.of("world.json"));
			} catch (Exception e1) {
				logger.error("Unable to change lavel", e1);
			}
			break;
		case SET_SCENE_MAIN_MENU:
			try {
				logger.info("Change current level to {} ...", GameSceneType.MAIN_MENU.toString());
				changeScene(GameSceneType.MAIN_MENU, currentSceneMode, Optional.of("menu.json"));
			} catch (Exception e1) {
				logger.error("Unable to change lavel", e1);
			}
			break;
		case SET_SCENE_TEST:
			try {
				logger.info("Change current level to {} ...", GameSceneType.TEST.toString());
				changeScene(GameSceneType.TEST, currentSceneMode, Optional.of("test.json"));
			} catch (Exception e1) {
				logger.error("Unable to change lavel", e1);
			}
			break;
		default:
			break;
		}
	}

	public GameSceneType currentSceneType() {
		return currentSceneType;
	}
}
