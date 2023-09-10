package com.lunix.javagame.engine.util;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.exception.ResourceNotFound;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Line;
import com.lunix.javagame.engine.graphic.Shader;

@Component
public class Debugger {
	private static final Logger logger = LogManager.getLogger(Debugger.class);
	private static boolean active;
	private static int MAX_LINES = 500;
	private static List<Line> lines;
	// 7 floats per vertex, 2 vertices per line
	private static float[] vertexArray;
	private static Shader shader;
	private static int vboID;
	private static int vaoID;
	private static boolean started;

	public Debugger(@Value("${spring.profiles.active}") String profile) {
		active = profile.equals("dev");
		lines = new ArrayList<>();
		started = false;
		vertexArray = new float[7 * 2 * MAX_LINES];
	}

	public static void init() throws ResourceNotFound, IOException {
		shader = ResourcePool.getShader(ShaderType.DEBUG);
	}

	public static void display(boolean show, String message, Object... objects) {
		if (active && show)
			logger.debug(message, objects);
	}

	public static void display(boolean show, Object... objects) {
		if (active && show)
			logger.debug("{}", objects);
	}

	public static void start() {
		if (!active)
			return;

		// Generate vao
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for the vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);


		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);

		glLineWidth(2);
	}

	public static void beginFrame() {
		if (!active)
			return;

		if (!started) {
			start();
			started = true;
		}

		// Remove dead lines
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).beginFrame() < 0) {
				lines.remove(i);
				i--;
			}
		}
	}

	public static void draw() {
		if (!active)
			return;

		if (lines.size() <= 0)
			return;

		int idx = 0;
		for (Line l : lines) {
			for (int i = 0; i < 2; i++) {
				Vector3f p = i == 0 ? l.start() : l.end();
				Color col = l.color();

				// Load position
				vertexArray[idx++] = p.x;
				vertexArray[idx++] = p.y;
				vertexArray[idx++] = p.z;
				// Load color
				vertexArray[idx++] = col.r();
				vertexArray[idx++] = col.g();
				vertexArray[idx++] = col.b();
				vertexArray[idx++] = col.a();
			}
		}

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 7 * 2));

		// use the shader
		shader.use();
		shader.uploadMat4f("projMat", GameInstance.get().camera().projectionMatrix());
		shader.uploadMat4f("viewMat", GameInstance.get().camera().viewMatrix());

		// Bind the VAO that we are using
		glBindVertexArray(vaoID);

		// Enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		// Draw the batch
		glDrawArrays(GL_LINES, 0, lines.size() * 7 * 2);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);

		shader.detach();
	}

	public static void addLine(Vector3f start, Vector3f end, Color color, int lifetime) {
		if (lines.size() > MAX_LINES)
			return;

		lines.add(new Line(start, end, color, lifetime));
	}

	public static void addLine(Vector3f start, Vector3f end, Color color) {
		addLine(start, end, color, Integer.MAX_VALUE);
	}

	public static void addLine(Vector3f start, Vector3f end) {
		addLine(start, end, Color.black(), Integer.MAX_VALUE);
	}

	public static void drawAxis(boolean show) {
		if (!show)
			return;

		addLine(VectorUtil.X().mul(-10_000), VectorUtil.X().mul(10_000), Color.blue());
		addLine(VectorUtil.Y().mul(-10_000), VectorUtil.Y().mul(10_000), Color.red());
		addLine(VectorUtil.Z().mul(-10_000), VectorUtil.Z().mul(10_000), Color.green());
	}
}
