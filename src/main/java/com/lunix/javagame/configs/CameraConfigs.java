package com.lunix.javagame.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "camera")
public record CameraConfigs(OrthoCameraConfig ortho, PrespectiveCameraConfig prespective, float xOffset, float yOffset,
		float zOffset, float zNear, float zFar) {
	public record OrthoCameraConfig(float width, float height) {
	}

	public record PrespectiveCameraConfig(float fieldOfView) {
	}
}
