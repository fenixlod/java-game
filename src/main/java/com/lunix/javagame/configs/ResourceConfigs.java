package com.lunix.javagame.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lunix.javagame.engine.enums.ShaderType;

@ConfigurationProperties(prefix = "resources")
public record ResourceConfigs(Map<ShaderType, String> shaders) {
}
