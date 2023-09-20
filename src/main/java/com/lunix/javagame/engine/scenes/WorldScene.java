package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.util.Debugger;

public class WorldScene extends Scene {
	private GameObject playerObject;
	protected GameObject currentObject;

	public WorldScene() {
		super();
	}

	@Override
	public void init() throws Exception {
		super.init();
		game.camera().setOrthoProjection();
		game.camera().position(new Vector3f());
		ResourcePool.loadResources(ShaderType.DEFAULT, TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE,
				TextureType.TILE_BRICK);
	}

	@Override
	public void update(float deltaTime) throws Exception {
		Vector3f offset = new Vector3f();

		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT))
			offset.x += 50f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT))
			offset.x -= 50f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_UP))
			offset.y += 50f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN))
			offset.y -= 50f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP))
			offset.z += 50f * deltaTime;

		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN))
			offset.z -= 50f * deltaTime;

		float zoomChange = (float) game.mouse().scroll().y * 0.1f;
		if (zoomChange != 0f)
			game.camera().changeZoom(zoomChange);

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		Debugger.display(false, "X={}, Y={}, Z={}", game.camera().position().x, game.camera().position().y,
				game.camera().position().z);

		this.playerObject.move(offset);
		game.camera().position(playerObject.transform().position());

		Vector3f worldPosition = game.mouse().positionInWorldProjected();
		System.out.println("Current X=" + worldPosition.x + " Y=" + worldPosition.y + " Z=" + worldPosition.z);
		super.update(deltaTime);
	}

	@Override
	protected void sceneLoaded(GameObject[] loadedData) {
		for (GameObject obj : loadedData) {
			if (obj.name().equals("Player")) {
				playerObject = obj;
				currentObject = obj;
				return;
			}
		}
	}

	@Override
	public void newFrame() {
		super.newFrame();
		game.window().clearColor(1f, 1f, 1f, 1f);
	}
}
