package com.lunix.javagame.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.ResourceConfigs;
import com.lunix.javagame.configs.ResourceConfigs.SpriteSheetData;
import com.lunix.javagame.engine.enums.ResourceType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.graphic.SpriteSheet;
import com.lunix.javagame.engine.graphic.Texture;

@Component
public class ResourcePool {
	private static final Logger logger = LogManager.getLogger(ResourcePool.class);
	private final ResourceConfigs resourceConfig;
	private static final Map<ShaderType, Shader> shaders;
	private static final Map<TextureType, Texture> textures;
	private static final Map<TextureType, SpriteSheet> spriteSheets;

	static {
		shaders = new HashMap<>();
		textures = new HashMap<>();
		spriteSheets = new HashMap<>();
	}

	public ResourcePool(ResourceConfigs resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void init() throws IOException, ResourceNotFound {
		initShaders(resourceConfig.shaders());
		initTextures(resourceConfig.textures());
		initSpriteSheets(resourceConfig.spriteSheet());
	}

	private void initShaders(Map<ShaderType, String> shadersToLoad) throws IOException {
		logger.info("Loading shaders...");
		for (Entry<ShaderType, String> entry : shadersToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading shader: {}", path);
			Shader shader = new Shader(entry.getKey(), path);
			shaders.put(entry.getKey(), shader);
		}
	}

	private void initTextures(Map<TextureType, String> texturesToLoad) throws IOException {
		logger.info("Loading textures...");
		for (Entry<TextureType, String> entry : texturesToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading texture: {}", path);
			Texture texture = new Texture(entry.getKey(), path);
			textures.put(entry.getKey(), texture);
		}
	}

	private void initSpriteSheets(List<SpriteSheetData> sheetsData) throws IOException, ResourceNotFound {
		for (SpriteSheetData ssd : sheetsData) {
			spriteSheets.put(ssd.texture(), new SpriteSheet(ssd));
		}
	}

	public static void loadResources(ResourceType... resources) throws ResourceNotFound, IOException {
		for (ResourceType resource : resources) {
			if (resource instanceof ShaderType shader) {
				getShader(shader);
			} else if (resource instanceof TextureType texture) {
				getTexture(texture);
			}
		}
	}

	public static Shader getShader(ShaderType shaderName) throws ResourceNotFound {
		Shader shader = shaders.get(shaderName);
		if (shader == null) {
			logger.warn("Unable to find shader: {}", shaderName);
			throw new ResourceNotFound("Shader not found: " + shaderName);
		}

		shader.compile();
		return shader;
	}

	public static Texture getTexture(TextureType textureType) throws ResourceNotFound, IOException {
		Texture texture = textures.get(textureType);
		if (texture == null) {
			logger.warn("Unable to find texture: {}", textureType);
			throw new ResourceNotFound("Texture not found: " + textureType);
		}

		texture.load();
		return texture;
	}

	public static Sprite getSprite(TextureType textureType, int index) throws ResourceNotFound, IOException {
		SpriteSheet sheet = spriteSheets.get(textureType);
		if (sheet == null) {
			logger.warn("Unable to find sprite sheet: {}", textureType);
			throw new ResourceNotFound("Sprite sheet not found: " + textureType);
		}

		return sheet.get(index);
	}
}
