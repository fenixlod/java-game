package com.lunix.javagame.engine.components;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.graphic.SpriteSheet;

public class Animation extends Component {
	private SpriteSheet sheet;
	private float changeInterval;
	private int startingPose;

	private transient float leftTime;
	private transient int currentPose;

	public Animation() {
		this(null, 0);
	}

	public Animation(SpriteSheet sheet, float changeInterval) {
		this.sheet = sheet;
		this.changeInterval = changeInterval;
		this.startingPose = 0;
	}

	@Override
	public void start() {
		SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
		currentPose = startingPose;
		leftTime = changeInterval;
		if (renderer != null)
			renderer.sprite(sheet.get(currentPose));
	}

	@Override
	public void update(float deltaTime) {
		leftTime -= deltaTime;

		if (leftTime <= 0f) {
			leftTime = changeInterval;
			currentPose++;

			if (currentPose >= sheet.size()) {
				currentPose = 0;
			}

			SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
			if (renderer != null)
				renderer.sprite(sheet.get(currentPose));
		}
	}

	public Animation sheet(SpriteSheet sheet) {
		this.sheet = sheet;
		return this;
	}

	public Animation changeInterval(float changeInterval) {
		this.changeInterval = changeInterval;
		this.leftTime = changeInterval;
		return this;
	}

	public Animation startingPose(int startingPose) {
		this.startingPose = startingPose;
		this.currentPose = startingPose;
		return this;
	}
}
