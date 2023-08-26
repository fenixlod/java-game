package com.lunix.javagame.engine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.lunix.javagame.configs.CameraConfigs;
import com.lunix.javagame.configs.ResourceConfigs;
import com.lunix.javagame.configs.WindowConfigs;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.objects.ScreenElement;

@Component
public class Display {
	private static final Logger logger = LogManager.getLogger(Display.class);
	private final ResourceConfigs resourceConfig;
	private final CameraConfigs cameraConfig;
	private final WindowConfigs windowConfigs;
	private final GameWindow window;
	private final Camera camera;
	private final Map<ShaderType, Shader> shaders;
	private Shader currentShader;

	public Display(ResourceConfigs resourceConfig, CameraConfigs cameraConfig, WindowConfigs windowConfigs) {
		this.resourceConfig = resourceConfig;
		this.cameraConfig = cameraConfig;
		this.windowConfigs = windowConfigs;
		this.window = new GameWindow(this.windowConfigs);
		this.camera = new Camera(this.cameraConfig);
		this.shaders = new HashMap<>();
	}

	public void init(MouseListener mouse, KeyboardListener keyboard) throws IOException {
		window.create(mouse, keyboard);
		loadShaders(resourceConfig.shaders());
	}

	private void loadShaders(Map<ShaderType, String> shadersToLoad) throws IOException {
		logger.info("Loading shaders...");
		for (Entry<ShaderType, String> entry : shadersToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading shader: {}", path);
			Shader shader = new Shader(entry.getKey(), path);
			shader.compile();
			shaders.put(entry.getKey(), shader);
		}
	}

	private Shader getShader(ShaderType shaderName) {
		Shader shader = shaders.get(shaderName);
		if (shader == null) {
			logger.warn("Unable to find shader: {}, using the default one", shaderName);
			return shaders.get(ShaderType.DEFAULT);
		}
		return shader;
	}

	public void drawElement(ScreenElement... elements) {
		updateCamera();
		for (ScreenElement element : elements) {
			ShaderType shaderType = element.shader();
			if (currentShader == null || currentShader.type() != shaderType)
				changeShader(shaderType);

			element.draw();
		}
	}

	private void changeShader(ShaderType shader) {
		if (currentShader != null) {
			currentShader.detach();
		}

		currentShader = getShader(shader);
		currentShader.use();
		updateCamera();
	}

	private void updateCamera() {
		if (currentShader != null) {
			currentShader.uploadMat4f("projMat", camera.getProjectionMatrix());
			Matrix4f view = camera.getViewMatrix();
			if (currentShader.type() == ShaderType.NO_PERSPECTIVE) {
				// TODO: Move this logic in the shader if possible
				//System.out.println("----> \n" + view.toString(NumberFormat.getNumberInstance()));
				view.translate(camera.position());
				view.rotate((float) Math.toRadians(-45f), new Vector3f(1f, 0f, 0f));
				view.translate(camera.position().mul(-1, new Vector3f()));
				//System.out.println("<---- \n" + view.toString(NumberFormat.getNumberInstance()));
			}
			currentShader.uploadMat4f("viewMat", view);
		}
	}

	public Camera camera() {
		return camera;
	}

	public void clean() {
		window.destroy();
	}

	public void refresh() {
		window.refresh();
	}

	public void draw() {
		window.draw();
	}

	public boolean isWindowOpen() {
		return window.isOpened();
	}

	public void closeWindow() {
		window.close();
	}

	public void setWindowClearColor(float r, float g, float b, float alpha) {
		window.setClearColor(r, g, b, alpha);
	}

	public float windowAspectRatio() {
		return window.getAspectRatio();
	}
}
