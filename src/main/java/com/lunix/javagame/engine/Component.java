package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Component {
	protected static final Logger logger = LogManager.getLogger(Component.class);
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
