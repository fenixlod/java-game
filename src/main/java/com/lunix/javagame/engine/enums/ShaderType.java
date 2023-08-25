package com.lunix.javagame.engine.enums;

public enum ShaderType {
	DEFAULT("DEFAULT"), NO_PERSPECTIVE("NO_PERSPECTIVE");

	private final String value;

	private ShaderType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
