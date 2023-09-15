package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector2i;

public class PickingTexture {
	private int textureID;
	private int fboID;
	private int depthTexture;

	public boolean init(int width, int height) throws Exception {
		// Generate framebuffer
		this.fboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, this.fboID);

		// Create the texture to render the data and attach it to our framebuffer
		this.textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.textureID, 0);

		// Create the texture to store depthbuffer
		// glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		this.depthTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.depthTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depthTexture, 0);

		// Disable the reading
		glReadBuffer(GL_NONE);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new Exception("Error the framebuffer is not compleatee");
		}

		// Unbind the texture and frame buffer
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return true;
	}

	public void enableWrithing() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.fboID);
	}

	public void disableWrithing() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}

	public long readPixel(Vector2i position) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, this.fboID);
		glReadBuffer(GL_COLOR_ATTACHMENT0);
		float pixels[] = new float[3];
		glReadPixels(position.x, position.y, 1, 1, GL_RGB, GL_FLOAT, pixels);
		return ((long) pixels[0]) - 1;
	}
}
