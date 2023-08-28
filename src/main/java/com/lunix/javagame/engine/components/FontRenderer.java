package com.lunix.javagame.engine.components;

import com.lunix.javagame.engine.Component;

public class FontRenderer extends Component {
	private boolean firstTime = true;

	@Override
	public void start() {
		super.start();
		logger.info("[{}] Font renderer is starting!", owner.name());
	}

	@Override
	public void update(float deltaTime) {
		if (firstTime) {
			logger.info("[{}] Font renderer is updating!", owner.name());
			firstTime = false;
		}
	}
}
