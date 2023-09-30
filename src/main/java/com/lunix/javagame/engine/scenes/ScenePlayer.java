package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.enums.EventType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;
import com.lunix.javagame.engine.util.Debugger;

public class ScenePlayer extends SceneExecutor {
	private GameObject playerObject;

	public ScenePlayer() {
		super();
	}

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void start(Scene scene) throws Exception {
		super.start(scene);
		for (GameObject obj : currentScene.objects()) {
			if (obj.name().equals("Player")) {
				playerObject = obj;
				return;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void update(float deltaTime) throws Exception {
		Vector3f offset = new Vector3f();

		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT))
			offset.x += 5f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT))
			offset.x -= 5f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_UP))
			offset.y += 5f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN))
			offset.y -= 5f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP))
			offset.z += 5f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN))
			offset.z -= 5f * deltaTime;

		float zoomChange = (float) game.mouse().scroll().y * 0.1f;
		if (zoomChange != 0f)
			game.camera().changeZoom(zoomChange);

		Debugger.display(false, "X={}, Y={}, Z={}", game.camera().position().x, game.camera().position().y,
				game.camera().position().z);

		playerObject.transform().move(offset);

		currentScene.update(deltaTime, true);
		currentScene.render();

		game.camera().position(playerObject.transform().positionCopy());
		// Go to level editor when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			logger.info("Escape button pressed. Exith the world scene");
			EventSystem.notify(new Event(EventType.GAME_END_PLAY));
		}
	}

	@Override
	public void newFrame() {
		super.newFrame();
		game.window().clearColor(1f, 1f, 1f, 1f);
	}

	@Override
	public void ui() {
		super.ui();
	}

	@Override
	public void endFrame() {
		super.endFrame();
	}
}
