package com.lunix.javagame.engine;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joml.Vector3f;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class GameObject {
	private long id = -1;
	private String name;
	private Transform transform;
	@JsonManagedReference
	private Map<Class<? extends Component>, Component> components;
	private transient boolean outlined;
	protected transient boolean temporary;
	protected transient boolean destroyed;

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
		components = new TreeMap<>((o1, o2) -> {
			try {
				Method method1 = o1.getMethod("priority");
				Method method2 = o2.getMethod("priority");
				Object result1 = method1.invoke(null);
				Object result2 = method2.invoke(null);
				return Integer.compare((int) result1, (int) result2);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		});
		id = GameInstance.getNextId();
	}

	/**
	 * Start the object components.
	 */
	public void start() {
		components.values().forEach(Component::start);
	}

	/**
	 * Update this object components animations, AI etc.
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime, boolean isPlaying) {
		if (destroyed)
			return;

		Class<? extends Component> destroyedComponent = null;
		for (Entry<Class<? extends Component>, Component> entry : components.entrySet()) {
			entry.getValue().update(deltaTime, isPlaying);
			if (entry.getValue().isDestroyed())
				destroyedComponent = entry.getKey();
		}

		if (destroyedComponent != null)
			removeComponent(destroyedComponent);
	}

	public void destroy() {
		destroyed = true;
		components.values().forEach(Component::destroy);
	}

	/**
	 * Get component by class. Only one component per type is allowed.
	 * 
	 * @param <T>
	 * @param className
	 * @return
	 */
	public <T extends Component> T getComponent(Class<T> className) {
		Component object = components.get(className);

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
		components.put(component.getClass(), component);
		component.owner(this);
		component.start();
		return this;
	}

	/**
	 * Remove component by type.
	 * 
	 * @param <T>
	 * @param className
	 */
	public <T extends Component> void removeComponent(Class<T> className) {
		components.remove(className);
	}

	public String name() {
		return name;
	}

	public GameObject name(String name) {
		this.name = name;
		return this;
	}

	public Transform transform() {
		return transform;
	}

	public GameObject move(Vector3f offset) {
		transform.move(offset);
		return this;
	}

	public Collection<Component> components() {
		return components.values();
	}

	public long id() {
		return id;
	}

	public boolean isOutlined() {
		return outlined;
	}

	public void outlined(boolean outlined) {
		this.outlined = outlined;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void temporary(boolean temporary) {
		this.temporary = temporary;
	}

	public boolean isDestriyed() {
		return destroyed;
	}
}
