package com.lunix.javagame.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Map<String, Sprite> sprites;

	static {
		shaders = new HashMap<>();
		textures = new HashMap<>();
		spriteSheets = new HashMap<>();
		sprites = new HashMap<>();
	}

	public ResourcePool(ResourceConfigs resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void init() throws IOException, ResourceNotFound {
		initShaders(resourceConfig.shaders());
		initTextures(resourceConfig.textures());
		initSpriteSheets(resourceConfig.spriteSheet());
		initSprites(resourceConfig.textures());
	}

	private void initShaders(Map<ShaderType, String> shadersToLoad) {
		logger.info("Loading shaders...");
		for (Entry<ShaderType, String> entry : shadersToLoad.entrySet()) {
			logger.debug("Loading shader: {}", entry.getValue());
			Shader shader = new Shader(entry.getKey(), entry.getValue());
			shaders.put(entry.getKey(), shader);
		}
	}

	private void initTextures(Map<TextureType, String> texturesToLoad) {
		logger.info("Loading textures...");
		for (Entry<TextureType, String> entry : texturesToLoad.entrySet()) {
			logger.debug("Loading texture: {}", entry.getValue());
			Texture texture = new Texture(entry.getKey(), entry.getValue());
			textures.put(entry.getKey(), texture);
		}
	}

	private void initSpriteSheets(List<SpriteSheetData> sheetsData) throws IOException, ResourceNotFound {
		for (SpriteSheetData ssd : sheetsData) {
			logger.debug("Loading sprite sheet: {}", ssd.texture());
			SpriteSheet sheet = new SpriteSheet(getTexture(ssd.texture()), ssd.width(), ssd.height());
			spriteSheets.put(ssd.texture(),sheet);
		}
	}

	private void initSprites(Map<TextureType, String> allTextures) {
		sprites.put(TextureType.NONE.name(), new Sprite());
		for(TextureType texture : allTextures.keySet()) {
			SpriteSheet sheet = spriteSheets.get(texture);
			if(sheet == null) {
				Sprite sprite = new Sprite(textures.get(texture), 0, 0);
				sprites.put(texture.toString(), sprite);
			}
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

	public static Shader getShader(ShaderType shaderName) throws ResourceNotFound, IOException {
		Shader shader = shaders.get(shaderName);
		if (shader == null) {
			logger.warn("Unable to find shader: {}", shaderName);
			throw new ResourceNotFound("Shader not found: " + shaderName);
		}

		shader.load();
		return shader;
	}

	public static Texture getTexture(TextureType textureType) throws ResourceNotFound, IOException {
		Texture texture = textures.get(textureType);
		if (texture == null) {
			logger.warn("Unable to find texture: {}", textureType);
			throw new ResourceNotFound("Texture not found: " + textureType);
		}

		texture.load();

		SpriteSheet sheet = spriteSheets.get(texture.type());
		if (sheet != null) {
			sheet.load();
			int idx = 0;
			for (Sprite s : sheet.sprites()) {
				sprites.put(s.texture().type().name() + idx++, s);
			}
		}
		return texture;
	}

	public static Sprite getSprite(String name) {
		Sprite sprite = sprites.get(name);
		if (sprite == null) {
			logger.warn("Unable to find sprite: {}", name);
			sprite = sprites.get(TextureType.NONE.name());
		}

		return sprite;
	}

	public static SpriteSheet getSpriteSheet(TextureType textureType) throws ResourceNotFound, IOException {
		SpriteSheet sheet = spriteSheets.get(textureType);
		if (sheet == null) {
			logger.warn("Unable to find sprite sheet: {}", textureType);
			throw new ResourceNotFound("Sprite sheet not found: " + textureType);
		}

		return sheet;
	}
}
