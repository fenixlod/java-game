package com.lunix.javagame.engine.components;

import java.util.List;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.graphic.Sprite;

public class Animation extends Component {
	private List<Sprite> poses;
	private float changeInterval;
	private int startingPose;

	private transient float leftTime;
	private transient int currentPose;

	public Animation() {
		this(null, 0);
	}

	public Animation(List<Sprite> poses, float changeInterval) {
		this.poses = poses;
		this.changeInterval = changeInterval;
		startingPose = 0;
	}

	@Override
	public void start() {
		SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
		currentPose = startingPose;
		leftTime = changeInterval;
		if (renderer != null) {
			if (currentPose >= poses.size() || currentPose < 0) {
				logger.warn("Starting pose is out of bounds. Current value:{}, bounds: 0 - {}", currentPose,
						poses.size());
				currentPose = 0;
			}
			renderer.sprite(poses.get(currentPose));
		}
	}

	@Override
	public void update(float deltaTime) {
		leftTime -= deltaTime;

		if (leftTime <= 0f) {
			leftTime = changeInterval;
			currentPose++;

			if (currentPose >= poses.size()) {
				currentPose = 0;
			}

			SpriteRenderer renderer = owner.getComponent(SpriteRenderer.class);
			if (renderer != null)
				renderer.sprite(poses.get(currentPose));
		}
	}

	public Animation poses(List<Sprite> poses) {
		this.poses = poses;
		return this;
	}

	public Animation changeInterval(float changeInterval) {
		this.changeInterval = changeInterval;
		leftTime = changeInterval;
		return this;
	}

	public Animation startingPose(int startingPose) {
		this.startingPose = startingPose;
		currentPose = startingPose;
		return this;
	}

	/**
	 * All Components needs to implement this method. This value determine the order
	 * of execution of components within a game object. The lower the priority value
	 * = the sooner this component will be executed. Priority of 1 - first to
	 * execute, 1000 - last to execute.
	 * 
	 * @return
	 */
	public static int priority() {
		return 400;
	}
}
