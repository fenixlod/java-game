package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FrameBuffer {
	private static final Logger logger = LogManager.getLogger(Texture.class);
	private int fboID;
	private Texture texture;

	public FrameBuffer(int width, int height) throws IOException {
		// Generate framebuffer
		this.fboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, this.fboID);

		// Create the texture to render the data and attach it to our framebuffer
		this.texture = new Texture(width, height);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.id(), 0);

		// Create renderbuffer store the depth info
		int rboID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			logger.error("Error the framebuffer is not compleate");
			throw new IOException("Error the framebuffer is not compleatee");
		}

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int fboID() {
		return this.fboID;
	}

	public Texture texture() {
		return this.texture;
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, this.fboID);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
}
