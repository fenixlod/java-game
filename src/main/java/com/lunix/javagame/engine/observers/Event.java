package com.lunix.javagame.engine.observers;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.enums.EventType;

public class Event {
	protected EventType type;
	protected GameObject target; // The object this event is applicable to
	protected GameObject initiator; // The object that initiate the event

	public Event() {
		this(EventType.USER_EVENT);
	}

	public Event(EventType type) {
		this(type, null);
	}

	public Event(EventType type, GameObject target) {
		this(type, target, null);
	}

	public Event(EventType type, GameObject target, GameObject initiator) {
		this.type = type;
		this.target = target;
		this.initiator = initiator;
	}

	public EventType type() {
		return type;
	}

	public GameObject target() {
		return target;
	}

	public GameObject initiator() {
		return initiator;
	}
}
