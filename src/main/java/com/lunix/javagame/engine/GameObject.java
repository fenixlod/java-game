package com.lunix.javagame.engine;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class GameObject {
	private String name;
	private Transform transform;
	@JsonManagedReference
	private Map<Class<? extends Component>, Component> components;

	public GameObject() {	
		this("");
	}

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
		components.values().forEach(Component::start);
	}

	public String name() {
		return this.name;
	}

	public GameObject name(String name) {
		this.name = name;
		return this;
	}

	public Transform transform() {
		return transform;
	}

	public GameObject move(Vector3f offset) {
		this.transform.move(offset);
		return this;
	}

	public void ui() {
		components.values().forEach(Component::ui);
	}
}
