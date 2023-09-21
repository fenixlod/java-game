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
		this.gizmos = new ArrayList<>();
		
		SpriteRenderer sprite = new SpriteRenderer(50, 20)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.red().a(0.5f))
			.offset(new Vector3f(30, -10, 0));
		this.gizmos.add(new TranslateGizmo("TranslateGizmo X Axis", sprite, VectorUtil.X()));
		
		sprite = new SpriteRenderer(20, 50)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.blue().a(0.5f))
			.rotate(1)
			.offset(new Vector3f(0, 5, 0));
		this.gizmos.add(new TranslateGizmo("TranslateGizmo Y Axis", sprite, VectorUtil.Y()));
		
		sprite = new SpriteRenderer(10, 10)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.green().a(0.5f))
			.offset(new Vector3f(0, -5, 0));
		this.gizmos.add(new MoveGizmo("Move", sprite));
			
		sprite = new SpriteRenderer(20, 20)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.magenta().a(0.5f))
			.offset(new Vector3f(10, -10, 0));
		this.gizmos.add(new ScaleGizmo("ScaleGizmo X", sprite, VectorUtil.X()));
			
		sprite = new SpriteRenderer(20, 20)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.color(Color.cyan().a(0.5f))
			.offset(new Vector3f(0, 0, 0));
		this.gizmos.add(new ScaleGizmo("ScaleGizmo Y", sprite, VectorUtil.Y()));
	}

	public void init(Scene scene) throws Exception {
		for (Gizmo g : gizmos) {
			scene.addGameObject(g);
		}
	}

	public void attach(GameObject object) {
		this.attachedTo = object;
		for (Gizmo g : gizmos) {
			g.attach(object);
		}
	}

	public void detach() {
		this.attachedTo = null;
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

				g.transform().position(new Vector3f(selectedGizmo.transform().position()));
			}
		}
	}

	public void select(long id) {
		if(this.attachedTo == null)
			return;
		
		for (Gizmo g : gizmos) {
			g.select(id);
		}
	}
}
