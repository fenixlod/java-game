package com.lunix.javagame.engine.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.util.VectorUtil;

public class SpriteRenderer extends Component {
	private Vector3f positionOffset;
	private int width;
	private int height;
	private Vector3f widthDirection;
	private Vector3f heightDirection;
	private Color color;
	private ShaderType shader;
	private boolean isStatic;
	private Sprite sprite;

	public SpriteRenderer(int width, int height) {
		this.height = height;
		this.width = width;
		this.color = Color.white;
		this.widthDirection = VectorUtil.X();
		this.heightDirection = VectorUtil.Z();
		this.shader = ShaderType.DEFAULT;
		this.positionOffset = new Vector3f();
		this.isStatic = false;
		this.sprite = new Sprite();
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void update(float deltaTime) {
	}

	public SpriteRenderer offset(Vector3f positionOffset) {
		this.positionOffset = positionOffset;
		return this;
	}

	public SpriteRenderer width(int width) {
		this.width = width;
		return this;
	}

	public SpriteRenderer height(int height) {
		this.height = height;
		return this;
	}

	public SpriteRenderer widthDirection(Vector3f widthDirection) {
		this.widthDirection = widthDirection;
		return this;
	}

	public SpriteRenderer heightDirection(Vector3f heightDirection) {
		this.heightDirection = heightDirection;
		return this;
	}

	public SpriteRenderer color(Color color) {
		this.color = color;
		return this;
	}

	public SpriteRenderer shader(ShaderType shader) {
		this.shader = shader;
		return this;
	}

	public SpriteRenderer isStatic(boolean isStatic) {
		this.isStatic = isStatic;
		return this;
	}

	public SpriteRenderer sprite(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	public ShaderType shader() {
		return this.shader;
	}

	public void getVertexArray(float[] vertices, int offset, int textureIndex) {
		Vector3f center = owner.transform().position().add(positionOffset, new Vector3f());
		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);
		Vector3f p4 = new Vector3f(center);
		Vector3f scaledWidthDirection = widthDirection.mul(owner.transform().scale(), new Vector3f());
		Vector3f scaledHeightDirection = heightDirection.mul(owner.transform().scale(), new Vector3f());

		p1.add(scaledWidthDirection.mul(-width / 2f, new Vector3f()))
				.add(scaledHeightDirection.mul(-height / 2f, new Vector3f()));
		p2.add(scaledWidthDirection.mul(width / 2f, new Vector3f()))
				.add(scaledHeightDirection.mul(-height / 2f, new Vector3f()));
		p3.add(scaledWidthDirection.mul(width / 2f, new Vector3f()))
				.add(scaledHeightDirection.mul(height / 2f, new Vector3f()));
		p4.add(scaledWidthDirection.mul(-width / 2f, new Vector3f()))
				.add(scaledHeightDirection.mul(height / 2f, new Vector3f()));
		
		offset = setVertexInArray(vertices, offset, p1, sprite.textureCoords()[0], textureIndex);
		offset = setVertexInArray(vertices, offset, p2, sprite.textureCoords()[1], textureIndex);
		offset = setVertexInArray(vertices, offset, p3, sprite.textureCoords()[2], textureIndex);
		offset = setVertexInArray(vertices, offset, p4, sprite.textureCoords()[3], textureIndex);
	}

	private int setVertexInArray(float[] vertices, int offset, Vector3f position, Vector2f uv, int textureIndex) {
		vertices[offset++] = position.x();
		vertices[offset++] = position.y();
		vertices[offset++] = position.z();

		vertices[offset++] = color.r();
		vertices[offset++] = color.g();
		vertices[offset++] = color.b();
		vertices[offset++] = color.a();

		vertices[offset++] = uv.x;
		vertices[offset++] = uv.y;

		vertices[offset++] = textureIndex;
		return offset;
	}

	public TextureType texture() {
		return sprite.texture();
	}
}
