package com.lunix.javagame.engine.graphic.objects;


import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Shader;

public class ScreenElement {
	private Shape shape;
	private Shader shader = new Shader();
	private Color color = Color.black;

	public ScreenElement shape(Shape shape) {
		this.shape = shape;
		return this;
	}

	public ScreenElement color(Color color) {
		this.color = color;
		return this;
	}

	public ScreenElement shader(Shader shader) {
		this.shader = shader;
		return this;
	}

	public void draw() {
		float[] vertexArray = shape.getVertexArray(color);
		int[] elementArray = shape.getElementArray();
		int vertexArrayObjectID, vertexBufferObjectID, elementBufferObjectID;

		// Generate VAO, VBO and EBO buffer objects and send them to the GPU
		vertexArrayObjectID = glGenVertexArrays();
		glBindVertexArray(vertexArrayObjectID);

		// Create float buffer of vertices
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();

		// Create VBO upload the vertex buffer
		vertexBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		// Create indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();

		elementBufferObjectID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		// Add the vertex attribute pointers
		int positionSize = 3;
		int colorSize = 4;
		int textureSize = 2;
		int vertexSizeInBytes = (positionSize + colorSize + textureSize) * Float.BYTES;

		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeInBytes, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionSize * Float.BYTES);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, textureSize, GL_FLOAT, false, vertexSizeInBytes,
				(positionSize + colorSize) * Float.BYTES);
		glEnableVertexAttribArray(2);

		// Bind shader program
		shader.use();

		// Bind the VAO that we are using
		glBindVertexArray(vertexArrayObjectID);

		// Enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

		// Unbind everithing
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glUseProgram(0);
	}
}
