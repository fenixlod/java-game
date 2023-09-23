package com.lunix.javagame.engine.observers;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {
	private static List<Observer> observers;

	static {
		observers = new ArrayList<>();
	}

	public static void addObserver(Observer newObserver) {
		observers.add(newObserver);
	}

	public static void notify(Event event) {
		observers.forEach(o -> o.onNotify(event));
	}
}
