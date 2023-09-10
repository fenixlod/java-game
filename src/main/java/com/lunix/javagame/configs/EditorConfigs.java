package com.lunix.javagame.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "editor")
public record EditorConfigs(int gridSize) {
}
