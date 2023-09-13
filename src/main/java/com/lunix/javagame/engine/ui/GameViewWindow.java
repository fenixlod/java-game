package com.lunix.javagame.engine.ui;

import org.joml.Vector2d;
import org.joml.Vector2f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.graphic.FrameBuffer;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {
	private static float leftX;
	private static float rightX;
	private static float topY;
	private static float bottomY;

	public static void show(FrameBuffer frameBuffer) {
		ImGui.begin("Game viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
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

		GameInstance.get().mouse().gameViewportPosition(new Vector2f(topLeft.x, topLeft.y));
		GameInstance.get().mouse().gameViewportSize(new Vector2f(windowSize.x, windowSize.y));

		ImGui.end();
	}

	private static ImVec2 getLargestSizeForViewport() {
		float targerAspectRatio = GameInstance.get().window().targerAspectRatio();
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

	private static ImVec2 getCenteredPositionForViewoirt(ImVec2 aspectSize) {
		ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);
		windowSize.x -= ImGui.getScrollX();
		windowSize.y -= ImGui.getScrollY();
		float viewportX = (windowSize.x / 2f) - (aspectSize.x / 2f);
		float viewportY = (windowSize.y / 2f) - (aspectSize.y / 2f);

		return windowSize.set(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
	}

	public static boolean getWantCaptureMouse() {
		Vector2d cursorPosition = GameInstance.get().mouse().position();
		return cursorPosition.x >= leftX && cursorPosition.x <= rightX && cursorPosition.y >= bottomY
				&& cursorPosition.y <= topY;
	}
}
