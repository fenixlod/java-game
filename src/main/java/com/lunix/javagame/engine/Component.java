package com.lunix.javagame.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.BoxCollider;
import com.lunix.javagame.engine.components.CircleCollider;
import com.lunix.javagame.engine.components.MouseDragging;
import com.lunix.javagame.engine.components.RigidBody;
import com.lunix.javagame.engine.components.SnapToGrid;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ObjectEventType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
		@JsonSubTypes.Type(name = "Animation", value = Animation.class),
		@JsonSubTypes.Type(name = "SpriteRenderer", value = SpriteRenderer.class),
		@JsonSubTypes.Type(name = "MouseDragging", value = MouseDragging.class),
		@JsonSubTypes.Type(name = "BoxCollider", value = BoxCollider.class),
		@JsonSubTypes.Type(name = "CircleCollider", value = CircleCollider.class),
		@JsonSubTypes.Type(name = "RigidBody", value = RigidBody.class),
		@JsonSubTypes.Type(name = "SnapToGrid", value = SnapToGrid.class)
})
public abstract class Component {
	protected static final Logger logger = LogManager.getLogger(Component.class);
	@JsonBackReference
	protected GameObject owner;
	private long id = -1;
	protected transient boolean destroyed;

	/**
	 * Initialize the component.
	 */
	public void start() {
	}

	/**
	 * Update this component.
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime, boolean isPlaying) {
	}

	/**
	 * Destroy the component. Free any allocated resources.
	 */
	public void destroy() {
	}

	public void generateId(boolean replace) {
		if (id == -1 || replace)
			id = GameInstance.getNextId();
	}

	public long id() {
		return id;
	}

	public void owner(GameObject owner) {
		this.owner = owner;
	}

	public GameObject owner() {
		return owner;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	protected void onNotify(ObjectEventType e) {
	}
}
