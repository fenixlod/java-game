package com.lunix.javagame.engine.enums;

public enum ShaderType implements ResourceType {
	DEFAULT("DEFAULT"), DEBUG("DEBUG"), PICKING("PICKING");

	private final String value;

	private ShaderType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
