package com.lunix.javagame.engine.struct;

import java.util.ArrayList;
import java.util.List;

import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.enums.AnimationStateType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Sprite;

public class AnimationState {
	public AnimationStateType stateType;
	private List<AnimationFrame> animationFrames;
	private static Sprite defaultSprite = ResourcePool.getSprite(TextureType.NONE);
	private boolean looping;

	private transient float leftTime;
	private transient int currentPose;

	public AnimationState() {
		this(AnimationStateType.NONE);
	}

	public AnimationState(AnimationStateType stateType) {
		animationFrames = new ArrayList<>();
		this.stateType = stateType;
	}

	public AnimationState addFrame(Sprite pose, float duration) {
		animationFrames.add(new AnimationFrame(pose, duration));
		return this;
	}

	public boolean isLooping() {
		return looping;
	}

	public AnimationState looping(boolean looping) {
		this.looping = looping;
		return this;
	}

	public void update(float deltaTime) {
		if (currentPose < animationFrames.size()) {
			leftTime -= deltaTime;

			if (leftTime <= 0f) {
				if (currentPose != animationFrames.size() - 1 || looping) {
					currentPose = (currentPose + 1) % animationFrames.size();
				}

				leftTime = animationFrames.get(currentPose).poseDuration();
			}
		}
	}

	public Sprite currentSprite() {
		if (currentPose < animationFrames.size())
			return animationFrames.get(currentPose).pose();

		return defaultSprite;
	}

	public AnimationStateType stateType() {
		return stateType;
	}
}
