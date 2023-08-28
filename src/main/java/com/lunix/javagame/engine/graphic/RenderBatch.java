package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Resources;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.util.VectorUtil;

public class RenderBatch {
	//Vertex
	//Pos						Color
	//float, float, float		float, float, float, float		
	private final int POSITION_SIZE = 3;
	private final int COLOR_SIZE = 4;
	private final int POSITION_OFFSET = 0;
	private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
	private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private SpriteRenderer[] sprites;
	private int countSprites;
	private float[] vertices;
	private int vaoID;
	private int vboID;
	private int maxBatchSize;
	private Shader shader;
	private boolean staticImage;

	public RenderBatch(int maxBatchSize, ShaderType shaderType) {
		this.shader = Resources.getShader(shaderType);
		this.sprites = new SpriteRenderer[maxBatchSize];
		this.maxBatchSize = maxBatchSize;

		// 4 vertices quads
		this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
		this.countSprites = 0;
	}

	public void start() {
		// Generate and bind Vertex Array Object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for the vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, staticImage ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW);

		// Create and upload the indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		// Enable the buffer attribute pointers
		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);
	}

	private int[] generateIndices() {
		// 6 indices per quad (3 per triangle)
		// 0, 1, 2, 2, 3, 0        4, 5, 6, 6, 7, 4       ...
		int[] elements = new int[6 * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			elements[i * 6] = 4 * i + 0;
			elements[i * 6 + 1] = 4 * i + 1;
			elements[i * 6 + 2] = 4 * i + 2;
			elements[i * 6 + 3] = 4 * i + 2;
			elements[i * 6 + 4] = 4 * i + 3;
			elements[i * 6 + 5] = 4 * i + 0;
		}

		return elements;
	}

	public void render() {
		// Update positions
		for (int i = 0; i < countSprites; i++) {
			loadVertexProperties(i);
		}

		// Re-buffer all data
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		shader.use();
		shader.uploadMat4f("projMat", GameInstance.get().camera().getProjectionMatrix());
		Matrix4f view = GameInstance.get().camera().getViewMatrix();
		if (shader.type() == ShaderType.NO_PERSPECTIVE) {
			// TODO: Move this logic in the shader if possible
			//The idea is to set the camera to be perpendicular to the plane of the element,
			// this way it will be displayed without any perspective
			//System.out.println("----> \n" + view.toString(NumberFormat.getNumberInstance()));
			view.translate(GameInstance.get().camera().position());
			view.rotate((float) Math.toRadians(-45f), VectorUtil.X());
			view.translate(GameInstance.get().camera().position().mul(-1, new Vector3f()));
			//System.out.println("<---- \n" + view.toString(NumberFormat.getNumberInstance()));
		}
		shader.uploadMat4f("viewMat", view);
		shader.uploadBoolean("useTexture", false);

		// Bind the VAO that we are using
		glBindVertexArray(vaoID);

		// Enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, countSprites * 6, GL_UNSIGNED_INT, 0);

		// Unbind everithing
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		shader.detach();
	}

	public void addSprite(SpriteRenderer sprite) {
		// Get index and add grenderObject
		int index = this.countSprites;
		this.sprites[index] = sprite;
		this.countSprites++;

		// Add properties to local vertices array
		loadVertexProperties(index);
	}

	private void loadVertexProperties(int index) {
		SpriteRenderer sprite = this.sprites[index];

		// Find offset within array (4 vertices per sprite)
		int offset = 4 * index * VERTEX_SIZE;

		// Add vertices with appropriate attributes
		sprite.getVertexArray(vertices, offset);
	}

	public boolean haveRoom() {
		return countSprites < maxBatchSize;
	}

	public ShaderType shader() {
		return this.shader.type();
	}
}
