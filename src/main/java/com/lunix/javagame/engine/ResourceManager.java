package com.lunix.javagame.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.ResourceConfigs;
import com.lunix.javagame.engine.graphic.Shader;

@Component
public class ResourceManager {
	private static final Logger logger = LogManager.getLogger(ResourceManager.class);
	private final ResourceConfigs resourceConfig;
	private final Map<String, Shader> shaders;
	
	public ResourceManager(ResourceConfigs resourceConfig) {
		this.resourceConfig = resourceConfig;
		this.shaders = new HashMap<>();
	}

	public void loadAll() throws IOException {
		loadShaders();
	}

	private void loadShaders() throws IOException {
		logger.info("Loading shaders...");
		for (Entry<String, String> entry : resourceConfig.shaders().entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading shader: {}", path);
			Shader shader = new Shader(path);
			shader.compile();
			shaders.put(entry.getKey(), shader);
		}
	}

	public Shader getShader(String shaderName) {
		Shader shader = shaders.get(shaderName);
		if (shader == null) {
			logger.warn("Unable to find shader: {}, using the default one", shaderName);
			return shaders.get("default");
		}
		return shader;
	}
}
