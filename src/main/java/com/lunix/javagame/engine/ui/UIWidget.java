package com.lunix.javagame.engine.ui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImLong;
import imgui.type.ImString;

public class UIWidget {
	private static float DEFAULT_COLUMN_WIDTH = 130f;

	public static boolean vect3Control(String label, Vector3f value, float resetValue) {
		boolean valueChanged = false;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2;
		Vector2f buttonSize = new Vector2f(lineHeight + 3, lineHeight);
		ImGui.pushItemWidth(-1);
		float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 3) / 3;

		ImGui.pushItemWidth(widthEach + 1);
		ImGui.pushStyleColor(ImGuiCol.Button, 0.75f, 0f, 0f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1f, 0f, 0f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1f, 0.3f, 0.3f, 1f);
		if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
			value.x = resetValue;
			valueChanged = true;
		}
		ImGui.popStyleColor(3);
		ImGui.sameLine();
		float[] vectValuesX = { value.x };
		ImGui.dragFloat("##x", vectValuesX, 0.1f, 0, 0, "%.2f");
		ImGui.popItemWidth();

		ImGui.sameLine();
		ImGui.pushItemWidth(widthEach + 1);
		ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0.75f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 0f, 1f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.3f, 1f, 1f);
		if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
			value.y = resetValue;
			valueChanged = true;
		}
		ImGui.popStyleColor(3);
		ImGui.sameLine();
		float[] vectValuesY = { value.y };
		ImGui.dragFloat("##y", vectValuesY, 0.1f, 0, 0, "%.2f");
		ImGui.popItemWidth();

		ImGui.sameLine();
		ImGui.pushItemWidth(widthEach + 1);
		ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0.75f, 0f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0f, 1f, 0f, 1f);
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 1f, 0.3f, 1f);
		if (ImGui.button("Z", buttonSize.x, buttonSize.y)) {
			value.z = resetValue;
			valueChanged = true;
		}
		ImGui.popStyleColor(3);
		ImGui.sameLine();
		float[] vectValuesZ = { value.z };
		ImGui.dragFloat("##z", vectValuesZ, 0.1f, 0, 0, "%.2f");
		ImGui.popItemWidth();

		ImGui.nextColumn();

		value.x = vectValuesX[0];
		value.y = vectValuesY[0];
		value.z = vectValuesZ[0];

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return valueChanged;
	}

	public static boolean vect3Control(String label, Vector3f value) {
		return vect3Control(label, value, 0f);
	}

	public static String stringControl(String label, String value) {
		String newValue = null;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		ImString val = new ImString(value, 20);
		if (ImGui.inputText("", val)) {
			newValue = val.get();
		}
		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return newValue;
	}

	public static Long longControl(String label, long value) {
		Long newValue = null;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		ImLong uiValue = new ImLong(value);
		if (ImGui.inputScalar("", ImGuiDataType.U32, uiValue)) {
			newValue = uiValue.get();
		}
		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return newValue;
	}

	public static Float floatControl(String label, float value) {
		Float newValue = null;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		ImFloat uiValue = new ImFloat(value);
		if (ImGui.inputFloat("", uiValue)) {
			newValue = uiValue.get();
		}
		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return newValue;
	}

	public static Integer intControl(String label, int value) {
		Integer newValue = null;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		ImInt uiValue = new ImInt(value);
		if (ImGui.inputInt("", uiValue)) {
			newValue = uiValue.get();
		}
		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return newValue;
	}

	public static Boolean boolControl(String label, boolean value) {
		Boolean newValue = null;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		if (ImGui.checkbox("", value)) {
			newValue = !value;
		}
		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return newValue;
	}

	public static boolean vect4Control(String label, Vector4f value) {
		boolean changed = false;
		ImGui.pushID(label);

		ImGui.columns(2);
		ImGui.setColumnWidth(0, DEFAULT_COLUMN_WIDTH);
		ImGui.text(label);
		ImGui.nextColumn();

		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
		ImGui.pushItemWidth(-1);

		float[] uiColor = { value.x, value.y, value.z, value.w };
		if (ImGui.colorEdit4("##colorPicker", uiColor, ImGuiColorEditFlags.OptionsDefault)) {
			value.set(uiColor);
			changed = true;
		}

		ImGui.popItemWidth();

		ImGui.nextColumn();

		ImGui.popStyleVar();
		ImGui.columns(1);
		ImGui.popID();
		return changed;
	}
}
