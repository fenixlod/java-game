package com.lunix.javagame.engine;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.ResourceConfigs;
import com.lunix.javagame.configs.ResourceConfigs.SoundData;
import com.lunix.javagame.configs.ResourceConfigs.SpriteSheetData;
import com.lunix.javagame.engine.audio.Sound;
import com.lunix.javagame.engine.enums.ResourceType;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.SoundType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.graphic.Texture;
import com.lunix.javagame.engine.graphic.TextureSheet;

@Component
public class ResourcePool {
	private static final Logger logger = LogManager.getLogger(ResourcePool.class);
	private final ResourceConfigs resourceConfig;
	private static final Map<ShaderType, Shader> shaders;
	private static final Map<TextureType, TextureSheet> textures;
	private static final Map<String, Sprite> sprites;
	private static final Map<SoundType, Sound> sounds;

	static {
		shaders = new HashMap<>();
		textures = new HashMap<>();
		sprites = new HashMap<>();
		sounds = new HashMap<>();
	}

	public ResourcePool(ResourceConfigs resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void init() throws IOException, ResourceNotFound {
		initShaders(resourceConfig.shaders());
		initTextures(resourceConfig.textures());
		initSounds(resourceConfig.sounds());
	}

	private void initShaders(Map<ShaderType, String> shadersToLoad) {
		logger.info("Loading shaders...");
		for (Entry<ShaderType, String> entry : shadersToLoad.entrySet()) {
			logger.debug("Loading shader: {}", entry.getValue());
			Shader shader = new Shader(entry.getKey(), entry.getValue());
			shaders.put(entry.getKey(), shader);
		}
	}

	private void initTextures(Map<TextureType, SpriteSheetData> seetData) {
		logger.info("Loading textures...");
		for (Entry<TextureType, SpriteSheetData> entry : seetData.entrySet()) {
			logger.debug("Loading texture: {}", entry.getValue().path());
			TextureSheet sSheet = new TextureSheet(entry.getKey(), entry.getValue());
			textures.put(entry.getKey(), sSheet);
		}
		sprites.put(TextureType.NONE.name(), new Sprite());
	}

	private void initSounds(Map<SoundType, SoundData> soundData) {
		logger.info("Loading sounds...");
		for (Entry<SoundType, SoundData> entry : soundData.entrySet()) {
			logger.debug("Loading sound: {}", entry.getValue().path());
			Sound sound = new Sound(entry.getValue().path(), entry.getValue().loops());
			sounds.put(entry.getKey(), sound);
		}
		sounds.put(SoundType.NONE, new Sound());
	}

	public static void loadResources(ResourceType... resources) throws ResourceNotFound, IOException {
		for (ResourceType resource : resources) {
			if (resource instanceof ShaderType shader) {
				getShader(shader);
			} else if (resource instanceof TextureType texture) {
				getTexture(texture);
			} else if (resource instanceof SoundType sound) {
				getSound(sound);
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
		TextureSheet sheet = textures.get(textureType);
		if (sheet == null) {
			logger.warn("Unable to find texture: {}", textureType);
			throw new ResourceNotFound("Texture not found: " + textureType);
		}

		if (!sheet.texture().isLoaded()) {
			sheet.load();
			int idx = 0;
			for (Sprite s : sheet.sprites()) {
				sprites.put(s.texture().name() + "_" + idx++, s);
			}
		}
		return sheet.texture();
	}

	public static Sprite getSprite(TextureType type) {
		return getSprite(type.name() + "_0");
	}

	public static Sprite getSprite(TextureType type, int index) {
		return getSprite(type.name() + "_" + index);
	}

	public static Sprite getSprite(String name) {
		Sprite sprite = sprites.get(name);
		if (sprite == null) {
			logger.error("Unable to find sprite: {}", name);
			sprite = sprites.get(TextureType.NONE.name());
		}

		return sprite;
	}

	public static List<Sprite> getSprites(TextureType textureType) {
		TextureSheet sheet = textures.get(textureType);
		if (sheet == null) {
			logger.warn("Unable to find texture sheet: {}", textureType);
			return Collections.emptyList();
		}

		return sheet.sprites();
	}

	public static Map<String, Sprite> sprites() {
		return sprites;
	}

	public static Sound getSound(SoundType soundName) throws ResourceNotFound, IOException {
		Sound sound = sounds.get(soundName);
		if (sound == null) {
			logger.warn("Unable to find sound: {}", soundName);
			throw new ResourceNotFound("Sound not found: " + soundName);
		}

		sound.load();
		return sound;
	}

	public static Map<SoundType, Sound> sounds() {
		return sounds;
	}
}
