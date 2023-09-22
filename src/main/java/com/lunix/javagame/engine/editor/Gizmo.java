package com.lunix.javagame.engine.editor;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.util.VectorUtil;

public class Gizmo extends GameObject {
	protected GameObject attachedTo;
	protected boolean isSelected;
	protected SpriteRenderer spriteRenderer;
	protected float offsetFromObject;

	public Gizmo(String name, SpriteRenderer spriteRenderer) {
		super(name);
		temporary = true;
		isSelected = false;
		this.spriteRenderer = spriteRenderer;
		addComponent(spriteRenderer);
		offsetFromObject = -2;
	}

	@Override
	public void start() {
		super.start();
		spriteRenderer.color().a(0);
		spriteRenderer.isChanged(true);
	}

	public void attach(GameObject object) {
		attachedTo = object;
		spriteRenderer.color().a(0.5f);
		spriteRenderer.isChanged(true);
		transform().position(
				attachedTo.transform()
				.position()
				.add(VectorUtil.viewDirection().mul(offsetFromObject), new Vector3f())
		);
	}

	public void detach() {
		attachedTo = null;
		spriteRenderer.color().a(0);
		spriteRenderer.isChanged(true);
		isSelected = false;
	}

	public void select(long id) {
		if(attachedTo == null)
			return;
		
		isSelected = id() == id;
		spriteRenderer.color().a(isSelected ? 1f : 0.5f);
		spriteRenderer.isChanged(true);
	}
}
