package com.lunix.javagame.engine.enums;

public enum TextureType implements ResourceType {
	PLAYER("DEFAULT");

	private final String value;

	private TextureType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
