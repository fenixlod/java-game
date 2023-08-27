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

import com.lunix.javagame.engine.enums.TextureType;

public class Texture {
	private static final Logger logger = LogManager.getLogger(Texture.class);
	private final String filePath;
	private int textureID;
	private final TextureType type;

	public Texture(TextureType type, Path filePath) throws IOException {
		this.type = type;
		this.filePath = filePath.toString();
	}

	public void load() throws IOException {
		// Generate texture on GPU
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
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
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		if (image != null) {
			if(channels.get(0) == 3)
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			else
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		} else {
			throw new IOException("Cannot load image: " + filePath);
		}

		// Free the memory
		stbi_image_free(image);
	}

	public void bind(int slot) {
		// Set the current texture slot to slot number
		glActiveTexture(GL_TEXTURE0 + slot);
		// Bind texture to the current slot
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int id() {
		return this.textureID;
	}
}
