package com.lunix.javagame.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.ResourceConfigs;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.Texture;

@Component
public class Resources {
	private static final Logger logger = LogManager.getLogger(Resources.class);
	private final ResourceConfigs resourceConfig;
	private static final Map<ShaderType, Shader> shaders;
	private static final Map<TextureType, Texture> textures;

	static {
		shaders = new HashMap<>();
		textures = new HashMap<>();
	}

	public Resources(ResourceConfigs resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void init() throws IOException {
		loadShaders(resourceConfig.shaders());
		loadTextures(resourceConfig.textures());
	}

	private void loadShaders(Map<ShaderType, String> shadersToLoad) throws IOException {
		logger.info("Loading shaders...");
		for (Entry<ShaderType, String> entry : shadersToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading shader: {}", path);
			Shader shader = new Shader(entry.getKey(), path);
			shader.compile();
			shaders.put(entry.getKey(), shader);
		}
	}

	private void loadTextures(Map<TextureType, String> texturesToLoad) throws IOException {
		logger.info("Loading textures...");
		for (Entry<TextureType, String> entry : texturesToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading texture: {}", path);
			Texture texture = new Texture(entry.getKey(), path);
			texture.load();
			textures.put(entry.getKey(), texture);
		}
	}

	public static Shader getShader(ShaderType shaderName) {
		Shader shader = shaders.get(shaderName);
		if (shader == null) {
			logger.warn("Unable to find shader: {}, using the default one", shaderName);
			return shaders.get(ShaderType.DEFAULT);
		}
		return shader;
	}

	public static Texture getTexture(TextureType textureTYpe) throws Exception {
		Texture texture = textures.get(textureTYpe);
		if (texture == null) {
			logger.warn("Unable to find texture: {}, textureTYpe");
			throw new Exception("Texture not found");
		}
		return texture;
	}
}
