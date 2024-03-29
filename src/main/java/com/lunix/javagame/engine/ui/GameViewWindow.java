package com.lunix.javagame.engine.ui;

import org.joml.Vector2d;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.graphic.FrameBuffer;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {
	private float leftX;
	private float rightX;
	private float topY;
	private float bottomY;
	private GameInstance game;

	public void init() {
		game = GameInstance.get();
	}

	public void show(FrameBuffer frameBuffer) {
		ImGui.begin("Game viewport",
				ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);
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
