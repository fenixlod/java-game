package com.lunix.javagame.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

import org.joml.Intersectionf;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

public class MouseListener {
	private double scrollX;
	private double scrollY;
	private double positionX;
	private double positionY;
	private double deltaX;
	private double deltaY;
	private boolean buttonPressed[] = new boolean[5];
	private boolean dragging;

	public void bindToWindow(long windowHandler, GLFWMouseButtonCallbackI mouseBtnCb, GLFWScrollCallbackI scrollCb) {
		glfwSetCursorPosCallback(windowHandler, (win, xPos, yPos) -> positionCallback(xPos, yPos));
		glfwSetMouseButtonCallback(windowHandler, (win, button, action, mod) -> {
			buttonCallback(button, action, mod);
			mouseBtnCb.invoke(win, button, action, mod);
		});
		glfwSetScrollCallback(windowHandler, (win, xScroll, yScroll) -> {
			scrollCallback(xScroll, yScroll);
			scrollCb.invoke(win, xScroll, yScroll);
		});
	}

	private void positionCallback(double xPos, double yPos) {
		deltaX = xPos - positionX;
		deltaY = yPos - positionY;
		positionX = xPos;
		positionY = yPos;
		
		for (boolean buttonIsPressed : buttonPressed) {
			if (buttonIsPressed == true) {
				dragging = true;
				break;
			}
		}
	}

	private void buttonCallback(int button, int action, int modifiers) {
		if (button >= buttonPressed.length)
			return;

		if (action == GLFW_PRESS)
			buttonPressed[button] = true;
		else if (action == GLFW_RELEASE) {
			buttonPressed[button] = false;
			dragging = false;
		}
	}

	private void scrollCallback(double xOffset, double yOffset) {
		scrollX = xOffset;
		scrollY = yOffset;
	}

	/**
	 * Reset the delta variables. The delta variables contains changes in position
	 * or scroll since the last frame. These variables should be reset at the end of
	 * each frame.
	 */
	public void reset() {
		scrollX = 0.0;
		scrollY = 0.0;
		deltaX = 0.0;
		deltaY = 0.0;
	}

	public boolean isButtonPressed(int button) {
		if (button >= buttonPressed.length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return buttonPressed[button];
	}

	public double getScrollX() {
		return scrollX;
	}

	public double getScrollY() {
		return scrollY;
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public boolean isDragging() {
		return dragging;
	}

	public Vector3f worldPosition() {
		float[] size = GameInstance.get().window().size();
		double currentX = positionX;
		currentX = (currentX / size[0]) * 2f - 1;
		double currentY = positionY;
		currentY = (currentY / size[1]) * 2f - 1;
		Vector4d tmp = new Vector4d(currentX, currentY, -1, 1);
		tmp.mul(GameInstance.get().camera().inverseProjection()).mul(GameInstance.get().camera().inverseView());

		// ADD
		// tmp = new Vector4f(tmp.x / tmp.w, tmp.y / tmp.w, tmp.z / tmp.w, 0.0f);
		Vector3f orig = new Vector3f((float) tmp.x, (float) tmp.y, (float) tmp.z);
		Vector3f dir =  new Vector3f(-100, -100, 100).normalize().mul(-1);
		Vector3f olaneOrg =  new Vector3f();
		Vector3f olaneNorm =  new Vector3f(0,0,1);
		float dist = Intersectionf.intersectRayPlane(orig, dir, olaneOrg, olaneNorm, 1);
		orig.add(dir.mul(dist));

		currentX = orig.x;
		currentY = orig.y;
		System.out.println("Current X=" + currentX + " Y=" + currentY);
		return null;
	}

	@Override
	public String toString() {
		return "MouseListener [X=" + positionX + ", Y=" + positionY
				+ ", dX=" + deltaX + ", dY=" + deltaY + ", buttonPressed="
				+ Arrays.toString(buttonPressed) + ", dragging=" + dragging + ", scrollX=" + scrollX + ", scrollY="
				+ scrollY + "]";
	}
}
