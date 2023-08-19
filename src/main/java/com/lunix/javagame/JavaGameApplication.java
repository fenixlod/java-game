package com.lunix.javagame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import com.lunix.javagame.engine.GameInstance;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JavaGameApplication implements CommandLineRunner {
	private static final Logger logger = LogManager.getLogger(JavaGameApplication.class);

	@Autowired
	private GameInstance game;

	
	public static void main(String[] args) {
		SpringApplication.run(JavaGameApplication.class, args);
	}

	@Override
    public void run(String... args) {
		logger.info("Starting the java game...");
		game.run();
		logger.info("The java game is closing...");
    }
}
