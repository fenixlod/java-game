package com.lunix.javagame.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.engine.util.VectorUtil;

public class Camera {
	private final CameraConfigs cameraConfig;
	private Matrix4f projectionMatrix;
	private Matrix4f inverseProjection;
	private Matrix4f viewMatrix;
	private Matrix4f inverseView;
	private Vector3f position;
	private Vector3f offsets;
	private float zoomFactor;

	public Camera(CameraConfigs cameraConfig) {
		this.cameraConfig = cameraConfig;
		this.offsets = new Vector3f(cameraConfig.xOffset(), cameraConfig.yOffset(), cameraConfig.zOffset());
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.inverseProjection = new Matrix4f();
		this.inverseView = new Matrix4f();
		this.zoomFactor = 1f;
		VectorUtil.setView(offsets);
	}

	public void setOrthoProjection() {
		this.projectionMatrix.identity();
		this.projectionMatrix.ortho(-this.cameraConfig.ortho().width() / 2, this.cameraConfig.ortho().width() / 2,
				-this.cameraConfig.ortho().height() / 2, this.cameraConfig.ortho().height() / 2,
				this.cameraConfig.zNear(), this.cameraConfig.zFar());

		this.projectionMatrix.invert(this.inverseProjection);
	}

	public void setPerspectiveProjection(float aspect) {
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective((float) Math.toRadians(this.cameraConfig.prespective().fieldOfView()), aspect,
				this.cameraConfig.zNear(), this.cameraConfig.zFar());

		this.projectionMatrix.invert(this.inverseProjection);
	}

	private void calculateViewMatrix() {
		this.viewMatrix.identity();
		this.viewMatrix = this.viewMatrix.lookAt(this.offsets, new Vector3f(), VectorUtil.Z());
		this.viewMatrix.scale(this.zoomFactor);
		this.viewMatrix.translate(this.position.mul(-1, new Vector3f()));
		this.viewMatrix.invert(this.inverseView);
	}

	public Matrix4f viewMatrix() {
		return this.viewMatrix;
	}

	public Matrix4f projectionMatrix() {
		return this.projectionMatrix;
	}

	public void position(Vector3f pos) {
		this.position = pos;
		calculateViewMatrix();
	}

	public void move(Vector3f change) {
		this.position.add(change);
		calculateViewMatrix();
	}

	public Vector3f position() {
		return this.position;
	}

	public void changeZoom(float change) {
		this.zoomFactor = Math.min(Math.max(this.zoomFactor + change, 0.2f), 2f);
		calculateViewMatrix();
	}

	public Matrix4f inverseProjection() {
		return this.inverseProjection;
	}

	public Matrix4f inverseView() {
		return this.inverseView;
	}

	public float zoomFactor() {
		return this.zoomFactor;
	}

	public void zoomFactor(float zoom) {
		this.zoomFactor = zoom;
		calculateViewMatrix();
	}
}
