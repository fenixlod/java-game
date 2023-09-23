package com.lunix.javagame.engine.scenes;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Map.Entry;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.lunix.javagame.configs.EditorConfigs;
import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Prefabs;
import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.components.Animation;
import com.lunix.javagame.engine.components.MouseDragging;
import com.lunix.javagame.engine.components.SpriteRenderer;
import com.lunix.javagame.engine.controlls.EditorControlls;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
import com.lunix.javagame.engine.graphic.Color;
import com.lunix.javagame.engine.graphic.FrameBuffer;
import com.lunix.javagame.engine.graphic.Sprite;
import com.lunix.javagame.engine.ui.GameViewWindow;
import com.lunix.javagame.engine.ui.ObjectInspector;
import com.lunix.javagame.engine.util.Debugger;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class LevelEditorScene extends Scene {
	private GameObject playerObject;
	private GameObject draggingObject;
	private EditorConfigs editorConfig;
	private FrameBuffer frameBuffer;
	private GameViewWindow viewWindow;
	private ObjectInspector objInspector;
	private EditorControlls controlls;

	public LevelEditorScene() {
		super();
		viewWindow = new GameViewWindow();
		objInspector = new ObjectInspector();
		controlls = new EditorControlls(game.camera());
	}

	@Override
	public void init() throws Exception {
		editorConfig = game.editorConfig();
		controlls.init();
		game.window().uiLayer().setViewWindow(viewWindow);
		frameBuffer = new FrameBuffer(game.window().windowSize().x, game.window().windowSize().y);
		ResourcePool.loadResources(ShaderType.DEFAULT, ShaderType.PICKING, ShaderType.DEBUG,
				TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE, TextureType.TILE_BRICK,
				TextureType.ARROW);
		
		Debugger.drawAxis(true);
		objInspector.init(this);
		super.init();// TODO: loading game objects need to be after initialization of gizmo tools for
						// now otherwise they are not going to work correctly. After implementing of
						// event based system this will not be needed anymore
		// Debugger.addBox(new Vector3f(10, 10, 0), 20, 20);
		// Debugger.addCircle(new Vector3f(40, 40, 0.1f), VectorUtil.Z(), 70);

		if (loaded)
			return;

		playerObject = new GameObject("Player")
				.addComponent(
						new SpriteRenderer(40, 50)
						.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
				);
		playerObject.addComponent(new Animation(ResourcePool.getSprites(TextureType.PLAYER_IDLE), 0.3f));
			addGameObject(playerObject);
			

		GameObject enemy = new GameObject("Enemy", new Vector3f(-50f, 50f, 0f))
			.addComponent(
				new SpriteRenderer(20, 40)
					.color(Color.red())
			);
		addGameObject(enemy);

/*		
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
*/
/*		
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
*/
	}

	@Override
	public void update(float deltaTime) throws Exception {
		// Close the game window when escape key is pressed
		if (game.keyboard().isKeyPressed(GLFW_KEY_ESCAPE)) {
			if (draggingObject != null) {
				MouseDragging component = draggingObject.getComponent(MouseDragging.class);
				if (component.isPicked()) {
					removeGameObject(draggingObject);
					draggingObject = null;
				}
			}
		}
/*
		Vector3f worldPosition = game.mouse().worldPositionProjected();
		System.out.println("Current X=" + worldPosition.x + " Y=" + worldPosition.y + " Z=" + worldPosition.z);
*/
		controlls.update(deltaTime);
		objInspector.update(deltaTime, this);
		super.update(deltaTime);
	}

	@Override
	public void ui() {
		setupDockspace();

		objInspector.show();
		ImGui.begin("World Editor");

		ImVec2 windowPos = new ImVec2();
		ImGui.getWindowPos(windowPos);
		ImVec2 windowSize = new ImVec2();
		ImGui.getWindowSize(windowSize);
		ImVec2 itemSpacing = new ImVec2();
		ImGui.getStyle().getItemSpacing(itemSpacing);
		float windowX2 = windowPos.x + windowSize.x;
		int i = 0;
		for (Entry<String, Sprite> entry : ResourcePool.sprites().entrySet()) {
			try {
				Sprite sp = entry.getValue();
				TextureType textureType = sp.texture();

				if (textureType == TextureType.NONE)
					continue;

				float spriteWidth = 60;
				float spriteHeight = 60;
				int id = ResourcePool.getTexture(textureType).id();
				Vector2f[] textureCoords = sp.textureCoords();

				ImGui.pushID(i);
				if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoords[3].x, textureCoords[3].y,
						textureCoords[1].x, textureCoords[1].y)) {
					logger.info("Sprite {} clicked", entry.getKey());
					GameObject groundTile = Prefabs.groundTile(new Vector3f(0f, 0f, 0f),
							editorConfig.gridSize(), editorConfig.gridSize(),
							entry.getKey());
					// Attach the ground object to the mouse cursor
					groundTile.addComponent(new MouseDragging().pickup());
					draggingObject = groundTile;
					addGameObject(groundTile);
				}
				ImGui.popID();

				ImVec2 lastButtonPos = new ImVec2();
				ImGui.getItemRectMax(lastButtonPos);
				float lastButtonX2 = lastButtonPos.x;
				float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

				if (i + 1 < ResourcePool.sprites().size() && nextButtonX2 < windowX2) {
					ImGui.sameLine();
				}
				i++;
			} catch (Exception e) {
				logger.error(e);
			}
		}

		ImGui.end();

		viewWindow.show(frameBuffer, this);
		ImGui.showDemoWindow();
		ImGui.end();
	}

	@Override
	protected void sceneLoaded(GameObject[] loadedData) {
		for (GameObject obj : loadedData) {
			if (obj.name().equals("Player")) {
				playerObject = obj;
				return;
			}
		}
	}

	@Override
	public void newFrame() {
		super.newFrame();
		frameBuffer.bind();
		game.window().clearColor(1f, 1f, 1f, 1f);
	}

	@Override
	public void endFrame() {
		super.endFrame();
		frameBuffer.unbind();
	}

	private void setupDockspace() {
		int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
		ImGuiViewport mainViewport = ImGui.getMainViewport();
		ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY(), ImGuiCond.Always);
		ImGui.setNextWindowSize(mainViewport.getWorkSizeX(), mainViewport.getWorkSizeY());
		ImGui.setNextWindowViewport(mainViewport.getID());
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
		windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize
				| ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

		ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
		ImGui.popStyleVar(2);

		// Dockspace
		ImGui.dockSpace(ImGui.getID("Dockspace"));
	}

	@Override
	public void stop() {
		super.stop();
		game.window().uiLayer().setViewWindow(null);
	}
}
