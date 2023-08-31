package com.lunix.javagame.engine.enums;

public enum TextureType implements ResourceType {
	NONE("NONE"), PLAYER("PLAYER"), ENEMY("ENEMY");

	private final String value;

	private TextureType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
