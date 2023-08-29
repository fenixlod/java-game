package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import com.lunix.javagame.engine.enums.ShaderType;

public class Shader {
	private static final Logger logger = LogManager.getLogger(Shader.class);
	private final String filePath;
	private String vertexShaderSource;
	private String fragmentShaderSource;
	private int programID;
	private boolean inUse;
	private final ShaderType type;
	private boolean compiled;

	public Shader(ShaderType type, Path filePath) throws IOException {
		this.type = type;
		this.filePath = filePath.toString();
		this.compiled = false;

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
		if (compiled)
			return;

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

		compiled = true;
	}

	public void use() {
		if (!inUse) {
			glUseProgram(programID);
			inUse = true;
		}
	}

	public void detach() {
		glUseProgram(0);
		inUse = false;
	}

	public void uploadMat4f(String variableName, Matrix4f mat) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
		mat.get(matrixBuffer);
		glUniformMatrix4fv(varLocation, false, matrixBuffer);
	}

	public void uploadMat3f(String variableName, Matrix3f mat) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);
		mat.get(matrixBuffer);
		glUniformMatrix3fv(varLocation, false, matrixBuffer);
	}

	public void uploadVect4f(String variableName, Vector4f vect) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform4f(varLocation, vect.x, vect.y, vect.z, vect.w);
	}

	public void uploadVect3f(String variableName, Vector3f vect) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform3f(varLocation, vect.x, vect.y, vect.z);
	}

	public void uploadVect2f(String variableName, Vector2f vect) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform2f(varLocation, vect.x, vect.y);
	}

	public void uploadFloat(String variableName, Float value) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform1f(varLocation, value);
	}

	public void uploadInt(String variableName, int value) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform1i(varLocation, value);
	}

	public void uploadTexture(String variableName, int slot) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform1i(varLocation, slot);
	}
	
	public void uploadBoolean(String variableName, boolean value) {
		int varLocation = glGetUniformLocation(programID, variableName);
		use();
		glUniform1i(varLocation, value ? 1 : 0);
	}

	public ShaderType type() {
		return type;
	}
}
