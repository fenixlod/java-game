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
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Shader;
import com.lunix.javagame.engine.graphic.Texture;
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
	private final Map<TextureType, Texture> textures;
	private Shader currentShader;

	public Display(ResourceConfigs resourceConfig, CameraConfigs cameraConfig, WindowConfigs windowConfigs) {
		this.resourceConfig = resourceConfig;
		this.cameraConfig = cameraConfig;
		this.windowConfigs = windowConfigs;
		this.window = new GameWindow(this.windowConfigs);
		this.camera = new Camera(this.cameraConfig);
		this.shaders = new HashMap<>();
		this.textures = new HashMap<>();
	}

	public void init(MouseListener mouse, KeyboardListener keyboard) throws IOException {
		window.create(mouse, keyboard);
		loadShaders(resourceConfig.shaders());
		loadTextures(resourceConfig.textures());
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

	private void loadTextures(Map<TextureType, String> texturesToLoad) throws IOException {
		logger.info("Loading textures...");
		for (Entry<TextureType, String> entry : texturesToLoad.entrySet()) {
			Path path = new ClassPathResource(entry.getValue()).getFile().toPath();
			logger.debug("Loading texture: {}", path);
			Texture texture = new Texture(entry.getKey(), path);
			texture.load();
			textures.put(entry.getKey(), texture);
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

	private Texture getTexture(TextureType textureTYpe) throws Exception {
		Texture texture = textures.get(textureTYpe);
		if (texture == null) {
			logger.warn("Unable to find texture: {}, textureTYpe");
			throw new Exception("Texture not found");
		}
		return texture;
	}

	public void drawElement(ScreenElement... elements) {
		updateCamera();
		for (ScreenElement element : elements) {
			ShaderType shaderType = element.shader();
			if (currentShader == null || currentShader.type() != shaderType)
				changeShader(shaderType);

			setTextureToShader(element.texture());
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
				//The idea is to set the camera to be perpendicular to the plane of the element,
				// this way it will be desplayed without any prespective
				//System.out.println("----> \n" + view.toString(NumberFormat.getNumberInstance()));
				view.translate(camera.position());
				view.rotate((float) Math.toRadians(-45f), new Vector3f(1f, 0f, 0f));
				view.translate(camera.position().mul(-1, new Vector3f()));
				//System.out.println("<---- \n" + view.toString(NumberFormat.getNumberInstance()));
			}
			currentShader.uploadMat4f("viewMat", view);
		}
	}

	private void setTextureToShader(TextureType texture) {
		if (texture == null) {
			Texture.unbind();
			currentShader.uploadTexture("textureSlot", 0);
			currentShader.uploadBoolean("useTexture", false);
			return;
		}

		try {
			Texture currentTexture = getTexture(texture);
			currentShader.uploadTexture("textureSlot", 0);
			currentShader.uploadBoolean("useTexture", true);
			currentTexture.bind(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
