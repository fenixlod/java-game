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
	private boolean mirrorWidth;
	private boolean mirrorHeight;

	private transient Transform lastTransform;
	private transient boolean isChanged;

	public SpriteRenderer() {
		this(0, 0);
	}

	public SpriteRenderer(int width, int height) {
		this.height = height;
		this.width = width;
		color = Color.white();
		widthDirection = VectorUtil.viewX();
		heightDirection = VectorUtil.viewZ();
		shader = ShaderType.DEFAULT;
		positionOffset = new Vector3f();
		isChanged = true;
		sprite = new Sprite();
		rotate = 0;
	}

	@Override
	public void start() {
		super.start();
		lastTransform = owner.transform().copy();
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		if (!lastTransform.equals(owner.transform())) {
			lastTransform = owner.transform().copy();
			isChanged = true;
		}
	}

	public SpriteRenderer offset(Vector3f positionOffset) {
		if (!this.positionOffset.equals(positionOffset)) {
			this.positionOffset = positionOffset;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer width(int width) {
		if (this.width != width) {
			this.width = width;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer height(int height) {
		if (this.height != height) {
			this.height = height;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer widthDirection(Vector3f widthDirection) {
		if (!this.widthDirection.equals(widthDirection)) {
			this.widthDirection = widthDirection;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer heightDirection(Vector3f heightDirection) {
		if (!this.heightDirection.equals(heightDirection)) {
			this.heightDirection = heightDirection;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer color(Color color) {
		if (!this.color.equals(color)) {
			this.color = color;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer shader(ShaderType shader) {
		if (this.shader != shader) {
			this.shader = shader;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer sprite(Sprite sprite) {
		if (!this.sprite.equals(sprite)) {
			this.sprite = sprite;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer rotate(int rotate) {
		if (this.rotate != rotate) {
			this.rotate = rotate;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer mirrorWidth(boolean mirror) {
		if (this.mirrorWidth != mirror) {
			this.mirrorWidth = mirror;
			isChanged = true;
		}
		return this;
	}

	public SpriteRenderer mirrorHeight(boolean mirror) {
		if (this.mirrorHeight != mirror) {
			this.mirrorHeight = mirror;
			isChanged = true;
		}
		return this;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public SpriteRenderer isChanged(boolean isChanged) {
		this.isChanged = isChanged;
		return this;
	}

	public ShaderType shader() {
		return shader;
	}

	/**
	 * Transform this element to vertices for drawing.
	 * 
	 * @param vertices
	 * @param offset
	 * @param textureIndex
	 */
	public void getVertexArray(float[] vertices, int offset, int textureIndex) {
		Vector3f[] points = getBox();
		
		int bottomLeft, bottomRight, topRight, topLeft, tmp;
		bottomLeft = ((0 - rotate) % 4 + 4) % 4;
		bottomRight = ((1 - rotate) % 4 + 4) % 4;
		topRight = ((2 - rotate) % 4 + 4) % 4;
		topLeft = ((3 - rotate) % 4 + 4) % 4;
		
		if (mirrorWidth) {
			tmp = bottomLeft;
			bottomLeft = bottomRight;
			bottomRight = tmp;
			tmp = topRight;
			topRight = topLeft;
			topLeft = tmp;
		}

		if (mirrorHeight) {
			tmp = bottomLeft;
			bottomLeft = topLeft;
			topLeft = tmp;
			tmp = bottomRight;
			bottomRight = topRight;
			topRight = tmp;
		}
		
		Vector2f[] uvMap = new Vector2f[] { 
				sprite.textureCoords()[bottomLeft], sprite.textureCoords()[bottomRight],
				sprite.textureCoords()[topRight], sprite.textureCoords()[topLeft]
		};

		offset = setVertexInArray(vertices, offset, points[0], uvMap[0], textureIndex);
		offset = setVertexInArray(vertices, offset, points[1], uvMap[1], textureIndex);
		offset = setVertexInArray(vertices, offset, points[2], uvMap[2], textureIndex);
		offset = setVertexInArray(vertices, offset, points[3], uvMap[3], textureIndex);
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

		vertices[offset++] = owner.id() + 1;
		return offset;
	}

	public TextureType textureType() {
		return sprite.texture();
	}

	public Vector3f[] getBox() {
		Vector3f center = owner.transform().position().add(positionOffset, new Vector3f());
		Vector3f[] points = { new Vector3f(center), new Vector3f(center), new Vector3f(center), new Vector3f(center) };
		Vector3f scaledWidthDirection = widthDirection.mul(owner.transform().scale(), new Vector3f());
		Vector3f scaledHeightDirection = heightDirection.mul(owner.transform().scale(), new Vector3f());

		points[0].add(scaledWidthDirection.mul(-width / 2f, new Vector3f()));
		points[1].add(scaledWidthDirection.mul(width / 2f, new Vector3f()));
		points[3] = points[0].add(scaledHeightDirection.mul(height, new Vector3f()), new Vector3f());
		points[2] = points[1].add(scaledHeightDirection.mul(height, new Vector3f()), new Vector3f());
		return points;
	}

	public Vector3f widthDirection() {
		return widthDirection;
	}

	public Vector3f heightDirection() {
		return heightDirection;
	}

	public Color color() {
		return color;
	}

	/**
	 * All Components needs to implement this method. This value determine the order
	 * of execution of components within a game object. The lower the priority value
	 * = the sooner this component will be executed. Priority of 1 - first to
	 * execute, 1000 - last to execute.
	 * 
	 * @return
	 */
	public static int priority() {
		return 500;
	}

	public Vector3f positionOffset() {
		return positionOffset;
	}
}
