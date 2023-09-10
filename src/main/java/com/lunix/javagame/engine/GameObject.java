package com.lunix.javagame.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class GameObject {
	private long id = -1;
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

	public GameObject(String name, Vector3f position) {
		this(name, new Transform().position(position));
	}

	public GameObject(String name, Transform transform) {
		this.name = name;
		this.transform = transform;
		this.components = new HashMap<>();
		this.id = GameInstance.getNextId();
	}

	/**
	 * Get component by class. Only one component per type is allowed.
	 * 
	 * @param <T>
	 * @param className
	 * @return
	 */
	public <T extends Component> T getComponent(Class<T> className) {
		Component object = this.components.get(className);

		if (object != null)
			return className.cast(object);

		return null;
	}

	/**
	 * Add new component to the object. Only one component per type is allowed.
	 * 
	 * @param component
	 * @return
	 */
	public GameObject addComponent(Component component) {
		component.generateId();
		this.components.put(component.getClass(), component);
		component.owner(this);
		return this;
	}

	/**
	 * Remove component by type.
	 * 
	 * @param <T>
	 * @param className
	 */
	public <T extends Component> void removeComponent(Class<T> className) {
		this.components.remove(className);
	}

	/**
	 * Update this object components animations, AI etc.
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		this.components.values().forEach(c -> c.update(deltaTime));
	}

	/**
	 * Start the object components.
	 */
	public void start() {
		this.components.values().forEach(Component::start);
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

	public Collection<Component> components() {
		return components.values();
	}

	public long id() {
		return this.id;
	}
}
