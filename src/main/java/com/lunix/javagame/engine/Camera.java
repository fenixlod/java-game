package com.lunix.javagame.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.engine.util.VectorUtil;

public class Camera {
	private final CameraConfigs cameraConfig;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f position;
	private Vector3f offsets;
	private float zoomFactor;

	public Camera(CameraConfigs cameraConfig) {
		this.cameraConfig = cameraConfig;
		this.offsets = new Vector3f(cameraConfig.xOffset(), cameraConfig.yOffset(), cameraConfig.zOffset());
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.zoomFactor = 1f;
		VectorUtil.setView(offsets);
	}

	public void setOrthoProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(-cameraConfig.ortho().width() / 2, cameraConfig.ortho().width() / 2,
				-cameraConfig.ortho().height() / 2, cameraConfig.ortho().height() / 2,
				cameraConfig.zNear(), cameraConfig.zFar());
	}

	public void setPerspectiveProjection(float aspect) {
		projectionMatrix.identity();
		projectionMatrix.perspective((float) Math.toRadians(cameraConfig.prespective().fieldOfView()), aspect,
				cameraConfig.zNear(), cameraConfig.zFar());
	}

	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		viewMatrix = viewMatrix.lookAt(offsets, new Vector3f(), VectorUtil.Z());
		viewMatrix.scale(zoomFactor);
		viewMatrix.translate(position.mul(-1, new Vector3f()));
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

	public Vector3f position() {
		return this.position;
	}

	public void changeZoom(float change) {
		this.zoomFactor = Math.min(Math.max(zoomFactor + change, 0.1f), 2f);
	}

	public Vector3f offsets() {
		return this.offsets;
	}
}
