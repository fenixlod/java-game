package com.lunix.javagame.engine.ui;

import java.util.List;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Scene;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class SceneHierarchyWindow {
	public void show(Scene scene) {
		ImGui.begin("Scene Hierarchy");
		List<GameObject> objects = scene.objects();
		int idx = 0;
		for (GameObject obj : objects) {
			if (obj.isTemporary())
				continue;

			ImGui.pushID(idx++);
			boolean treeIsOpened = ImGui.treeNodeEx(obj.name(),
					ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | 
					ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
					obj.name());
			ImGui.popID();

			if (ImGui.beginDragDropSource()) {
				ImGui.setDragDropPayload(obj);
				ImGui.text(obj.name());
				ImGui.endDragDropSource();
			}

			if (ImGui.beginDragDropTarget()) {
				GameObject payload = ImGui.acceptDragDropPayload(GameObject.class);
				if (payload != null) {
					System.out.println("Droped:" + payload.name());
				}
				ImGui.endDragDropTarget();
			}

			if (treeIsOpened) {
				ImGui.treePop();
			}
		}
		ImGui.end();
	}
}
