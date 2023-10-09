package com.lunix.javagame.engine.struct;

import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Sprite;

public class AnimationFrame {
	private Sprite pose;
	private float poseDuration;

	public AnimationFrame() {
		this(ResourcePool.getSprite(TextureType.NONE), 0);
	}

	public AnimationFrame(Sprite pose, float poseDuration) {
		this.pose = pose;
		this.poseDuration = poseDuration;
	}

	public Sprite pose() {
		return pose;
	}

	public float poseDuration() {
		return poseDuration;
	}

}
