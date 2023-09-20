package com.lunix.javagame.engine.controlls;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Camera;
import com.lunix.javagame.engine.GameInstance;

public class EditorControlls {
	private Camera controlledCamera;
	private GameInstance game;
	private Vector3f clickOrigin;
	private float drageDebounce = 0.032f;
	private float dragSensitivity = 30f;
	private float scrollSensitivity = 0.05f;
	private float resetSpeed = 3;
	private float lerpTime = 0f;
	private boolean reset;

	public EditorControlls(Camera cameraToControll) {
		this.controlledCamera = cameraToControll;
		this.game = GameInstance.get();
		this.clickOrigin = new Vector3f();
	}

	public void init() {
		this.controlledCamera.setOrthoProjection();
		this.controlledCamera.position(new Vector3f());
	}

	public void update(float deltaTime) {
		if (game.mouse().dragging() && game.mouse().isButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
			if (this.drageDebounce > 0) {
				this.clickOrigin = game.mouse().positionInWorldProjected();
				this.drageDebounce -= deltaTime;
			} else {
				Vector3f currentPos = game.mouse().positionInWorldProjected();
				Vector3f change = currentPos.sub(this.clickOrigin, new Vector3f());
				this.controlledCamera.move(change.mul(-deltaTime).mul(dragSensitivity));
				this.clickOrigin.lerp(currentPos, deltaTime);
			}
		} else {
			if (this.drageDebounce < 0) {
				this.drageDebounce = 0.032f;
			}
		}

		if (GameInstance.get().mouse().scroll().y != 0) {
			float addValue = (float) Math.pow(Math.abs(game.mouse().scroll().y * scrollSensitivity),
					controlledCamera.zoomFactor());

			if (addValue > 0.15)
				addValue = 0.15f;

			addValue *= Math.signum(game.mouse().scroll().y);
			game.camera().changeZoom(addValue);
		}
		
		if(game.keyboard().isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
			reset = true;
		}
		
		if(reset) {
			Vector3f newPos = controlledCamera.position().lerp(new Vector3f(), deltaTime * lerpTime * resetSpeed,
					new Vector3f());
			controlledCamera
					.zoomFactor(controlledCamera.zoomFactor() + ((1f - controlledCamera.zoomFactor()) * deltaTime));
			lerpTime += 0.5f * deltaTime;
			controlledCamera.position(newPos);
			
			if (Math.abs(controlledCamera.position().x) <= 5 && Math.abs(controlledCamera.position().y) <= 5) {
				controlledCamera.position(new Vector3f());
				reset = false;
				lerpTime = 0f;
				controlledCamera.zoomFactor(1f);
			}
		}
	}
}
