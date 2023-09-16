package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2i;

import com.lunix.javagame.engine.Editor;
import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.SpriteRenderer;

import imgui.ImGui;

public class ObjectInspector {
	private GameObject inspectedObject;
	private GameInstance game;

	public ObjectInspector() {
		this.game = GameInstance.get();
	}

	public void show() {
		if (inspectedObject == null)
			return;

		ImGui.begin("Object properties");
		ImGui.text("Name: %s, ID: %d".formatted(inspectedObject.name(), inspectedObject.id()));
		Editor.editObject(inspectedObject);
		// currentObject.ui();
		ImGui.end();
	}

	public void update(float deltaTime, Scene currentScene) {
		if (GameInstance.get().mouse().isButtonClicked(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector2i pos = GameInstance.get().mouse().positionInViewPort();
			long pickedObjID = game.window().pickObject(pos);

			if (inspectedObject != null) {
				inspectedObject.outlined(false);
				inspectedObject.getComponent(SpriteRenderer.class).isChanged(true);
			}

			inspectedObject = currentScene.getGameObject(pickedObjID);

			if (inspectedObject != null) {
				inspectedObject.outlined(true);
				inspectedObject.getComponent(SpriteRenderer.class).isChanged(true);
			}
		}
	}
}
