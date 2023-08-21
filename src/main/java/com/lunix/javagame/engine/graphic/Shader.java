package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shader {
	private static final Logger logger = LogManager.getLogger(Shader.class);

	private String vertexShader;
	private String fragmentShader;
	private int vertexShaderID;
	private int fragmentShaderID;
	private int programID;

	public Shader() {
		vertexShader = """
				#version 330 core

				layout (location=0) in vec3 position;
				layout (location=1) in vec4 color;

				out vec4 fragmentColor;

				void main()
				{
					fragmentColor = color;
					gl_Position = vec4(position, 1.0);
				}
				""";

		fragmentShader = """
				#version 330 core

				in vec4 fragmentColor;

				out vec4 color;

				void main()
				{
					color = fragmentColor;
				}
				""";
	}

	public void compile() {
		// Load and compile the vertex shader
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(vertexShaderID, vertexShader);
		// Compile the vertex shader
		glCompileShader(vertexShaderID);
		// Check for compile errors
		int status = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			int errorMsgLength = glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH);
			String errorMessage = glGetShaderInfoLog(vertexShaderID, errorMsgLength);
			logger.error(errorMessage);
			throw new RuntimeException("Unable to compile vertex shader");
		}

		// Load and compile the fragment shader
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(fragmentShaderID, fragmentShader);
		// Compile the fragment shader
		glCompileShader(fragmentShaderID);
		// Check for compile errors
		status = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			int errorMsgLength = glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH);
			String errorMessage = glGetShaderInfoLog(fragmentShaderID, errorMsgLength);
			logger.error(errorMessage);
			throw new RuntimeException("Unable to compile fragment shader");
		}

		// Link shaders and check for errors
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		glLinkProgram(programID);

		// Check for linking errors
		status = glGetProgrami(programID, GL_LINK_STATUS);
		if (status == GL_FALSE) {
			int errorMsgLength = glGetProgrami(programID, GL_INFO_LOG_LENGTH);
			String errorMessage = glGetProgramInfoLog(programID, errorMsgLength);
			logger.error(errorMessage);
			throw new RuntimeException("Unable to link shaders");
		}
	}

	public void use() {
		glUseProgram(programID);
	}
}
