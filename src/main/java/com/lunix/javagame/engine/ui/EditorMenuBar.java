package com.lunix.javagame.engine.ui;

import com.lunix.javagame.engine.enums.EventType;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;

import imgui.ImGui;

public class EditorMenuBar {
	public void show() {
		ImGui.beginMenuBar();
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
