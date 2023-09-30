package com.lunix.javagame.engine.ui;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.enums.EventType;
import com.lunix.javagame.engine.enums.GameSceneType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;

import imgui.ImGui;

public class EditorMenuBar {
	public void show() {
		ImGui.beginMenuBar();

		GameSceneType currentSceneType = GameInstance.get().sceneManager().currentSceneType();
		if (ImGui.beginMenu("SCENE")) {
			if (ImGui.menuItem("WORLD", "", currentSceneType == GameSceneType.WORLD,
					currentSceneType != GameSceneType.WORLD)) {
				EventSystem.notify(new Event(EventType.SET_SCENE_WORLD));
			}

			if (ImGui.menuItem("MENU", "", currentSceneType == GameSceneType.MAIN_MENU,
					currentSceneType != GameSceneType.MAIN_MENU)) {
				EventSystem.notify(new Event(EventType.SET_SCENE_MAIN_MENU));
			}

			if (ImGui.menuItem("TEST", "", currentSceneType == GameSceneType.TEST,
					currentSceneType != GameSceneType.TEST)) {
				EventSystem.notify(new Event(EventType.SET_SCENE_TEST));
			}
			ImGui.endMenu();
		}

		if (ImGui.menuItem("SAVE", "CTRL+S")) {
			EventSystem.notify(new Event(EventType.SAVE_LEVEL));
		}

		if (ImGui.menuItem("LOAD", "CTRL+O")) {
			EventSystem.notify(new Event(EventType.LOAD_LEVEL));
		}

		if (ImGui.menuItem("PLAY", "ALT+P")) {
			EventSystem.notify(new Event(EventType.GAME_START_PLAY));
		}

		ImGui.endMenuBar();
	}
}
