package com.lunix.javagame.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.engine.util.VectorUtil;

public class Camera {
	private final CameraConfigs cameraConfig;
	private Vector3f position;
	private Vector3f offsets;
	private float zoomFactor;
	private Matrix4f viewXProjectionMatrix;
	private Matrix4f inverseViewXProjection;

	public Camera(CameraConfigs cameraConfig) {
		this.cameraConfig = cameraConfig;
		this.offsets = new Vector3f(cameraConfig.xOffset(), cameraConfig.yOffset(), cameraConfig.zOffset());
		this.zoomFactor = 1f;
		this.viewXProjectionMatrix = new Matrix4f();
		this.inverseViewXProjection = new Matrix4f();
		this.position = new Vector3f();
		VectorUtil.setView(offsets);
		this.calculateViewXProjectionMatrix();
	}

//	public void setOrthoProjection() {
//		this.projectionMatrix.identity();
//		this.projectionMatrix.ortho(-this.cameraConfig.ortho().width() / 2, this.cameraConfig.ortho().width() / 2,
//				-this.cameraConfig.ortho().height() / 2, this.cameraConfig.ortho().height() / 2,
//				this.cameraConfig.zNear(), this.cameraConfig.zFar());
//
//		this.projectionMatrix.invert(this.inverseProjection);
//	}
//
//	public void setPerspectiveProjection(float aspect) {
//		this.projectionMatrix.identity();
//		this.projectionMatrix.perspective((float) Math.toRadians(this.cameraConfig.prespective().fieldOfView()), aspect,
//				this.cameraConfig.zNear(), this.cameraConfig.zFar());
//
//		this.projectionMatrix.invert(this.inverseProjection);
//	}
//
//	private void calculateViewMatrix() {
//		this.viewMatrix.identity();
//		this.viewMatrix = this.viewMatrix.lookAt(this.offsets, new Vector3f(), VectorUtil.Z());
//		this.viewMatrix.scale(this.zoomFactor);
//		this.viewMatrix.translate(this.position.mul(-1, new Vector3f()));
//		this.viewMatrix.invert(this.inverseView);
//	}

	public void position(Vector3f pos) {
		this.position = pos;
		calculateViewXProjectionMatrix();
	}

	public void move(Vector3f change) {
		this.position.add(change);
		calculateViewXProjectionMatrix();
	}

	public Vector3f position() {
		return this.position;
	}

	public void changeZoom(float change) {
		this.zoomFactor = Math.min(Math.max(this.zoomFactor + change, 0.2f), 2f);
		calculateViewXProjectionMatrix();
	}

	public float zoomFactor() {
		return this.zoomFactor;
	}

	public void zoomFactor(float zoom) {
		this.zoomFactor = zoom;
		calculateViewXProjectionMatrix();
	}
	
	private void calculateViewXProjectionMatrix() {
		this.viewXProjectionMatrix
				.identity()
				.ortho(-this.cameraConfig.ortho().width() / 2, this.cameraConfig.ortho().width() / 2,
					-this.cameraConfig.ortho().height() / 2, this.cameraConfig.ortho().height() / 2,
					this.cameraConfig.zNear(), this.cameraConfig.zFar())
				.lookAt(this.offsets, new Vector3f(), VectorUtil.Z())
				.scale(this.zoomFactor)
				.translate(this.position.mul(-1, new Vector3f()));
		this.viewXProjectionMatrix.invert(this.inverseViewXProjection);
	}

	public Matrix4f inverseViewXProjection() {
		return inverseViewXProjection;
	}

	public Matrix4f viewXProjectionMatrix() {
		return viewXProjectionMatrix;
	}
}
