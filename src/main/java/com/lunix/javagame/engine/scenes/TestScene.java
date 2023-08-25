package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.lunix.javagame.engine.GameInstance;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.objects.Rectangle;
import com.lunix.javagame.engine.graphic.objects.ScreenElement;
import com.lunix.javagame.engine.graphic.objects.Triangle;

public class TestScene extends Scene {
	private ScreenElement rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6, character, camera;
	private List<ScreenElement> staticElements;

	@Override
	public void init(GameInstance game) {
		super.init(game);
		game.display().setWindowClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		// game.camera().setOrthoProjection();
		game.camera().setPerspectiveProjection(game.display().windowAspectRatio());
		game.camera().setPosition(new Vector3f());
		
		// draw cuboid with dimensions: x=50, y=30, z=10
		// front
		rectangle1 = new ScreenElement();
		rectangle1.shape(Rectangle.sized(new Vector3f(25.0f, 15.0f, 0.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.red)
			.shader(ShaderType.DEFAULT)
			.upload();
		
		// right
		rectangle2 = new ScreenElement();
		rectangle2.shape(Rectangle.sized(new Vector3f(50.0f, 15.0f, 5.0f),
				10, new Vector3f(0.0f, 0.0f, -1.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.green)
			.shader(ShaderType.DEFAULT)
			.upload();

		//back
		rectangle3 = new ScreenElement();
		rectangle3.shape(Rectangle.sized(new Vector3f(25.0f, 15.0f, 10.0f),
				50, new Vector3f(-1.0f, 0.0f, 0.0f), 
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.black)
			.shader(ShaderType.DEFAULT)
			.upload();
		
		//left
		rectangle4 = new ScreenElement();
		rectangle4.shape(Rectangle.sized(new Vector3f(0.0f, 15.0f, 5.0f),
				10, new Vector3f(0.0f, 0.0f, 1.0f),
				30, new Vector3f(0.0f, 1.0f, 0.0f)))
			.color(Color.blue)
			.shader(ShaderType.DEFAULT)
			.upload();
		
		//top
		rectangle5 = new ScreenElement();
		rectangle5.shape(Rectangle.sized(new Vector3f(25.0f, 30.0f, 5.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f), 
				10, new Vector3f(0.0f, 0.0f, -1.0f)))
			.color(Color.cyan)
			.shader(ShaderType.DEFAULT)
			.upload();
		
		rectangle6 = new ScreenElement();
		rectangle6.shape(Rectangle.sized(new Vector3f(25.0f, 0.0f, 5.0f),
				50, new Vector3f(1.0f, 0.0f, 0.0f),
				10, new Vector3f(0.0f, 0.0f, 1.0f)))
			.color(Color.magenta)
			.shader(ShaderType.DEFAULT)
			.upload();
		
		character = new ScreenElement();
		character.shape(Rectangle.sized(new Vector3f(-50.0f, 0.0f, 20.0f),
				20, new Vector3f(1.0f, 0.0f, 0.0f),
				40, new Vector3f(0.0f, 0.0f, 1.0f)))
				.color(Color.orange)
				.shader(ShaderType.NO_PERSPECTIVE)
			.upload();
		
		camera = new ScreenElement();
		camera.shape(Triangle.equilateral(new Vector3f(0.0f, 0.0f, 0.0f), 10,
				new Vector3f(1.0f, 0.0f, 0.0f),
				new Vector3f(0.0f, 0.0f, 1.0f)))
			.color(Color.black)
				.shader(ShaderType.DEFAULT)
			.upload();

		staticElements = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			ScreenElement element = new ScreenElement();
			element.shape(Rectangle.sized(new Vector3f(i * 5.0f, 0.0f, 0.0f),
					1, new Vector3f(1.0f, 0.0f, 0.0f),
					1, new Vector3f(0.0f, 1.0f, 0.0f)))
					.color(new Color(0.0f, 0.0f, 1.0f, i * 0.05f))
				.shader(ShaderType.DEFAULT)
				.upload();
			staticElements.add(element);
			
			element = new ScreenElement();
			element.shape(Rectangle.sized(new Vector3f(i * -5.0f, 0.0f, 0.0f),
					1, new Vector3f(1.0f, 0.0f, 0.0f),
					1, new Vector3f(0.0f, 1.0f, 0.0f)))
				.color(new Color(0.0f, 0.0f, 1.0f, i * 0.05f))
				.shader(ShaderType.DEFAULT)
				.upload();
			staticElements.add(element);
			
			element = new ScreenElement();
			element.shape(Rectangle.sized(new Vector3f(0.0f, i * 5.0f, 0.0f),
					1, new Vector3f(1.0f, 0.0f, 0.0f),
					1, new Vector3f(0.0f, 1.0f, 0.0f)))
					.color(new Color(1.0f, 0.0f, 0.0f, i * 0.05f))
				.shader(ShaderType.DEFAULT)
				.upload();
			staticElements.add(element);
			
			element = new ScreenElement();
			element.shape(Rectangle.sized(new Vector3f(0.0f, i * -5.0f, 0.0f),
					1, new Vector3f(1.0f, 0.0f, 0.0f),
					1, new Vector3f(0.0f, 1.0f, 0.0f)))
				.color(new Color(1.0f, 0.0f, 0.0f, i * 0.05f))
				.shader(ShaderType.DEFAULT)
				.upload();
			staticElements.add(element);
		}
	}

	@Override
	public void update(float deltaTime, GameInstance game) {
		Vector3f offset = new Vector3f();

		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT))
			offset.x += 50.0f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT))
			offset.x -= 50.0f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_UP))
			offset.y += 50.0f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN))
			offset.y -= 50.0f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP))
			offset.z += 50.0f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN))
			offset.z -= 50.0f * deltaTime;
		
		float zoomChange = (float) game.mouse().getScrollY() * 0.1f;
		game.camera().move(offset);
		if (zoomChange != 0.0f)
			game.camera().changeZoom(zoomChange);
		
		camera.move(offset);
		camera.upload();
		// logger.info(mouse);
		// logger.info(keyboard);
		// logger.info("Time elapsed: {}", time.getElapsedTimeInSeconds());
		// logger.info("Delta time: {}", time.getDeltaTime());

		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.display().closeWindow();
			logger.info("Escape button pressed. Close the game window");
		}

		game.display().drawElement(rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6, character,
				camera);

		game.display().drawElement(staticElements.toArray(ScreenElement[]::new));
	}
}
