package com.lunix.javagame.engine.scenes;

import java.util.Optional;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Scene;

public class MainMenuScene extends Scene {

	public MainMenuScene() {
		super();
		fileName = "menu.json";
	}

	@Override
	public void init(Optional<String> loadFile) throws Exception {
		super.init(loadFile);
		game.camera().reset();
		Vector3f offsets = new Vector3f(game.camera().offsets());
		offsets.x = 0;
		offsets.y = 0;
		game.camera().offsets(offsets);
	}

	@Override
	protected void scenePreUpdate(float deltaTime, boolean isPlaying) throws Exception {
	}
}
