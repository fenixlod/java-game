package com.lunix.javagame.engine.graphic;

public class Color {
	private float[] rgba = new float[4];

	public static final Color white = new Color(1f, 1f, 1f, 1f);
	public static final Color lightGray = new Color(0.75f, 0.75f, 0.75f, 1f);
	public static final Color gray = new Color(0.5f, 0.5f, 0.5f, 1f);
	public static final Color darkGray = new Color(0.25f, 0.25f, 0.25f, 1f);
	public static final Color black = new Color(0f, 0f, 0f, 1f);
	public static final Color red = new Color(1f, 0f, 0f, 1f);
	public static final Color pink = new Color(1f, 0.69f, 0.69f, 1f);
	public static final Color orange = new Color(1f, 0.78f, 0f, 1f);
	public static final Color yellow = new Color(1f, 1f, 0f, 1f);
	public static final Color green = new Color(0f, 1f, 0f, 1f);
	public static final Color magenta = new Color(1f, 0f, 1f, 1f);
	public static final Color cyan = new Color(0f, 1f, 1f, 1f);
	public static final Color blue = new Color(0f, 0f, 1f, 1f);

	public Color() {
		this(0f, 0f, 0f, 1f);
	}

	public Color(float red, float green, float blue, float alpha) {
		rgba(red, green, blue, alpha);
	}

	public Color(Color color) {
		rgba(color);
	}

	public Color r(float red) {
		this.rgba[0] = red;
		return this;
	}

	public Color g(float green) {
		this.rgba[1] = green;
		return this;
	}

	public Color b(float blue) {
		this.rgba[2] = blue;
		return this;
	}

	public Color a(float alpha) {
		this.rgba[3] = alpha;
		return this;
	}

	public Color rgba(float red, float green, float blue, float alpha) {
		this.rgba[0] = red;
		this.rgba[1] = green;
		this.rgba[2] = blue;
		this.rgba[3] = alpha;
		return this;
	}

	public Color rgba(float[] color) {
		if (color.length != 4)
			throw new IllegalArgumentException("Color object require 4 float values");

		this.rgba[0] = color[0];
		this.rgba[1] = color[1];
		this.rgba[2] = color[2];
		this.rgba[3] = color[3];
		return this;
	}

	public Color rgba(Color color) {
		return rgba(color.r(), color.g(), color.b(), color.a());
	}

	public float r() {
		return this.rgba[0];
	}

	public float g() {
		return this.rgba[1];
	}

	public float b() {
		return this.rgba[2];
	}

	public float a() {
		return this.rgba[3];
	}

	public float[] rgba() {
		return this.rgba;
	}

	@Override
	public String toString() {
		return "Color [r=" + rgba[0] + ", g=" + rgba[1] + ", b=" + rgba[2] + ", a=" + rgba[3] + "]";
	}

}
