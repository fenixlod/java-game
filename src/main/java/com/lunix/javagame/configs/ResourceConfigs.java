package com.lunix.javagame.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resources")
public record ResourceConfigs(Map<String, String> shaders) {
}
