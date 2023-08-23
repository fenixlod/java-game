package com.lunix.javagame.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "camera")
public record CameraConfigs(float xOffset, float yOffset, float zOffset) {
}
