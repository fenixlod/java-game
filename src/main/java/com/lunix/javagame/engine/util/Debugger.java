package com.lunix.javagame.engine.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Debugger {
	private static final Logger logger = LogManager.getLogger(Debugger.class);
	private static boolean active;

	public Debugger(@Value("${spring.profiles.active}") String profile) {
		active = profile.equals("dev");
	}

	public static void display(boolean show, String message, Object... objects) {
		if (active && show)
			logger.debug(message, objects);
	}

	public static void display(boolean show, Object... objects) {
		if (active && show)
			logger.debug("{}", objects);
	}
}
