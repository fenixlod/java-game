package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.lunix.javagame.engine.Editor;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.GameObjectFactory;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.exception.ResourceNotFound;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.graphic.Texture;
import com.lunix.javagame.engine.util.Debugger;
import com.lunix.javagame.engine.util.VectorUtil;

import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorScene extends Scene {
	private GameObject playerObject;
	protected GameObject currentObject;

	@Override
	public void init() throws Exception {
		super.init();
		game.window().setClearColor(1f, 1f, 1f, 1f);
		game.camera().setOrthoProjection();
		game.camera().setPosition(new Vector3f());
		ResourcePool.loadResources(ShaderType.DEFAULT, TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE,
				TextureType.TILE_BRICK);
		
		if (loaded)
			return;

		this.playerObject = new GameObject("Player")
				.addComponent(
						new SpriteRenderer(40, 50)
						.sprite(new Sprite(TextureType.PLAYER))
				);
		playerObject.addComponent(new Animation(ResourcePool.getSpriteSheet(TextureType.PLAYER_IDLE), 0.3f));
			addGameObject(playerObject);
			
		for (int j = 0; j < 4; j++) {
			int reverse = j > 1 ? -1 : 1;
			int isXAxis = j % 2 * reverse;
			int isYAxis = (1 - Math.abs(isXAxis)) * reverse;
			for (int i = 0; i < 100; i++) {
				GameObject obj = new GameObject("Marker " + i + " - " + j, new Vector3f(i * isXAxis * 5f, i * isYAxis * 5f, 0f))
					.addComponent(
						new SpriteRenderer(1,1)
							.color(isXAxis != 0 ? Color.blue() : Color.red())
							.widthDirection(VectorUtil.X())
							.heightDirection(VectorUtil.Y())
					);
				addGameObject(obj);
			}
		}
		
		GameObject enemy = new GameObject("Enemy", new Vector3f(-50f, 50f, 0f))
			.addComponent(
				new SpriteRenderer(20, 40)
					.color(Color.red())
			);
		addGameObject(enemy);
		currentObject = enemy;
		
		// draw cuboid with dimensions: x=20, y=20, z=20
		// front
		GameObject rectangle = new GameObject("Cube1", new Vector3f(10f, 0f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.blue())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		// right
		rectangle = new GameObject("Cube2", new Vector3f(20f, 10f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.red())
					.widthDirection(VectorUtil.Y())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);

		//back
		rectangle = new GameObject("Cube3", new Vector3f(10f, 20f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.black())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.Z())
			);
		addGameObject(rectangle);
		
		//left
		rectangle = new GameObject("Cube4", new Vector3f(0f, 10f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.yellow())
					.widthDirection(VectorUtil.minusY())
					.heightDirection(VectorUtil.Z())
					);
		addGameObject(rectangle);
		
		//top
		rectangle = new GameObject("Cube5", new Vector3f(10f, 0f, 20f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.cyan())
					.widthDirection(VectorUtil.X())
					.heightDirection(VectorUtil.Y())
			);
		addGameObject(rectangle);
		
		
		//bottom
		rectangle = new GameObject("Cube6", new Vector3f(10f, 20f, 0f))
			.addComponent(
				new SpriteRenderer(20, 20)
					.color(Color.magenta())
					.widthDirection(VectorUtil.minusX())
					.heightDirection(VectorUtil.minusY())
			);
		addGameObject(rectangle);
		
		enemy = new GameObject("Enemy1", new Vector3f(0f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.red())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY, 0))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy2", new Vector3f(100f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.green())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY, 1))
				);
		addGameObject(enemy);
		
		enemy = new GameObject("Enemy3", new Vector3f(200f, -100f, 0f))
				.addComponent(
					new SpriteRenderer(50, 100)
						.color(Color.blue())
						.widthDirection(VectorUtil.viewX())
						.heightDirection(VectorUtil.viewY())
						.sprite(ResourcePool.getSprite(TextureType.ENEMY, 2))
				);
		addGameObject(enemy);
		
		enemy = GameObjectFactory.GroundTile(new Vector3f(0f, 0f, 0f), TextureType.TILE_BRICK, 100, 100);
		addGameObject(enemy);
		
//		logger.info("Creating game objects...");
//		for (int i = 0; i < 200; i++) {
//			float yPos = (100 - i) * 100f;
//			for(int j = 0; j < 200; j++) {
//				float xPos = (100 - j) * 100f;
//				GameObject ground = new GameObject("Ground " + i + " - " + j, new Vector3f(xPos, yPos, 0f))
//						.addComponent(
//							new SpriteRenderer(100, 100)
//								.heightDirection(VectorUtil.Y())
//								.color(new Color(0f, 1f, 0f, 0.5f))
//								.shader(ShaderType.DEFAULT)
//								.isStatic(true)
//						);
//				addGameObject(ground);
//			}
//		}

//		String value = game.save(playerObject);
//		System.out.println(value);
//		GameObject ob = game.load(value, GameObject.class);
//		System.out.println("Yey");
//		playerObject = ob;
	}

	@Override
	public void update(float deltaTime) throws Exception {
		Vector3f offset = new Vector3f();

		if (game.keyboard().isKeyPressed(GLFW_KEY_RIGHT))
			offset.x += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_LEFT))
			offset.x -= 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_UP))
			offset.y += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_DOWN))
			offset.y -= 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_UP))
			offset.z += 50f * deltaTime;
		
		if (game.keyboard().isKeyPressed(GLFW_KEY_PAGE_DOWN))
			offset.z -= 50f * deltaTime;
		
		float zoomChange = (float) game.mouse().getScrollY() * 0.1f;
		if (zoomChange != 0f)
			game.camera().changeZoom(zoomChange);
		
		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			game.window().close();
			logger.info("Escape button pressed. Close the game window");
		}

		Debugger.display(false, "X={}, Y={}, Z={}", game.camera().position().x, game.camera().position().y,	game.camera().position().z);

		this.playerObject.move(offset);
		game.camera().setPosition(playerObject.transform().position());

		game.mouse().worldPosition();
		super.update(deltaTime);
	}

	public void inspector() {
		if (currentObject != null) {
			ImGui.begin("Inspector");
			Editor.editObject(currentObject);
			// currentObject.ui();
			ImGui.end();
		}
	}

	@Override
	public void ui() {
		inspector();
		ImGui.begin("World Editor");

		ImVec2 windowPos = new ImVec2();
		ImGui.getWindowPos(windowPos);
		ImVec2 windowSize = new ImVec2();
		ImGui.getWindowSize(windowSize);
		ImVec2 itemSpacing = new ImVec2();
		ImGui.getStyle().getItemSpacing(itemSpacing);
		float windowX2 = windowPos.x + windowSize.x;
		for (int i = 0; i < 1; i++) {
			Texture texture = null;
			try {
				texture = ResourcePool.getTexture(TextureType.TILE_BRICK);
			} catch (ResourceNotFound | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Sprite sp = new Sprite(TextureType.TILE_BRICK);
			float spriteWidth = texture.width() / 4;
			float spriteHeight = texture.height() / 4;
			int id = texture.id();
			Vector2f[] textureCoords = sp.textureCoords();

			ImGui.pushID(i);
			if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoords[0].x, textureCoords[0].y,
					textureCoords[2].x, textureCoords[2].y)) {
				System.out.println("Texture clicked");
			}
			ImGui.popID();

			ImVec2 lastButtonPos = new ImVec2();
			ImGui.getItemRectMax(lastButtonPos);
			float lastButtonX2 = lastButtonPos.x;
			float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

			if (i + 1 < 1 && nextButtonX2 < windowX2) {
				ImGui.sameLine();
			}
		}

		ImGui.end();
	}

	@Override
	protected void sceneLoaded(GameObject[] loadedData) {
		for (GameObject obj : loadedData) {
			if (obj.name().equals("Player")) {
				playerObject = obj;
				currentObject = obj;
				return;
			}
		}
	}
}