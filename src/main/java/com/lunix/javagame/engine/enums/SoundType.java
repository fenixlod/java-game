package com.lunix.javagame.engine.enums;

public enum SoundType implements ResourceType {
	NONE("NONE"), PLAYER_HIT("PLAYER_HIT");

	private final String value;

	private SoundType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
