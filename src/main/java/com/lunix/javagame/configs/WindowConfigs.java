package com.lunix.javagame.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "window")
public record WindowConfigs(int height, int width, String title, boolean maximized) {
}
