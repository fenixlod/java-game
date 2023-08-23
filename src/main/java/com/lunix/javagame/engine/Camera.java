package com.lunix.javagame.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f position;
	private Vector3f offsets;

	public Camera(Vector3f position, Vector3f offsets, float aspect) {
		this.position = position;
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.offsets = offsets;
		adjustProjection(aspect);
	}

	public void adjustProjection(float aspect) {
		projectionMatrix.identity();
		//projectionMatrix.perspective((float) Math.toRadians(60.0f), aspect, 0.01f, 1000.f);
		projectionMatrix.ortho(0.0f, 400.0f, 0.0f, 250.0f, 0.0f, 500.0f);
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraEye = position.add(offsets, new Vector3f());
		viewMatrix.identity();
		viewMatrix = viewMatrix.lookAt(
				cameraEye,
				position,
				new Vector3f(0.0f, 1.0f, 0.0f));

		// correction for Z axis to be UP
		viewMatrix.rotate((float) Math.toRadians(-90.0f), new Vector3f(1.0f, 0.0f, 0.0f));
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setPosition(Vector3f pos) {
		position = pos;
	}

	public void move(Vector3f change) {
		position.add(change);
	}
}
