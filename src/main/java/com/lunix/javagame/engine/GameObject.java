package com.lunix.javagame.engine;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

public class GameObject {
	private final String name;
	private Transform transform;
	private Map<Class<? extends Component>, Component> components;

	public GameObject(String name) {
		this(name, new Transform());
	}

	public GameObject(String name, Transform transform) {
		this.name = name;
		this.transform = transform;
		this.components = new HashMap<>();
	}

	public GameObject(String name, Vector3f position) {
		this(name, new Transform().position(position));
	}

	public <T extends Component> T getComponent(Class<T> className) {
		Component object = components.get(className);

		if (object != null)
			return className.cast(object);

		return null;
	}

	public GameObject addComponent(Component component) {
		components.put(component.getClass(), component);
		component.owner(this);
		return this;
	}

	public <T extends Component> void removeComponent(Class<T> className) {
		components.remove(className);
	}

	public void update(float deltaTime) {
		components.values().forEach(c -> c.update(deltaTime));
	}

	public void start() {
		components.values().forEach(c -> c.start());
	}

	public String name() {
		return this.name;
	}

	public Transform transform() {
		return transform;
	}

	public GameObject move(Vector3f offset) {
		this.transform.move(offset);
		return this;
	}
}
