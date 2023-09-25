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
	public void update(float deltaTime, boolean isPlaying) {
		if (firstTime) {
			logger.info("[{}] Font renderer is updating!", owner.name());
			firstTime = false;
		}
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
		return 300;
	}
}
