package com.lunix.javagame.engine.enums;

public enum TextureType implements ResourceType {
	NONE("NONE"), PLAYER("PLAYER"), ENEMY("ENEMY"), PLAYER_IDLE("PLAYER_IDLE"), TILE_BRICK("TILE_BRICK");

	private final String value;

	private TextureType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
