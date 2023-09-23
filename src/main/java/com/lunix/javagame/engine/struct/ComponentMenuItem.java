package com.lunix.javagame.engine.struct;

import java.util.ArrayList;
import java.util.List;

import com.lunix.javagame.engine.Component;

public class ComponentMenuItem {
	private Class<? extends Component> type;
	private String name;
	private List<Class<? extends Component>> childs;

	public ComponentMenuItem() {
		this.childs = new ArrayList<>();
	}

	public String name() {
		return name;
	}

	public ComponentMenuItem addChild(Class<? extends Component> clazz) {
		childs.add(clazz);
		return this;
	}

	public ComponentMenuItem name(String name) {
		this.name = name;
		return this;
	}

	public Class<? extends Component> type() {
		return type;
	}

	public ComponentMenuItem type(Class<? extends Component> type) {
		this.type = type;
		return this;
	}

	public List<Class<? extends Component>> childs() {
		return childs;
	}
}
