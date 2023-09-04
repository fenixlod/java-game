package com.lunix.javagame.engine.components;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.graphic.SpriteSheet;

public class Animation extends Component {
	private SpriteSheet sheet;
	private float changeDuration;
	private float leftTime;
	private int currentIdx;

	public Animation() {
		this(null, 0);
	}

	public Animation(SpriteSheet sheet, float changeDuration) {
		this.sheet = sheet;
		this.changeDuration = changeDuration;
		this.leftTime = changeDuration;
		this.currentIdx = 0;
	}

	@Override
	public void start() {
		SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
		if (renderer != null)
			renderer.sprite(sheet.get(currentIdx));
	}

	@Override
	public void update(float deltaTime) {
		leftTime -= deltaTime;

		if (leftTime <= 0f) {
			leftTime = changeDuration;
			currentIdx++;

			if (currentIdx >= sheet.size()) {
				currentIdx = 0;
			}

			SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
			if (renderer != null)
				renderer.sprite(sheet.get(currentIdx));
		}
	}

	public Animation sheet(SpriteSheet sheet) {
		this.sheet = sheet;
		return this;
	}

	public Animation changeDuration(float changeDuration) {
		this.changeDuration = changeDuration;
		return this;
	}
}
