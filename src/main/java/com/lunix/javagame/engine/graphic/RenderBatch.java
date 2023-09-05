package com.lunix.javagame.engine.graphic;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;

public class RenderBatch {
	//Vertex
	//Pos						Color							Texture UV		Texture ID
	//float, float, float,		float, float, float, float,		float, float,	float
	private final int POSITION_SIZE = 3;
	private final int COLOR_SIZE = 4;
	private final int TEXTURE_COORDS_SIZE = 2;
	private final int TEXTURE_ID_SIZE = 1;

	private final int POSITION_OFFSET = 0;
	private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
	private final int TEXTURE_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEXTURE_ID_OFFSET = TEXTURE_COORDS_OFFSET + TEXTURE_COORDS_SIZE * Float.BYTES;

	private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_COORDS_SIZE + TEXTURE_ID_SIZE;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private List<SpriteRenderer> sprites;
	private float[] vertices;
	private int[] textureSlots;
	private Map<TextureType, Integer> textures;
	private int vaoID;
	private int vboID;
	private int maxBatchSize;
	private Shader shader;

	public RenderBatch(int maxBatchSize, ShaderType shaderType) throws ResourceNotFound {
		this.shader = ResourcePool.getShader(shaderType);
		this.sprites = new LinkedList<>();
		this.maxBatchSize = maxBatchSize;
		this.textures = new HashMap<>();

		// 4 vertices quads
		this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
		this.textureSlots = IntStream.range(0, 32).toArray();
	}

	public void start() {
		// Generate and bind Vertex Array Object
		this.vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for the vertices
		this.vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
		glBufferData(GL_ARRAY_BUFFER, this.vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

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

		glVertexAttribPointer(2, TEXTURE_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDS_OFFSET);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}

	private int[] generateIndices() {
		// 6 indices per quad (3 per triangle)
		// 0, 1, 2, 2, 3, 0        4, 5, 6, 6, 7, 4       ...
		int[] elements = new int[6 * this.maxBatchSize];
		for (int i = 0; i < this.maxBatchSize; i++) {
			elements[i * 6] = 4 * i + 0;
			elements[i * 6 + 1] = 4 * i + 1;
			elements[i * 6 + 2] = 4 * i + 2;
			elements[i * 6 + 3] = 4 * i + 2;
			elements[i * 6 + 4] = 4 * i + 3;
			elements[i * 6 + 5] = 4 * i + 0;
		}

		return elements;
	}

	public void render() throws Exception {
		// Update positions
		boolean update = false;
		int index = 0;
		for (SpriteRenderer spr : this.sprites) {
			if (spr.isChanged()) {
				spr.isChanged(false);
				if (loadVertexProperties(index, spr))
					update = true;
			}
			index++;
		}

		if (update) {
			// Re-buffer all data
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		}

		this.shader.use();
		this.shader.uploadMat4f("projMat", GameInstance.get().camera().projectionMatrix());
		this.shader.uploadMat4f("viewMat", GameInstance.get().camera().viewMatrix());

		for (Entry<TextureType, Integer> entry : this.textures.entrySet()) {
			ResourcePool.getTexture(entry.getKey()).bind(entry.getValue());
		}

		shader.uploadIntArray("textures", this.textureSlots);

		// Bind the VAO that we are using
		glBindVertexArray(this.vaoID);

		// Enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		glDrawElements(GL_TRIANGLES, this.sprites.size() * 6, GL_UNSIGNED_INT, 0);

		// Unbind everithing
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);

		Texture.unbind();
		this.shader.detach();
	}

	public void addSprite(SpriteRenderer sprite) {
		// Get index and add grenderObject
		int index = this.sprites.size();
		this.sprites.add(sprite);

		if (sprite.texture() != TextureType.NONE) {
			if (this.textures.get(sprite.texture()) == null) {
				this.textures.put(sprite.texture(), this.textures.size() + 1);
			}
		}

		// Add properties to local vertices array
		loadVertexProperties(index, sprite);
	}

	private boolean loadVertexProperties(int index, SpriteRenderer sprite) {
		// Find offset within array (4 vertices per sprite)
		int offset = 4 * index * VERTEX_SIZE;
		int textureIndex = 0;

		if (sprite.texture() != TextureType.NONE) {
			Integer idx = this.textures.get(sprite.texture());
			if (idx != null)
				textureIndex = idx;
			else if (haveTextureRoom()) {
				textureIndex = this.textures.size() + 1;
				this.textures.put(sprite.texture(), textureIndex);
			} else {
				// Use textureIndex = 0, reserved no texture
			}
		}

		// Add vertices with appropriate attributes
		sprite.getVertexArray(vertices, offset, textureIndex);
		return true;
	}

	public boolean haveRoom() {
		return this.sprites.size() < this.maxBatchSize;
	}

	public boolean haveTextureRoom() {
		return textures.size() < 32;
	}

	public ShaderType shader() {
		return this.shader.type();
	}

	public boolean hasTexture(TextureType texture) {
		return this.textures.get(texture) != null;
	}
}
