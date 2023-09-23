package com.lunix.javagame.engine.observers;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.enums.EventType;

public abstract class Event {
	protected EventType type;
	protected GameObject target; // The object this event is applicable to
	protected GameObject initiator; // The object that initiate the event
}
