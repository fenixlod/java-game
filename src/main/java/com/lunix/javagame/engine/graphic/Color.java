package com.lunix.javagame.engine.graphic;

public class Color {
	private float[] rgba = new float[4];

	private static final Color white = new Color(1f, 1f, 1f, 1f);
	private static final Color lightGray = new Color(0.75f, 0.75f, 0.75f, 1f);
	private static final Color gray = new Color(0.5f, 0.5f, 0.5f, 1f);
	private static final Color darkGray = new Color(0.25f, 0.25f, 0.25f, 1f);
	private static final Color black = new Color(0f, 0f, 0f, 1f);
	private static final Color red = new Color(1f, 0f, 0f, 1f);
	private static final Color pink = new Color(1f, 0.69f, 0.69f, 1f);
	private static final Color orange = new Color(1f, 0.78f, 0f, 1f);
	private static final Color yellow = new Color(1f, 1f, 0f, 1f);
	private static final Color green = new Color(0f, 1f, 0f, 1f);
	private static final Color magenta = new Color(1f, 0f, 1f, 1f);
	private static final Color cyan = new Color(0f, 1f, 1f, 1f);
	private static final Color blue = new Color(0f, 0f, 1f, 1f);

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
		rgba[0] = red;
		return this;
	}

	public Color g(float green) {
		rgba[1] = green;
		return this;
	}

	public Color b(float blue) {
		rgba[2] = blue;
		return this;
	}

	public Color a(float alpha) {
		rgba[3] = alpha;
		return this;
	}

	public Color rgba(float red, float green, float blue, float alpha) {
		rgba[0] = red;
		rgba[1] = green;
		rgba[2] = blue;
		rgba[3] = alpha;
		return this;
	}

	public Color rgba(float[] color) {
		if (color.length != 4)
			throw new IllegalArgumentException("Color object require 4 float values");

		rgba[0] = color[0];
		rgba[1] = color[1];
		rgba[2] = color[2];
		rgba[3] = color[3];
		return this;
	}

	public Color rgba(Color color) {
		return rgba(color.r(), color.g(), color.b(), color.a());
	}

	public float r() {
		return rgba[0];
	}

	public float g() {
		return rgba[1];
	}

	public float b() {
		return rgba[2];
	}

	public float a() {
		return rgba[3];
	}

	public float[] rgba() {
		return rgba;
	}

	@Override
	public String toString() {
		return "Color [r=" + rgba[0] + ", g=" + rgba[1] + ", b=" + rgba[2] + ", a=" + rgba[3] + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof Color other) {
			return rgba[0] == other.rgba()[0] && rgba[1] == other.rgba()[1] && 
					rgba[2] == other.rgba()[2] && rgba[3] == other.rgba()[3];
		} else
			return false;
	}

	public static Color white() {
		return new Color(white);
	}

	public static Color lightgray() {
		return new Color(lightGray);
	}

	public static Color gray() {
		return new Color(gray);
	}

	public static Color darkgray() {
		return new Color(darkGray);
	}

	public static Color black() {
		return new Color(black);
	}

	public static Color red() {
		return new Color(red);
	}

	public static Color pink() {
		return new Color(pink);
	}

	public static Color orange() {
		return new Color(orange);
	}

	public static Color yellow() {
		return new Color(yellow);
	}

	public static Color green() {
		return new Color(green);
	}

	public static Color magenta() {
		return new Color(magenta);
	}

	public static Color cyan() {
		return new Color(cyan);
	}

	public static Color blue() {
		return new Color(blue);
	}
}
