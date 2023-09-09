package com.lunix.javagame.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;

@ConfigurationProperties(prefix = "resources")
public record ResourceConfigs(Map<ShaderType, String> shaders, Map<TextureType, SpriteSheetData> textures) {
	public record SpriteSheetData(String path, int width, int height) {
	}
}
