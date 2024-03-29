package com.lunix.javagame.engine.editor;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.VectorUtil;

public class GizmoTools {
	private List<Gizmo> gizmos;
	private GameObject attachedTo;

	public GizmoTools() {
		gizmos = new ArrayList<>();
		
		SpriteRenderer sprite = new SpriteRenderer(5, 2)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.red().a(0.5f))
			.offset(new Vector3f(3, -1, 0));
		gizmos.add(new TranslateGizmo("TranslateGizmo X Axis", sprite, VectorUtil.X()));
		
		sprite = new SpriteRenderer(2, 5)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.blue().a(0.5f))
			.rotate(1)
			.offset(new Vector3f(0, 0.5f, 0));
		gizmos.add(new TranslateGizmo("TranslateGizmo Y Axis", sprite, VectorUtil.Y()));
		
		sprite = new SpriteRenderer(1, 1)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.green().a(0.5f))
			.offset(new Vector3f(0, -0.5f, 0));
		gizmos.add(new MoveGizmo("Move", sprite));
			
		sprite = new SpriteRenderer(2, 2)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.magenta().a(0.5f))
			.offset(new Vector3f(1, -1, 0));
		gizmos.add(new ScaleGizmo("ScaleGizmo X", sprite, VectorUtil.X()));
			
		sprite = new SpriteRenderer(2, 2)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.cyan().a(0.5f))
			.offset(new Vector3f(0, 0, 0));
		gizmos.add(new ScaleGizmo("ScaleGizmo Y", sprite, VectorUtil.Y()));
	}

	public void init(Scene scene) throws Exception {
		for (Gizmo g : gizmos) {
			scene.addGameObject(g);
		}
	}

	public void attach(GameObject object) {
		attachedTo = object;
		for (Gizmo g : gizmos) {
			g.attach(object);
		}
	}

	public void detach() {
		attachedTo = null;
		for (Gizmo g : gizmos) {
			g.detach();
		}
	}

	public void refresh() {
		// xAxis.update(deltaTime);
		// yAxis.update(deltaTime);
		// move.update(deltaTime);

		Gizmo selectedGizmo = null;
		for (Gizmo g : gizmos) {
			if (g.isSelected) {
				selectedGizmo = g;
				break;
			}
		}

		if (selectedGizmo != null) {
			for (Gizmo g : gizmos) {
				if (g == selectedGizmo)
					continue;

				g.transform().position(selectedGizmo.transform().positionCopy());
			}
		}
	}

	public void select(long id) {
		if (attachedTo == null)
			return;
		
		for (Gizmo g : gizmos) {
			g.select(id);
		}
	}
}
