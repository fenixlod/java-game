package com.lunix.javagame.engine.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.Transform;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.util.VectorUtil;

public class SpriteRenderer extends Component {
	private Vector3f positionOffset;
	private Vector3f widthDirection;
	private Vector3f heightDirection;
	private int width;
	private int height;
	private Color color;
	private ShaderType shader;
	private Sprite sprite;
	private int rotate;

	private transient Transform lastTransform;
	private transient boolean isChanged;

	public SpriteRenderer() {
		this(0, 0);
	}

	public SpriteRenderer(int width, int height) {
		this.height = height;
		this.width = width;
		this.color = Color.white();
		this.widthDirection = VectorUtil.viewX();
		this.heightDirection = VectorUtil.viewZ();
		this.shader = ShaderType.DEFAULT;
		this.positionOffset = new Vector3f();
		this.isChanged = true;
		this.sprite = new Sprite();
		this.rotate = 0;
	}

	@Override
	public void start() {
		super.start();
		this.lastTransform = owner.transform().copy();
	}

	@Override
	public void update(float deltaTime) {
		if (!this.lastTransform.equals(owner.transform())) {
			this.lastTransform = owner.transform().copy();
			this.isChanged = true;
		}
	}

	public SpriteRenderer offset(Vector3f positionOffset) {
		if (!positionOffset.equals(this.positionOffset)) {
			this.positionOffset = positionOffset;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer width(int width) {
		if (width != this.width) {
			this.width = width;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer height(int height) {
		if (height != this.height) {
			this.height = height;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer widthDirection(Vector3f widthDirection) {
		if (!widthDirection.equals(this.widthDirection)) {
			this.widthDirection = widthDirection;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer heightDirection(Vector3f heightDirection) {
		if (!heightDirection.equals(this.heightDirection)) {
			this.heightDirection = heightDirection;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer color(Color color) {
		if (!color.equals(this.color)) {
			this.color = color;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer shader(ShaderType shader) {
		if (shader != this.shader) {
			this.shader = shader;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer sprite(Sprite sprite) {
		if (!sprite.equals(this.sprite)) {
			this.sprite = sprite;
			this.isChanged = true;
		}
		return this;
	}

	public SpriteRenderer rotate(int rotate) {
		if (this.rotate != rotate) {
			this.rotate = rotate;
			this.isChanged = true;
		}
		return this;
	}

	public boolean isChanged() {
		return this.isChanged;
	}

	public SpriteRenderer isChanged(boolean isChanged) {
		this.isChanged = isChanged;
		return this;
	}

	public ShaderType shader() {
		return this.shader;
	}

	/**
	 * Transform this element to vertices for drawing.
	 * 
	 * @param vertices
	 * @param offset
	 * @param textureIndex
	 */
	public void getVertexArray(float[] vertices, int offset, int textureIndex) {
		Vector3f center = this.owner.transform().position().add(this.positionOffset, new Vector3f());
		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);
		Vector3f p4 = new Vector3f(center);
		Vector3f scaledWidthDirection = this.widthDirection.mul(this.owner.transform().scale(), new Vector3f());
		Vector3f scaledHeightDirection = this.heightDirection.mul(this.owner.transform().scale(), new Vector3f());

		p1.add(scaledWidthDirection.mul(-this.width / 2f, new Vector3f()));
		p2.add(scaledWidthDirection.mul(this.width / 2f, new Vector3f()));
		p4 = p1.add(scaledHeightDirection.mul(this.height, new Vector3f()), new Vector3f());
		p3 = p2.add(scaledHeightDirection.mul(this.height, new Vector3f()), new Vector3f());
		
		Vector2f[] uvMap = new Vector2f[] { 
				this.sprite.textureCoords()[((0 - rotate) % 4 + 4) % 4],
				this.sprite.textureCoords()[((1 - rotate) % 4 + 4) % 4],
				this.sprite.textureCoords()[((2 - rotate) % 4 + 4) % 4],
				this.sprite.textureCoords()[((3 - rotate) % 4 + 4) % 4] 
		};
		
		offset = setVertexInArray(vertices, offset, p1, uvMap[0], textureIndex);
		offset = setVertexInArray(vertices, offset, p2, uvMap[1], textureIndex);
		offset = setVertexInArray(vertices, offset, p3, uvMap[2], textureIndex);
		offset = setVertexInArray(vertices, offset, p4, uvMap[3], textureIndex);
	}

	private int setVertexInArray(float[] vertices, int offset, Vector3f position, Vector2f uv, int textureIndex) {
		vertices[offset++] = position.x();
		vertices[offset++] = position.y();
		vertices[offset++] = position.z();

		vertices[offset++] = this.color.r();
		vertices[offset++] = this.color.g();
		vertices[offset++] = this.color.b();
		vertices[offset++] = this.color.a();

		vertices[offset++] = uv.x;
		vertices[offset++] = uv.y;

		vertices[offset++] = textureIndex;
		return offset;
	}

	public TextureType textureType() {
		return this.sprite.texture();
	}
}
