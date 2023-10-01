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
		viewXProjectionMatrix = new Matrix4f();
		inverseViewXProjection = new Matrix4f();
		reset();
	}

//	public void setOrthoProjection() {
//		projectionMatrix.identity();
//		projectionMatrix.ortho(-cameraConfig.ortho().width() / 2, cameraConfig.ortho().width() / 2,
//				-cameraConfig.ortho().height() / 2, cameraConfig.ortho().height() / 2,
//				cameraConfig.zNear(), cameraConfig.zFar());
//
//		projectionMatrix.invert(inverseProjection);
//	}
//
//	public void setPerspectiveProjection(float aspect) {
//		projectionMatrix.identity();
//		projectionMatrix.perspective((float) Math.toRadians(cameraConfig.prespective().fieldOfView()), aspect,
//				cameraConfig.zNear(), cameraConfig.zFar());
//
//		projectionMatrix.invert(inverseProjection);
//	}
//
//	private void calculateViewMatrix() {
//		viewMatrix.identity();
//		viewMatrix = viewMatrix.lookAt(offsets, new Vector3f(), VectorUtil.Z());
//		viewMatrix.scale(zoomFactor);
//		viewMatrix.translate(position.mul(-1, new Vector3f()));
//		viewMatrix.invert(inverseView);
//	}

	public void position(Vector3f pos) {
		position = pos;
		calculateViewXProjectionMatrix();
	}

	public void move(Vector3f change) {
		position.add(change);
		calculateViewXProjectionMatrix();
	}

	public Vector3f position() {
		return position;
	}

	public void changeZoom(float change) {
		zoomFactor = Math.min(Math.max(zoomFactor + change, 0.2f), 2f);
		calculateViewXProjectionMatrix();
	}

	public float zoomFactor() {
		return zoomFactor;
	}

	public void zoomFactor(float zoom) {
		zoomFactor = zoom;
		calculateViewXProjectionMatrix();
	}
	
	private void calculateViewXProjectionMatrix() {
		Vector3f up = offsets.cross(VectorUtil.Z(), new Vector3f());
		if (up.isFinite() && up.lengthSquared() > 0)
			up = VectorUtil.Z();
		else
			up = VectorUtil.Y();

		viewXProjectionMatrix
				.identity()
				.ortho(-cameraConfig.ortho().width() / 2, cameraConfig.ortho().width() / 2,
						-cameraConfig.ortho().height() / 2, cameraConfig.ortho().height() / 2, cameraConfig.zNear(),
						cameraConfig.zFar())
				.lookAt(offsets, new Vector3f(), up).scale(zoomFactor)
				.translate(position.mul(-1, new Vector3f()));
		viewXProjectionMatrix.invert(inverseViewXProjection);
	}

	public Matrix4f inverseViewXProjection() {
		return inverseViewXProjection;
	}

	public Matrix4f viewXProjectionMatrix() {
		return viewXProjectionMatrix;
	}

	public void offsets(Vector3f offsets) {
		this.offsets = offsets;
		calculateViewXProjectionMatrix();
		VectorUtil.setView(offsets);
	}

	public Vector3f offsets() {
		return offsets;
	}

	public void reset() {
		offsets = new Vector3f(cameraConfig.xOffset(), cameraConfig.yOffset(), cameraConfig.zOffset());
		zoomFactor = 1f;
		position = new Vector3f();
		calculateViewXProjectionMatrix();
		VectorUtil.setView(offsets);
	}
}
