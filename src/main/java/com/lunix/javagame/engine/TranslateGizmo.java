package com.lunix.javagame.engine;

import org.joml.Vector3f;

import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.util.VectorUtil;

public class TranslateGizmo extends GameObject {
	private GameObject xAxis;
	private GameObject yAxis;
	private GameObject attachedTo;

	public TranslateGizmo(Sprite arrowSprite) {
		this.xAxis = new GameObject("TranslateGizmo X Axis")
				.addComponent(new SpriteRenderer(48, 16)
								.widthDirection(VectorUtil.X())
								.heightDirection(VectorUtil.Y())
								.sprite(arrowSprite)
								.color(Color.red())
								.offset(new Vector3f(24, -8, 0))
				);
		this.yAxis = new GameObject("TranslateGizmo Y Axis")
				.addComponent(new SpriteRenderer(16, 48)
								.widthDirection(VectorUtil.X())
								.heightDirection(VectorUtil.Y())
								.sprite(arrowSprite)
								.color(Color.blue())
								.rotate(1)
				);
		this.temporary = true;
		this.xAxis.temporary(true);
		this.yAxis.temporary(true);
	}

	public void init(Scene scene) throws Exception {
		SpriteRenderer renderer = xAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.red().a(0));
		renderer = yAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.blue().a(0));
		scene.addGameObject(xAxis);
		scene.addGameObject(yAxis);
	}

	public void attach(GameObject object) {
		this.attachedTo = object;
		SpriteRenderer renderer = xAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.red().a(1));
		
		renderer = yAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.blue().a(1));
	}

	public void detach() {
		this.attachedTo = null;
		SpriteRenderer renderer = xAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.red().a(0));
		renderer = yAxis.getComponent(SpriteRenderer.class);
		renderer.color(Color.blue().a(0));
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (this.attachedTo == null)
			return;

		this.xAxis.transform().position(
				this.attachedTo.transform().position().add(VectorUtil.viewDirection().mul(-1), new Vector3f()));
		this.yAxis.transform().position(
				this.attachedTo.transform().position().add(VectorUtil.viewDirection().mul(-1), new Vector3f()));
	}
}
