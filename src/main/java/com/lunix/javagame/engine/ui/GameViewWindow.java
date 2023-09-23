package com.lunix.javagame.engine.ui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.enums.EventType;
import com.lunix.javagame.engine.graphic.FrameBuffer;
import com.lunix.javagame.engine.observers.Event;
import com.lunix.javagame.engine.observers.EventSystem;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {
	private static final Logger logger = LogManager.getLogger(GameViewWindow.class);
	private float leftX;
	private float rightX;
	private float topY;
	private float bottomY;
	private GameInstance game;
	private boolean isPlaying;

	public GameViewWindow() {
		game = GameInstance.get();
	}

	public void show(FrameBuffer frameBuffer, Scene currentScene) {
		ImGui.begin("Game viewport",
				ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

		ImGui.beginMenuBar();
		if (ImGui.menuItem("SAVE", "CTRL+S", isPlaying, !isPlaying)) {
			try {
				currentScene.save();
			} catch (IOException e) {
				logger.error("Unable to save current lavel", e);
			}
		}

		if (ImGui.menuItem("PLAY", "ALT+P", isPlaying, !isPlaying)) {
			isPlaying = true;
			EventSystem.notify(new Event(EventType.GAME_START_PLAY));
		}

		if (ImGui.menuItem("STOP", "ALT+S", !isPlaying, isPlaying)) {
			isPlaying = false;
			EventSystem.notify(new Event(EventType.GAME_END_PLAY));
		}

		ImGui.endMenuBar();

		ImVec2 windowSize = getLargestSizeForViewport();
		ImVec2 windowPosition = getCenteredPositionForViewoirt(windowSize);

		ImGui.setCursorPos(windowPosition.x, windowPosition.y);

		ImVec2 topLeft = new ImVec2();
		ImGui.getCursorScreenPos(topLeft);
		topLeft.x -= ImGui.getScrollX();
		topLeft.y -= ImGui.getScrollY();

		leftX = topLeft.x;
		bottomY = topLeft.y;
		rightX = topLeft.x + windowSize.x;
		topY = topLeft.y + windowSize.y;

		int textureId = frameBuffer.texture().id();
		ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

		game.window().viewPortOffset((int) topLeft.x, (int) topLeft.y);
		game.window().viewPortSize((int) windowSize.x, (int) windowSize.y);

		ImGui.end();
	}

	private ImVec2 getLargestSizeForViewport() {
		float targerAspectRatio = game.window().targerAspectRatio();
		ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);
		windowSize.x -= ImGui.getScrollX();
		windowSize.y -= ImGui.getScrollY();
		float aspectWidth = windowSize.x;
		float aspectHeigth = aspectWidth / targerAspectRatio;
		if (aspectHeigth > windowSize.y) {
			// We must switch to pillar mode
			aspectHeigth = windowSize.y;
			aspectWidth = aspectHeigth * targerAspectRatio;
		}
		return windowSize.set(aspectWidth, aspectHeigth);
	}

	private ImVec2 getCenteredPositionForViewoirt(ImVec2 aspectSize) {
		ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);
		windowSize.x -= ImGui.getScrollX();
		windowSize.y -= ImGui.getScrollY();
		float viewportX = (windowSize.x / 2f) - (aspectSize.x / 2f);
		float viewportY = (windowSize.y / 2f) - (aspectSize.y / 2f);

		return windowSize.set(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
	}

	public boolean getWantCaptureMouse() {
		Vector2d cursorPosition = game.mouse().positionInWindow();
		return cursorPosition.x >= leftX && cursorPosition.x <= rightX && cursorPosition.y >= bottomY
				&& cursorPosition.y <= topY;
	}
}
