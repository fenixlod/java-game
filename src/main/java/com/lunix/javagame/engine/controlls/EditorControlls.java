package com.lunix.javagame.engine.controlls;

import static org.lwjgl.glfw.GLFW.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import com.lunix.javagame.configs.EditorConfigs;
import com.lunix.javagame.engine.Camera;
import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Prefabs;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.MouseDragging;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.EventType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;
import com.lunix.javagame.engine.observers.Observer;

public class EditorControlls implements Observer {
	private static final Logger logger = LogManager.getLogger(EditorControlls.class);
	private EditorConfigs editorConfig;
	private Camera controlledCamera;
	private GameInstance game;
	private Vector3f clickOrigin;
	private float drageDebounce = 0.032f;
	private float dragSensitivity = 3f;
	private float scrollSensitivity = 0.05f;
	private float resetSpeed = 3;
	private float lerpTime = 0f;
	private boolean reset;
	private Scene currentScene;
	private GameObject holdingObject;

	public EditorControlls() {
		clickOrigin = new Vector3f();
		EventSystem.addObserver(this);
	}

	public void init() {
		game = GameInstance.get();
		editorConfig = game.editorConfig();
	}

	public void start(Camera cameraToControll, Scene currentScene) {
		this.controlledCamera = cameraToControll;
		this.currentScene = currentScene;
		controlledCamera.position(new Vector3f());
	}

	public void update(float deltaTime) {
		if (game.mouse().dragging() && game.mouse().isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
			if (drageDebounce > 0) {
				clickOrigin = game.mouse().positionInWorldProjected();
				drageDebounce -= deltaTime;
			} else {
				Vector3f currentPos = game.mouse().positionInWorldProjected();
				Vector3f change = currentPos.sub(clickOrigin, new Vector3f());
				controlledCamera.move(change.mul(-deltaTime).mul(dragSensitivity));
				clickOrigin.lerp(currentPos, deltaTime);
			}
		} else {
			if (drageDebounce < 0) {
				drageDebounce = 0.032f;
			}
		}

		if (GameInstance.get().mouse().scroll().y != 0) {
			float addValue = (float) Math.pow(Math.abs(game.mouse().scroll().y * scrollSensitivity),
					controlledCamera.zoomFactor());

			if (addValue > 0.15)
				addValue = 0.15f;

			addValue *= Math.signum(game.mouse().scroll().y);
			game.camera().changeZoom(addValue);
		}
		
		if(game.keyboard().isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
			reset = true;
		}
		
		if(reset) {
			Vector3f newPos = controlledCamera.position().lerp(new Vector3f(), deltaTime * lerpTime * resetSpeed,
					new Vector3f());
			controlledCamera
					.zoomFactor(controlledCamera.zoomFactor() + ((1f - controlledCamera.zoomFactor()) * deltaTime));
			lerpTime += 0.5f * deltaTime;
			controlledCamera.position(newPos);
			
			if (Math.abs(controlledCamera.position().x) <= 5 && Math.abs(controlledCamera.position().y) <= 5) {
				controlledCamera.position(new Vector3f());
				reset = false;
				lerpTime = 0f;
				controlledCamera.zoomFactor(1f);
			}
		}
	}

	public void placeGround(String spriteName) throws Exception {
		if (holdingObject != null) {
			holdingObject.destroy();
			holdingObject = null;
		}

		GameObject groundTile = Prefabs.groundTile(new Vector3f(0f, 0f, 0f), editorConfig.gridSize(),
				editorConfig.gridSize(), spriteName);
		// Attach the ground object to the mouse cursor
		groundTile.addComponent(new MouseDragging().pickup());
		groundTile.getComponent(SpriteRenderer.class).color().a(0.5f);
		currentScene.addGameObject(groundTile);
		holdingObject = groundTile;
	}

	public void placePlayer() throws Exception {
		if (holdingObject != null) {
			holdingObject.destroy();
			holdingObject = null;
		}

		GameObject player = Prefabs.player(new Vector3f(0f, 0f, 0f));
		// Attach the ground object to the mouse cursor
		player.addComponent(new MouseDragging().pickup());
		player.getComponent(SpriteRenderer.class).color().a(0.5f);
		currentScene.addGameObject(player);
		holdingObject = player;
	}

	@Override
	public void onNotify(Event e) {
		if (e.type() != EventType.OBJECT_PLACED)
			return;

		try {
			holdingObject = null;
			if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT_SHIFT)
					|| game.keyboard().isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
				GameObject copy = game.load(game.save(e.target()), GameObject.class);
				copy.regenerateId();
				copy.getComponent(MouseDragging.class).pickup();
				currentScene.addGameObject(copy);
				holdingObject = copy;
			}
		} catch (Exception e1) {
			logger.error("Unable to copy object", e1);
		}
	}
}
