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

import imgui.ImGui;

public class SpriteRenderer extends Component {
	private Vector3f positionOffset;
	private int width;
	private int height;
	private Vector3f widthDirection;
	private Vector3f heightDirection;
	private Color color;
	private ShaderType shader;
	private boolean isChanged;
	private Sprite sprite;
	private Transform lastTransform;

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

	@Override
	public void ui() {
		float[] uiColor = color.rgba();
		if (ImGui.colorPicker4("Color picker: ", uiColor)) {
			// color.rgba(uiColor);
			isChanged = true;
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

	public void getVertexArray(float[] vertices, int offset, int textureIndex) {
		Vector3f center = owner.transform().position().add(positionOffset, new Vector3f());
		Vector3f p1 = new Vector3f(center);
		Vector3f p2 = new Vector3f(center);
		Vector3f p3 = new Vector3f(center);
		Vector3f p4 = new Vector3f(center);
		Vector3f scaledWidthDirection = widthDirection.mul(owner.transform().scale(), new Vector3f());
		Vector3f scaledHeightDirection = heightDirection.mul(owner.transform().scale(), new Vector3f());

		p1.add(scaledWidthDirection.mul(-width / 2f, new Vector3f()));
		p2.add(scaledWidthDirection.mul(width / 2f, new Vector3f()));
		p4 = p1.add(scaledHeightDirection.mul(height, new Vector3f()), new Vector3f());
		p3 = p2.add(scaledHeightDirection.mul(height, new Vector3f()), new Vector3f());
		
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
