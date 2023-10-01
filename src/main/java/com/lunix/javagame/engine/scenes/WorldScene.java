package com.lunix.javagame.engine.scenes;

import java.util.Optional;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.SceneEventType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.physics.Physics;

public class WorldScene extends Scene {
	private Physics physics;

	public WorldScene() {
		super();
		fileName = "world.json";
		physics = new Physics();
	}

	@Override
	public void init(Optional<String> loadFile) throws Exception {
		super.init(loadFile);
		// physics.init();
		game.camera().reset();
		// Probably collect all resources from objects list and load them
		ResourcePool.loadResources(ShaderType.DEFAULT, TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE,
				TextureType.TILE_BRICK);

		if (sceneLoaded)
			return;

		GameObject playerObject = new GameObject("Player")
			.addComponent(
						new SpriteRenderer(4, 5)
				.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
			);
		playerObject.addComponent(new Animation(ResourcePool.getSprites(TextureType.PLAYER_IDLE), 0.3f));
		addGameObject(playerObject);
			

		GameObject enemy = new GameObject("Enemy", new Vector3f(-5f, 5f, 0f))
			.addComponent(
						new SpriteRenderer(2, 4)
					.color(Color.red())
			);
		addGameObject(enemy);
	}

	@Override
	public void start() throws Exception {
		super.start();
	}

	@Override
	protected void scenePreUpdate(float deltaTime, boolean isPlaying) throws Exception {
		physics.update(deltaTime, isPlaying);
	}

	@Override
	public void newFrame() {
		super.newFrame();
		game.window().clearColor(1f, 1f, 1f, 1f);
	}

	@Override
	protected void notify(SceneEventType eventType, GameObject object) {
		switch(eventType) {
			case OBJECT_ADDED:
				physics.addGameObject(object);
				break;
			case OBJECT_STARTED:
				physics.addGameObject(object);
				break;
			case OBJECT_REMOVED:
				physics.removeGameObject(object);
				break;
		}
	}
}
