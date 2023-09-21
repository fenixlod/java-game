package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.springframework.util.StringUtils;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.Transform;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.editor.GizmoTools;
import com.lunix.javagame.engine.graphic.Color;

import imgui.ImGui;

public class ObjectInspector {
	private static final Logger logger = LogManager.getLogger(ObjectInspector.class);
	private GameObject inspectedObject;
	private GameInstance game;
	private GizmoTools mover;

	public ObjectInspector() {
		this.game = GameInstance.get();
	}

	public void show() {
		if (inspectedObject == null)
			return;

		ImGui.begin("Object properties");
		ImGui.text("Name: %s, ID: %d".formatted(inspectedObject.name(), inspectedObject.id()));
		editObject(inspectedObject);
		// currentObject.ui();
		// Add ui for gizmos?
		ImGui.end();
	}

	public void init(Scene scene) throws Exception {
		this.mover = new GizmoTools();
		this.mover.init(scene);
	}

	public void update(float deltaTime, Scene currentScene) {
		if (GameInstance.get().mouse().isButtonClicked(GLFW_MOUSE_BUTTON_LEFT)) {
			Vector2i pos = GameInstance.get().mouse().positionInViewPort();
			long pickedObjID = game.window().pickObject(pos);
			GameObject pickedObject = currentScene.getGameObject(pickedObjID);
			if (pickedObject == null || !pickedObject.isTemporary()) {
				if (inspectedObject != null) {
					inspectedObject.outlined(false);
				}

				inspectedObject = pickedObject;

				if (inspectedObject != null) {
					inspectedObject.outlined(true);
					this.mover.attach(inspectedObject);
				} else {
					this.mover.detach();
				}
			} else {
				this.mover.select(pickedObjID);
			}
		}

		this.mover.refresh();
	}

	/**
	 * Display controls to edit game object fields.
	 * 
	 * @param obj
	 */
	public void editObject(GameObject obj) {
		displayFields(obj);

		Transform transform = obj.transform();
		if (ImGui.collapsingHeader("Transform")) {
			UIWidget.vect3Control("Position", transform.position());
			UIWidget.vect3Control("Scale", transform.scale(), 1);
			UIWidget.vect3Control("Facing", transform.facing());
		}

		for (Component c : obj.components()) {
			if (c.isTemporary())
				continue;

			if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
				displayFields(c);
		}
	}

	/**
	 * Display controls to edit game object filed.
	 * 
	 * @param obj
	 */
	private void displayFields(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				int modifiers = field.getModifiers();

				if (Modifier.isTransient(modifiers))
					continue;

				boolean isPrivate = Modifier.isPrivate(modifiers);

				if (isPrivate)
					field.setAccessible(true);

				Class<?> type = field.getType();
				Object value = field.get(obj);
				String name = field.getName();

				if (type == int.class) {
					displayInt(name, value, field, obj);
				} else if (type == float.class) {
					displayFloat(name, value, field, obj);
				} else if (type == boolean.class) {
					displayBoolean(name, value, field, obj);
				} else if (type == Vector3f.class) {
					displayVector3f(name, value, field, obj);
				} else if (type == Color.class) {
					displayColor(name, value, field, obj);
				} else if (type == String.class) {
					displayString(name, value, field, obj);
				} else if (type == long.class) {
					displayLong(name, value, field, obj);
				}

				if (isPrivate)
					field.setAccessible(false);
			}
		} catch (Exception e) {
			logger.error("Exception while trying do display object fields in the editor", e);
		}
	}

	private void displayInt(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Integer newValue = UIWidget.intControl(StringUtils.capitalize(name), (int) value);
		if (newValue != null) {
			field.set(obj, newValue.intValue());
		}
	}

	private void displayLong(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Long newValue = UIWidget.longControl(StringUtils.capitalize(name), (long) value);
		if (newValue != null) {
			field.set(obj, newValue.longValue());
		}
	}

	private void displayFloat(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Float newValue = UIWidget.floatControl(StringUtils.capitalize(name), (float) value);
		if (newValue != null) {
			field.set(obj, newValue.floatValue());
		}
	}

	private void displayBoolean(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Boolean newValue = UIWidget.boolControl(StringUtils.capitalize(name), (boolean) value);
		if (newValue != null) {
			field.set(obj, newValue.booleanValue());
		}
	}

	private void displayVector3f(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Vector3f val = (Vector3f) value;
		UIWidget.vect3Control(StringUtils.capitalize(name), val);
	}

	private void displayColor(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		if (value instanceof Color color) {
			Vector4f uiColor = new Vector4f();
			uiColor.set(color.rgba());
			if (UIWidget.vect4Control(StringUtils.capitalize(name), uiColor)) {
				color.rgba(uiColor.x, uiColor.y, uiColor.z, uiColor.w);
				((SpriteRenderer) obj).isChanged(true);
			}
		}
	}

	private void displayString(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		String newString = UIWidget.stringControl(StringUtils.capitalize(name), (String) value);
		if (newString != null) {
			field.set(obj, newString);
		}
	}
}
