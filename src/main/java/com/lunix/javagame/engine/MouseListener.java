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
		this.scroll = new Vector2d();
		this.positionInWindow = new Vector2d();
		this.deltaInWindow = new Vector2d();
		this.deltaInWorld = new Vector3f();
		this.positionInWorldProjected = new Vector3f();
	}

	/**
	 * Execute this callback function every time when mouse is moved.
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void positionCallback(long window, double xPos, double yPos) {
		this.deltaInWindow.x = xPos - this.positionInWindow.x;
		this.deltaInWindow.y = yPos - this.positionInWindow.y;
		this.positionInWindow.x = xPos;
		this.positionInWindow.y = yPos;
		Vector3f old = this.positionInWorldProjected;
		calculateWorldPosition();
		this.deltaInWorld = this.positionInWorldProjected.sub(old, new Vector3f());
		
		if (pressedButtons[0][0] || pressedButtons[0][1]) {
			this.dragging = true;
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
		if (button >= this.pressedButtons[0].length)
			return;

		if (action == GLFW_PRESS) {
			this.pressedButtons[0][button] = true;
			this.pressedButtons[1][button] = true;
		} else if (action == GLFW_RELEASE) {
			this.pressedButtons[0][button] = false;
			if (!pressedButtons[0][0] || !pressedButtons[0][1]) {
				this.dragging = false;
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
		this.scroll.x = xOffset;
		this.scroll.y = yOffset;
	}

	/**
	 * Reset the delta variables. The delta variables contains changes in position
	 * or scroll since the last frame. These variables should be reset at the end of
	 * each frame.
	 */
	public void reset() {
		this.scroll.set(0.0);
		this.deltaInWindow.set(0.0);
		this.deltaInWorld.set(0);

		for (int i = 0; i < this.pressedButtons[1].length; i++) {
			this.pressedButtons[1][i] = false;
		}
	}

	public boolean isButtonPressed(int button) {
		if (button >= this.pressedButtons[0].length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return this.pressedButtons[0][button];
	}
	
	public boolean isButtonClicked(int button) {
		if (button >= this.pressedButtons[1].length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return this.pressedButtons[1][button];
	}

	public Vector2d scroll() {
		return this.scroll;
	}

	public Vector2d positionInWindow() {
		return this.positionInWindow;
	}

	public Vector2d deltaInWindow() {
		return this.deltaInWindow;
	}

	public Vector3f deltaInWorld() {
		return this.deltaInWorld;
	}

	public boolean dragging() {
		return this.dragging;
	}

	/**
	 * Get the current mouse position in world coordinates.
	 * 
	 * @return
	 */
	private void calculateWorldPosition() {
		float cursorX = (float) this.positionInWindow.x;
		float cursorY = (float) this.positionInWindow.y;

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
		this.positionInWorld = new Vector3f(nsc.x, nsc.y, nsc.z);

		Vector3f orig = new Vector3f(this.positionInWorld);
		Vector3f dir = VectorUtil.viewDirection();
		Vector3f planeOrg = new Vector3f();
		Vector3f planeNorm = new Vector3f(0, 0, 1);
		float dist = Intersectionf.intersectRayPlane(orig, dir, planeOrg, planeNorm, 1);
		orig.add(dir.mul(dist));
		this.positionInWorldProjected = orig;
	}

	public Vector2i positionInViewPort() {
		float cursorX = (float) this.positionInWindow.x;
		float cursorY = (float) this.positionInWindow.y;
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
		return this.positionInWorld;
	}

	public Vector3f positionInWorldProjected() {
		return this.positionInWorldProjected;
	}

	@Override
	public String toString() {
		return "MouseListener [X=" + positionInWindow.x + ", Y=" + positionInWindow.y + ", dX=" + deltaInWindow.x + ", dY=" + deltaInWindow.y +
				", pressedButtons=" + Arrays.toString(pressedButtons[0]) + ", dragging=" + dragging
				+ 
				", scrollX=" + scroll.x + ", scrollY=" + scroll.y + "]";
	}
}
