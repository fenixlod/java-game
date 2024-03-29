package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.springframework.core.io.ClassPathResource;

import com.lunix.javagame.engine.enums.TextureType;

public class Texture {
	private static final Logger logger = LogManager.getLogger(Texture.class);
	private final String filePath;
	private final TextureType type;
	private boolean loaded;
	private int width;
	private int height;

	private transient int textureID;

	public Texture() {
		filePath = "";
		type = TextureType.NONE;
	}

	public Texture(TextureType type, String filePath) {
		this.type = type;
		this.filePath = filePath;
		loaded = false;
		width = 0;
		height = 0;
	}

	public Texture(int width, int height) {
		filePath = "";
		type = TextureType.FRAMEBUFFER;
		// Generate texture on GPU
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
	}

	public void load() throws IOException {
		if (loaded)
			return;

		Path resourcePath = new ClassPathResource(filePath).getFile().toPath();

		// Generate texture on GPU
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.textureID);
		// Set texture parameters
		// Strech the texture if needed
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		// Blur when streching
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);

		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(resourcePath.toString(), width, height, channels, 0);
		if (image != null) {
			this.width = width.get(0);
			this.height = height.get(0);

			if(channels.get(0) == 3)
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
			else
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		} else {
			logger.error("Cannot load image: {}", filePath);
			throw new IOException("Cannot load image: " + filePath);
		}

		// Free the memory
		stbi_image_free(image);
		loaded = true;
	}

	public void bind(int slot) {
		if (!loaded) {
			logger.error("Texture: {} not loaded!", type);
			return;
		}
		// Set the current texture slot to slot number
		glActiveTexture(GL_TEXTURE0 + slot);
		// Bind texture to the current slot
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int id() {
		return textureID;
	}

	public TextureType type() {
		return type;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public boolean isLoaded() {
		return loaded;
	}
}
