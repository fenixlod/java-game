package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.Shader;

public class TestScene extends Scene {
	private Shader defaultShader;
	private float[] vertexArray = {
			//Position				//Color
			0.5f, -0.5f, 0.0f,		1.0f, 0.0f, 0.0f, 1.0f, //Bottom right
		   -0.5f,  0.5f, 0.0f,		0.0f, 1.0f, 0.0f, 1.0f, //Top left
			0.5f,  0.5f, 0.0f,		0.0f, 0.0f, 1.0f, 1.0f, //Top right
		   -0.5f, -0.5f, 0.0f,		1.0f, 1.0f, 0.0f, 1.0f, //Bottom left
	};

	// IMPORTANT - must be in CCW order
	private int[] elementArray = {
			/*
			 		x (1)		x (2)
			 		
			 		
			 		x (3)		x (0)
			  
			 */
			2, 1, 0,
			0, 1, 3

	};

	private int vertexArrayObjectID;
	private int vertexBufferObjectID;
	private int elementBufferObjectID;

	@Override
	public void init(GameInstance game) {
		super.init(game);
		game.window().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		defaultShader = new Shader();
		defaultShader.compile();
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
		int vertexSizeInBytes = (positionSize + colorSize) * Float.BYTES;

		glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeInBytes, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionSize * Float.BYTES);
		glEnableVertexAttribArray(1);
	}

	@Override
	public void update(float deltaTime, GameInstance game) {
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		// Bind shader program
		defaultShader.use();

		// Bind the VAO that we are using
		glBindVertexArray(vertexArrayObjectID);

		// Enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

		// Unbind everithing
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		glUseProgram(0);
	}
}
