package com.lunix.javagame.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import imgui.ImGui;
import imgui.type.ImInt;

public class Editor {
	private static final Logger logger = LogManager.getLogger(Editor.class);

	/**
	 * Display controls to edit game object fields.
	 * 
	 * @param obj
	 */
	public static void editObject(GameObject obj) {
		obj.components().forEach(Editor::displayFields);
	}

	/**
	 * Display controls to edit game object filed.
	 * 
	 * @param obj
	 */
	private static void displayFields(Object obj) {
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
				}
	/*			
				float[] uiColor = color.rgba();
				if (ImGui.colorPicker4("Color picker: ", uiColor)) {
					// color.rgba(uiColor);
					isChanged = true;
				}
*/
				if (isPrivate)
					field.setAccessible(false);
			}
		} catch (Exception e) {
			logger.error("Exception while trying do display object fields in the editor", e);
		}
	}

	private static void displayInt(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		ImInt uiValue = new ImInt((int) value);
		if (ImGui.inputInt(name + ": ", uiValue)) {
			field.set(obj, uiValue.get());
		}
	}

	private static void displayFloat(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		float[] uiValue = { (float) value };
		if (ImGui.dragFloat(name + ": ", uiValue)) {
			field.set(obj, uiValue[0]);
		}
	}

	private static void displayBoolean(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		boolean uiValue = (boolean) value;
		if (ImGui.checkbox(name + ": ", uiValue)) {
			field.set(obj, !uiValue);
		}
	}

	private static void displayVector3f(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Vector3f val = (Vector3f) value;
		float[] uiValue = { val.x, val.y, val.z };
		if (ImGui.dragFloat3(name + ": ", uiValue)) {
			val.set(uiValue[0], uiValue[1], uiValue[2]);
			field.set(obj, val);
		}
	}
}
