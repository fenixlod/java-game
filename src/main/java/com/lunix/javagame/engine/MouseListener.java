package com.lunix.javagame.engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Arrays;

import org.joml.Intersectionf;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.lunix.javagame.engine.util.VectorUtil;

public class MouseListener {
	private Vector2d scroll;
	private Vector2d positionInWindow;
	private Vector2d deltaInWindow;
	private boolean pressedButtons[][] = new boolean[2][5];//Pos[0] = if button is pressed, pos[1] = if it is clicked this frame
	private boolean dragging;
	private Vector3f positionInWorld; //Position of the cursor in world coordinates
	private Vector3f positionInWorldProjected; //Position of the cursor in world coordinates projected on the ground (Z = 0)
	private Vector3f deltaInWorld;

	public MouseListener() {
		scroll = new Vector2d();
		positionInWindow = new Vector2d();
		deltaInWindow = new Vector2d();
		deltaInWorld = new Vector3f();
		positionInWorldProjected = new Vector3f();
	}

	/**
	 * Execute this callback function every time when mouse is moved.
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void positionCallback(long window, double xPos, double yPos) {
		deltaInWindow.x = xPos - positionInWindow.x;
		deltaInWindow.y = yPos - positionInWindow.y;
		positionInWindow.x = xPos;
		positionInWindow.y = yPos;
		Vector3f old = positionInWorldProjected;
		calculateWorldPosition();
		deltaInWorld = positionInWorldProjected.sub(old, new Vector3f());
		
		if (pressedButtons[0][0] || pressedButtons[0][1]) {
			dragging = true;
		}
	}

	/**
	 * Execute this callback function every time when mouse button is clicked.
	 * 
	 * @param button
	 * @param action
	 * @param modifiers
	 */
	public void buttonCallback(long window, int button, int action, int modifiers) {
		if (button >= pressedButtons[0].length)
			return;

		if (action == GLFW_PRESS) {
			pressedButtons[0][button] = true;
			pressedButtons[1][button] = true;
		} else if (action == GLFW_RELEASE) {
			pressedButtons[0][button] = false;
			if (!pressedButtons[0][0] || !pressedButtons[0][1]) {
				dragging = false;
			}
		}
	}

	/**
	 * Execute this callback function each time when mouse scroll is changed.
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void scrollCallback(long window, double xOffset, double yOffset) {
		scroll.x = xOffset;
		scroll.y = yOffset;
	}

	/**
	 * Reset the delta variables. The delta variables contains changes in position
	 * or scroll since the last frame. These variables should be reset at the end of
	 * each frame.
	 */
	public void reset() {
		scroll.set(0.0);
		deltaInWindow.set(0.0);
		deltaInWorld.set(0);

		for (int i = 0; i < pressedButtons[1].length; i++) {
			pressedButtons[1][i] = false;
		}
	}

	public boolean isButtonPressed(int button) {
		if (button >= pressedButtons[0].length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return pressedButtons[0][button];
	}
	
	public boolean isButtonClicked(int button) {
		if (button >= pressedButtons[1].length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return pressedButtons[1][button];
	}

	public Vector2d scroll() {
		return scroll;
	}

	public Vector2d positionInWindow() {
		return positionInWindow;
	}

	public Vector2d deltaInWindow() {
		return deltaInWindow;
	}

	public Vector3f deltaInWorld() {
		return deltaInWorld;
	}

	public boolean dragging() {
		return dragging;
	}

	/**
	 * Get the current mouse position in world coordinates.
	 * 
	 * @return
	 */
	private void calculateWorldPosition() {
		float cursorX = (float) positionInWindow.x;
		float cursorY = (float) positionInWindow.y;

		if (GameInstance.get().window().viewPortSize().lengthSquared() > 0) {
			Vector2i offset = GameInstance.get().window().viewPortOffset();
			cursorX = (cursorX - offset.x) / GameInstance.get().window().viewPortSize().x;
			cursorY = (cursorY - offset.y) / GameInstance.get().window().viewPortSize().y;
		} else {
			Vector2i size = GameInstance.get().window().windowSize();
			cursorX /= size.x;
			cursorY /= size.y;
		}

		Vector4f nsc = new Vector4f(cursorX * 2f - 1, 1 - cursorY * 2f, -1, 1);
		nsc.mul(GameInstance.get().camera().inverseViewXProjection());
		positionInWorld = new Vector3f(nsc.x, nsc.y, nsc.z);

		Vector3f orig = new Vector3f(positionInWorld);
		Vector3f dir = VectorUtil.viewDirection();
		Vector3f planeOrg = new Vector3f();
		Vector3f planeNorm = new Vector3f(0, 0, 1);
		float dist = Intersectionf.intersectRayPlane(orig, dir, planeOrg, planeNorm, 1);
		orig.add(dir.mul(dist));
		positionInWorldProjected = orig;
	}

	public Vector2i positionInViewPort() {
		float cursorX = (float) positionInWindow.x;
		float cursorY = (float) positionInWindow.y;
		Vector2i size = GameInstance.get().window().windowSize();

		if (GameInstance.get().window().viewPortSize().lengthSquared() > 0) {
			Vector2i offset = GameInstance.get().window().viewPortOffset();
			cursorX = (cursorX - offset.x) / GameInstance.get().window().viewPortSize().x;
			cursorY = (cursorY - offset.y) / GameInstance.get().window().viewPortSize().y;

			cursorX *= size.x;
			cursorY *= size.y;
			cursorY = size.y - cursorY;
		}

		return new Vector2i((int) cursorX, (int) cursorY);
	}

	public Vector3f positionInWorld() {
		return positionInWorld;
	}

	public Vector3f positionInWorldProjected() {
		return positionInWorldProjected;
	}

	@Override
	public String toString() {
		return "MouseListener [X=" + positionInWindow.x + ", Y=" + positionInWindow.y + ", dX=" + deltaInWindow.x + ", dY=" + deltaInWindow.y +
				", pressedButtons=" + Arrays.toString(pressedButtons[0]) + ", dragging=" + dragging
				+ 
				", scrollX=" + scroll.x + ", scrollY=" + scroll.y + "]";
	}
}
