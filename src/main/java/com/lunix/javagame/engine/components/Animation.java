package com.lunix.javagame.engine.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.enums.AnimationStateTriggerType;
import com.lunix.javagame.engine.enums.AnimationStateType;
import com.lunix.javagame.engine.struct.AnimationState;

public class Animation extends Component {
	@JsonDeserialize(keyUsing = StateTriggerDeserializer.class)
	private Map<StateTrigger, AnimationStateType> stateTransfers;
	private List<AnimationState> states;
	private AnimationStateType defaultState;

	private transient AnimationState currentState;

	public Animation() {
		stateTransfers = new HashMap<>();
		states = new ArrayList<>();
		defaultState = AnimationStateType.NONE;
	}

	@Override
	public void start() {
		for (AnimationState state : states) {
			if (state.stateType == defaultState) {
				currentState = state;
				break;
			}
		}
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		if (currentState != null) {
			currentState.update(deltaTime);
			SpriteRenderer sprite = owner.getComponent(SpriteRenderer.class);
			if (sprite != null) {
				sprite.sprite(currentState.currentSprite());
			}
		}
	}


	public Animation addState(AnimationState state) {
		states.add(state);
		return this;
	}

	public void addStateTrigger(AnimationStateType from, AnimationStateType to, AnimationStateTriggerType trigger) {
		stateTransfers.put(new StateTrigger(from, trigger), to);
	}

	public void trigger(AnimationStateTriggerType trigger) {
		AnimationStateType stateToChange = stateTransfers.get(new StateTrigger(currentState.stateType(), trigger));
		if (stateToChange != null) {
			for (AnimationState state : states) {
				if (state.stateType == stateToChange) {
					currentState = state;
					return;
				}
			}
		}

		logger.error("Unable to find trigger: {}", trigger);
	}

	public Animation defaultState(AnimationStateType defaultState) {
		this.defaultState = defaultState;
		return this;
	}
}

class StateTrigger {
	private AnimationStateType state;
	private AnimationStateTriggerType trigger;

	public StateTrigger() {
	}

	public StateTrigger(String key) {
		String[] split = key.split("-");
		state = AnimationStateType.valueOf(split[0]);
		trigger = AnimationStateTriggerType.valueOf(split[1]);
	}

	public StateTrigger(AnimationStateType state, AnimationStateTriggerType trigger) {
		this.state = state;
		this.trigger = trigger;
	}

	public AnimationStateType state() {
		return state;
	}

	public StateTrigger state(AnimationStateType state) {
		this.state = state;
		return this;
	}

	public AnimationStateTriggerType trigger() {
		return trigger;
	}

	public StateTrigger trigger(AnimationStateTriggerType trigger) {
		this.trigger = trigger;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj instanceof StateTrigger stateTrigger) {
			return state == stateTrigger.state && trigger == stateTrigger.trigger;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(state, trigger);
	}

	@Override
	public String toString() {
		return state.toString() + "-" + trigger.toString();
	}
}

class StateTriggerDeserializer extends KeyDeserializer {
	@Override
	public StateTrigger deserializeKey(String key, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return new StateTrigger(key);
	}
}
