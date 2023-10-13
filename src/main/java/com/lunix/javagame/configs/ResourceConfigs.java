package com.lunix.javagame.configs;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lunix.javagame.configs.ResourceConfigs.SoundData;
import com.lunix.javagame.configs.ResourceConfigs.SpriteSheetData;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.SoundType;
import com.lunix.javagame.engine.enums.TextureType;

@ConfigurationProperties(prefix = "resources")
public record ResourceConfigs(Map<ShaderType, String> shaders, Map<TextureType, SpriteSheetData> textures,
		Map<SoundType, SoundData> sounds) {
	public record SpriteSheetData(String path, int width, int height) {
	}

	public record SoundData(String path, boolean loops) {
	}
}
