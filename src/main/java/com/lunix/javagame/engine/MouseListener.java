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
	private Vector2d position;
	private Vector2d delta;
	private boolean pressedButtons[] = new boolean[5];
	private boolean dragging;

	public MouseListener() {
		this.scroll = new Vector2d();
		this.position = new Vector2d();
		this.delta = new Vector2d();
	}

	/**
	 * Execute this callback function every time when mouse is moved.
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void positionCallback(long window, double xPos, double yPos) {
		this.delta.x = xPos - this.position.x;
		this.delta.y = yPos - this.position.y;
		this.position.x = xPos;
		this.position.y = yPos;
		
		for (boolean pressedButton : pressedButtons) {
			if (pressedButton) {
				this.dragging = true;
				break;
			}
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
		if (button >= this.pressedButtons.length)
			return;

		if (action == GLFW_PRESS)
			this.pressedButtons[button] = true;
		else if (action == GLFW_RELEASE) {
			this.pressedButtons[button] = false;
			this.dragging = false;
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
		this.scroll.x = 0.0;
		this.scroll.y = 0.0;
		this.delta.x = 0.0;
		this.delta.y = 0.0;
	}

	public boolean isButtonPressed(int button) {
		if (button >= this.pressedButtons.length)
			throw new IllegalStateException("Invalid mouse button: " + button);

		return this.pressedButtons[button];
	}

	public Vector2d scroll() {
		return this.scroll;
	}

	public Vector2d position() {
		return this.position;
	}

	public Vector2d delta() {
		return this.delta;
	}

	public boolean dragging() {
		return this.dragging;
	}

	/**
	 * Get the current mouse position in world coordinates.
	 * 
	 * @return
	 */
	public Vector3f worldPosition() {
		float cursorX = (float) this.position.x;
		float cursorY = (float) this.position.y;

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
		nsc.mul(GameInstance.get().camera().inverseProjection()).mul(GameInstance.get().camera().inverseView());
		return new Vector3f(nsc.x, nsc.y, nsc.z);
	}

	/**
	 * Get the current mouse position in world coordinates projected on the ground.
	 * 
	 * @return
	 */
	public Vector3f worldPositionProjected() {
		Vector3f orig = worldPosition();
		Vector3f dir = VectorUtil.viewDirection();
		Vector3f olaneOrg =  new Vector3f();
		Vector3f olaneNorm = new Vector3f(0, 0, 1);
		float dist = Intersectionf.intersectRayPlane(orig, dir, olaneOrg, olaneNorm, 1);
		orig.add(dir.mul(dist));
		return orig;
	}

	@Override
	public String toString() {
		return "MouseListener [X=" + position.x + ", Y=" + position.y + ", dX=" + delta.x + ", dY=" + delta.y +
				", pressedButtons="	+ Arrays.toString(pressedButtons) + ", dragging=" + dragging + 
				", scrollX=" + scroll.x + ", scrollY=" + scroll.y + "]";
	}
}
