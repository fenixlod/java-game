package com.lunix.javagame.engine.editor;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.util.VectorUtil;

public class Gizmo extends GameObject {
	protected GameObject attachedTo;
	protected boolean isSelected;
	private SpriteRenderer spriteRenderer;
	protected float offsetFromObject;

	public Gizmo(String name, SpriteRenderer spriteRenderer) {
		super(name);
		this.temporary = true;
		this.isSelected = false;
		this.spriteRenderer = spriteRenderer;
		addComponent(spriteRenderer);
		this.offsetFromObject = -2;
	}

	@Override
	public void start() {
		super.start();
		this.spriteRenderer.color().a(0);
		this.spriteRenderer.isChanged(true);
	}

	public void attach(GameObject object) {
		this.attachedTo = object;
		this.spriteRenderer.color().a(0.5f);
		this.spriteRenderer.isChanged(true);
		this.transform().position(
				this.attachedTo.transform()
				.position()
				.add(VectorUtil.viewDirection().mul(offsetFromObject), new Vector3f())
		);
	}

	public void detach() {
		this.attachedTo = null;
		this.spriteRenderer.color().a(0);
		this.spriteRenderer.isChanged(true);
		this.isSelected = false;
	}

	public void select(long id) {
		if(this.attachedTo == null)
			return;
		
		isSelected = id() == id;
		this.spriteRenderer.color().a(isSelected ? 1f : 0.5f);
		this.spriteRenderer.isChanged(true);
	}
}
