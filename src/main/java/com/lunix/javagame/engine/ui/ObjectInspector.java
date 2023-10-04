package com.lunix.javagame.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.editor.GizmoTools;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.struct.ComponentMenuItem;
import com.lunix.javagame.engine.util.Helper;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTreeNodeFlags;

public class ObjectInspector {
	private static final Logger logger = LogManager.getLogger(ObjectInspector.class);
	private GameObject inspectedObject;
	private GameInstance game;
	private GizmoTools mover;
	private List<ComponentMenuItem> allComponents;

	public ObjectInspector() {
		allComponents = Helper.getAllComponentClasses();
	}

	public void init() {
		game = GameInstance.get();
	}

	public void show() {
		ImGui.begin("Object properties");
		if (inspectedObject != null) {
			if (ImGui.beginPopupContextWindow("ComponentAdder")) {
				for (ComponentMenuItem item : allComponents) {
					if (item.childs().isEmpty()) {
						Component component = inspectedObject.getComponent(item.type());
						if (component == null) {
							addMenuItem(item.name(), item.type());
						}
					} else {
						boolean haveChild = false;
						for (Class<? extends Component> clazz : item.childs()) {
							Component component = inspectedObject.getComponent(clazz);
							if (component != null) {
								haveChild = true;
								break;
							}
						}

						if (!haveChild) {
							if (ImGui.beginMenu("Add " + item.name())) {
								item.childs().forEach((child) -> addMenuItem(child.getSimpleName(), child));
								ImGui.endMenu();
							}
						}
					}
				}
				ImGui.endPopup();
			}
			editObject(inspectedObject);
			// currentObject.ui();
			// Add ui for gizmos?
		}
		ImGui.end();
	}

	private void addMenuItem(String name, Class<? extends Component> type) {
		if (ImGui.menuItem("Add " + name)) {
			try {
				inspectedObject.addComponent(type.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				logger.error("Error while creating object properties contex menu", e);
			}
		}
	}

	public void start(Scene scene) throws Exception {
		mover = new GizmoTools();
		mover.init(scene);
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
					mover.attach(inspectedObject);
				} else {
					mover.detach();
				}
			} else {
				mover.select(pickedObjID);
			}
		}

		if (inspectedObject != null && GameInstance.get().keyboard().isKeyPressed(GLFW_KEY_DELETE)) {
			inspectedObject.destroy();
			inspectedObject = null;
			mover.detach();
		}

		mover.refresh();
	}

	/**
	 * Display controls to edit game object fields.
	 * 
	 * @param obj
	 */
	public void editObject(GameObject obj) {
		displayFields(obj);

		if (ImGui.collapsingHeader("Transform")) {
			Vector3f position = obj.transform().positionCopy();
			if (UIWidget.vect3Control("Position", position))
				obj.transform().position(position);

			Vector3f scale = obj.transform().scaleCopy();
			if (UIWidget.vect3Control("Scale", scale, 1))
				obj.transform().scale(scale);

			Vector3f facing = obj.transform().facingCopy();
			if (UIWidget.vect3Control("Facing", facing))
				obj.transform().facing(facing);
		}

		Class<? extends Component> markedForDelete = null;
		for (Component c : obj.components()) {
			float width = ImGui.calcItemWidth();
			boolean opened = ImGui.collapsingHeader(c.getClass().getSimpleName(), ImGuiTreeNodeFlags.AllowItemOverlap);
			ImGui.sameLine(width + 100);
			ImGui.pushStyleColor(ImGuiCol.Text, 1f, 0f, 0f, 1f);
			ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0f, 0f);
			ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 0f, 0f);
			ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0f, 0f, 0f, 0f);
			ImGui.pushID(c.getClass().getSimpleName() + "Remove");
			if (ImGui.button("x")) {
				markedForDelete = c.getClass();
			}
			ImGui.popID();
			ImGui.popStyleColor(4);
			if (ImGui.isItemHovered())
				ImGui.setTooltip("Remove component");

			if (opened) {
				displayFields(c);
			}
		}

		if (markedForDelete != null)
			obj.removeComponent(markedForDelete);
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
				} else if (type.isEnum()) {
					displayEnum(name, value, field, obj);
				} else if (type == List.class) {
					displayList(name, value, field, obj);
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

	private void displayEnum(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		Enum<?> newValue = UIWidget.enumControl(StringUtils.capitalize(name), (Enum<?>) value);
		if (newValue != null) {
			field.set(obj, newValue);
		}
	}

	private void displayList(String name, Object value, Field field, Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		List<?> list = (List<?>) value;

		int id = 1;
		if (ImGui.treeNodeEx(name, ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding
				| ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth, StringUtils.capitalize(name))) {
			for (Object o : list) {
				ImGui.pushID(id);
				boolean treeIsOpened = ImGui.treeNodeEx(o.getClass().getSimpleName(),
						ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding
								| ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
						"Item " + id);
				ImGui.popID();

				if (treeIsOpened) {
					displayFields(o);
					ImGui.treePop();
				}
				id++;
			}
			ImGui.treePop();
		}
	}
}
