package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SpriteRenderer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "Animation", value = Animation.class),
		@JsonSubTypes.Type(name = "SpriteRenderer", value = SpriteRenderer.class)
})
public abstract class Component {
	protected static final Logger logger = LogManager.getLogger(Component.class);
	@JsonBackReference
	protected GameObject owner;

	public void owner(GameObject owner) {
		this.owner = owner;
	}

	public GameObject owner() {
		return this.owner;
	}

	public void update(float deltaTime) {
	}

	public void start() {
	}

	public void ui() {
	}
}
