package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2i;

import com.lunix.javagame.engine.Editor;
import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.TranslateGizmo;
import com.lunix.javagame.engine.enums.TextureType;

import imgui.ImGui;

public class ObjectInspector {
	private GameObject inspectedObject;
	private GameInstance game;
	private TranslateGizmo mover;

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
		// Add ui for gizmos?
		ImGui.end();
	}

	public void init(Scene scene) throws Exception {
		this.mover = new TranslateGizmo(ResourcePool.getSprite(TextureType.ARROW));
		this.mover.init(scene);
	}

	public void update(float deltaTime, Scene currentScene) {
		if (GameInstance.get().mouse().isButtonClicked(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector2i pos = GameInstance.get().mouse().positionInViewPort();
			long pickedObjID = game.window().pickObject(pos);
			GameObject pickedObject = currentScene.getGameObject(pickedObjID);
			if (pickedObject == null || !pickedObject.isTemporary()) {
				if (inspectedObject != null) {
					inspectedObject.outlined(false);
				}

				inspectedObject = pickedObject;

				if (inspectedObject != null) {
					inspectedObject.outlined(true);
					this.mover.attach(inspectedObject);
				} else {
					this.mover.detach();
				}
			}
		}

		this.mover.update(deltaTime);
	}
}
