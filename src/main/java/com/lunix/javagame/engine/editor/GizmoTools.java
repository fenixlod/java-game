package com.lunix.javagame.engine.editor;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.util.VectorUtil;

public class GizmoTools extends GameObject {
	private TranslateGizmo xAxis;
	private TranslateGizmo yAxis;
	private MoveGizmo move;
	private GameObject attachedTo;

	public GizmoTools() {
		SpriteRenderer xAxisSprite = new SpriteRenderer(50, 20)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.red().a(0.5f))
			.offset(new Vector3f(30, -10, 0));
		this.xAxis = new TranslateGizmo("TranslateGizmo X Axis", xAxisSprite, VectorUtil.X());
		
		SpriteRenderer yAxisSprite = new SpriteRenderer(20, 50)
			.widthDirection(VectorUtil.X())
			.heightDirection(VectorUtil.Y())
			.sprite(ResourcePool.getSprite(TextureType.ARROW))
			.color(Color.blue().a(0.5f))
			.rotate(1)
			.offset(new Vector3f(0, 5, 0));
		
		this.yAxis = new TranslateGizmo("TranslateGizmo Y Axis", yAxisSprite, VectorUtil.Y());
		
		SpriteRenderer freeMoveSprite = new SpriteRenderer(10, 10)
				.widthDirection(VectorUtil.X())
				.heightDirection(VectorUtil.Y())
				.color(Color.green().a(0.5f))
				.offset(new Vector3f(0, -5, 0));
			
			this.move = new MoveGizmo("Move", freeMoveSprite);
	}

	public void init(Scene scene) throws Exception {
		xAxis.start();
		yAxis.start();
		move.start();
		scene.addGameObject(xAxis);
		scene.addGameObject(yAxis);
		scene.addGameObject(move);
	}

	public void attach(GameObject object) {
		this.attachedTo = object;
		xAxis.attach(object);
		yAxis.attach(object);
		move.attach(object);
	}

	public void detach() {
		this.attachedTo = null;
		xAxis.detach();
		yAxis.detach();
		move.detach();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		xAxis.update(deltaTime);
		yAxis.update(deltaTime);
		move.update(deltaTime);

		if (xAxis.isSelected) {
			yAxis.transform().position(new Vector3f(xAxis.transform().position()));
			move.transform().position(new Vector3f(xAxis.transform().position()));
		} else if (yAxis.isSelected) {
			xAxis.transform().position(new Vector3f(yAxis.transform().position()));
			move.transform().position(new Vector3f(yAxis.transform().position()));
		} else if (move.isSelected) {
			xAxis.transform().position(new Vector3f(move.transform().position()));
			yAxis.transform().position(new Vector3f(move.transform().position()));
		}
	}

	public void select(long id) {
		if(this.attachedTo == null)
			return;
		
		xAxis.select(id);
		yAxis.select(id);
		move.select(id);
	}
}
