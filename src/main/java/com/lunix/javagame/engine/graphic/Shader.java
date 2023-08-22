package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shader {
	private static final Logger logger = LogManager.getLogger(Shader.class);
	private String filePath;
	private String vertexShaderSource;
	private String fragmentShaderSource;
	private int programID;

	public Shader(Path filePath) throws IOException {
		this.filePath = filePath.toString();
		String source = new String(Files.readAllBytes(filePath));
		String[] sources = source.split("#(type )([a-zA-Z]+)");

		int typeLineStart = source.indexOf("#type ") + 6;
		int typeLineEnd = source.indexOf("\n");
		String firstType = source.substring(typeLineStart, typeLineEnd).trim();

		typeLineStart = source.indexOf("#type ", typeLineEnd) + 6;
		typeLineEnd = source.indexOf("\n", typeLineStart);
		String secondType = source.substring(typeLineStart, typeLineEnd).trim();

		if (firstType.equals("vertex")) {
			vertexShaderSource = sources[1];
		} else if (firstType.equals("fragment")) {
			fragmentShaderSource = sources[1];
		} else {
			throw new IOException("Unknown shader type: " + firstType + " in: " + this.filePath);
		}

		if (secondType.equals("vertex")) {
			vertexShaderSource = sources[2];
		} else if (secondType.equals("fragment")) {
			fragmentShaderSource = sources[2];
		} else {
			throw new IOException("Unknown shader type: " + secondType + " in: " + this.filePath);
		}
	}

	public void compile() {
		// Load and compile the vertex shader
		int vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(vertexShaderID, vertexShaderSource);
		// Compile the vertex shader
		glCompileShader(vertexShaderID);
		// Check for compile errors
		int status = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			int errorMsgLength = glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH);
			String errorMessage = glGetShaderInfoLog(vertexShaderID, errorMsgLength);
			logger.error(errorMessage);
			throw new RuntimeException("Unable to compile vertex shader: " + this.filePath);
		}

		// Load and compile the fragment shader
		int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		// Pass the shader source to the GPU
		glShaderSource(fragmentShaderID, fragmentShaderSource);
		// Compile the fragment shader
		glCompileShader(fragmentShaderID);
		// Check for compile errors
		status = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			int errorMsgLength = glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH);
			String errorMessage = glGetShaderInfoLog(fragmentShaderID, errorMsgLength);
			logger.error(errorMessage);
			throw new RuntimeException("Unable to compile fragment shader: " + this.filePath);
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
			throw new RuntimeException("Unable to link shaders: " + this.filePath);
		}
	}

	public void use() {
		glUseProgram(programID);
	}

	public void detach() {
		glUseProgram(0);
	}
}
