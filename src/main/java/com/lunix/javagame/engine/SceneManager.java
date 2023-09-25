package com.lunix.javagame.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;
import com.lunix.javagame.engine.observers.Observer;
import com.lunix.javagame.engine.scenes.LevelEditorScene;
import com.lunix.javagame.engine.scenes.MainMenuScene;
import com.lunix.javagame.engine.scenes.TestScene;
import com.lunix.javagame.engine.scenes.WorldScene;

public class SceneManager implements Observer {
	private static final Logger logger = LogManager.getLogger(GameWindow.class);
	private Scene currentScene;
	private GameSceneType currentSceneType;
	private Map<GameSceneType, Scene> scenes;

	public SceneManager() {
		scenes = new HashMap<>();
		EventSystem.addObserver(this);
	}

	/**
	 * Change to scene scene. If the scene is not yet created it will be created.
	 * 
	 * @param newSceneType
	 * @throws Exception
	 */
	public void changeScene(GameSceneType newSceneType) throws Exception {
		logger.info("Changing scene to: " + newSceneType.toString());
		Scene newScene = scenes.get(newSceneType);

		if (newScene == null) {
			newScene = createNewScene(newSceneType);
			newScene.init(true);
			newScene.start();
			scenes.put(newSceneType, newScene);
		} else {
			newScene.resume();
		}

		if (currentScene != null)
			currentScene.stop();

		currentScene = newScene;
		currentSceneType = newSceneType;
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
			case EDITOR -> new LevelEditorScene();
			default -> throw new IllegalStateException("Unknown scene with type: " + newSceneType.toString());
		};
	}

	/**
	 * Update the current scene
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public void update(float deltaTime) throws Exception {
		currentScene.update(deltaTime, GameSceneType.EDITOR != currentSceneType);
	}

	public Scene currentScene() {
		return currentScene;
	}

	/**
	 * Render the current scene
	 * 
	 * @param deltaTime
	 * @throws Exception
	 */
	public void render() throws Exception {
		currentScene.render();
	}

	/**
	 * Copy all game objects from scene to scene
	 * 
	 * @param from
	 * @param to
	 * @throws Exception
	 */
	public void copyObjects(GameSceneType from, GameSceneType to) throws Exception {
		logger.info("Copy all game ofjects from: {} -> {}", from, to);
		Scene fromScene = scenes.get(from);
		Scene toScene = scenes.get(to);
		toScene.destroy();
		toScene.init(false);
		toScene.deserialize(fromScene.serialize());
		toScene.start();
	}

	@Override
	public void onNotify(Event e) {
		switch (e.type()) {
		case GAME_START_PLAY:
			try {
				logger.info("Start playing the game...");
				changeScene(GameSceneType.WORLD);
				// The proper way to sync between the 2 scenes will be save/load
				// Now we do not have to make multiple saves so this variant is better
				copyObjects(GameSceneType.EDITOR, GameSceneType.WORLD);
			} catch (Exception e1) {
				logger.error("Unable to start the game", e1);
			}
			break;
		case GAME_END_PLAY:
			try {
				logger.info("Stop playing the game...");
				changeScene(GameSceneType.EDITOR);
			} catch (Exception e1) {
				logger.error("Unable to stop the game", e1);
			}
			break;
		case LOAD_LEVEL:
			break;
		case SAVE_LEVEL:
			try {
				currentScene.save();
			} catch (IOException e1) {
				logger.error("Unable to save lavel", e1);
			}
			break;
		case USER_EVENT:
			break;
		default:
			break;
		}
	}
}
