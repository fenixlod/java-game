package com.lunix.javagame.engine.scenes;

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
import com.lunix.javagame.engine.ui.EditorMenuBar;
import com.lunix.javagame.engine.ui.GameViewWindow;
import com.lunix.javagame.engine.ui.ObjectInspector;
import com.lunix.javagame.engine.ui.SceneHierarchyWindow;
import com.lunix.javagame.engine.util.Debugger;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class LevelEditorScene extends Scene {
	private EditorConfigs editorConfig;
	private FrameBuffer frameBuffer;
	private GameViewWindow viewWindow;
	private ObjectInspector objInspector;
	private SceneHierarchyWindow sceneHierarchy;
	private EditorControlls controlls;
	private EditorMenuBar menuBar;

	public LevelEditorScene() {
		super();
		viewWindow = new GameViewWindow();
		objInspector = new ObjectInspector();
		controlls = new EditorControlls(game.camera(), this);
		menuBar = new EditorMenuBar();
		sceneHierarchy = new SceneHierarchyWindow();
	}

	@Override
	public void init(boolean doLoad) throws Exception {
		super.init(doLoad);
		editorConfig = game.editorConfig();
		controlls.init();
		game.window().uiLayer().setViewWindow(viewWindow);
		frameBuffer = new FrameBuffer(game.window().windowSize().x, game.window().windowSize().y);
		ResourcePool.loadResources(ShaderType.DEFAULT, ShaderType.PICKING, ShaderType.DEBUG,
				TextureType.PLAYER, TextureType.ENEMY, TextureType.PLAYER_IDLE, TextureType.TILE_BRICK,
				TextureType.ARROW);
		
		objInspector.init(this);

		if (loaded)
			return;

		GameObject playerObject = new GameObject("Player")
			.addComponent(
						new SpriteRenderer(4, 5)
				.sprite(ResourcePool.getSprite(TextureType.PLAYER.name()))
			);
		playerObject.addComponent(new Animation(ResourcePool.getSprites(TextureType.PLAYER_IDLE), 0.3f));
		addGameObject(playerObject);
			

		GameObject enemy = new GameObject("Enemy", new Vector3f(-5f, 5f, 0f))
			.addComponent(
						new SpriteRenderer(2, 4)
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
	public void update(float deltaTime, boolean isPlaying) throws Exception {
		controlls.update(deltaTime);
		objInspector.update(deltaTime, this);
		super.update(deltaTime, isPlaying);
		Debugger.drawAxis(true);
		Debugger.outlineSelected(true, this);
	}

	@Override
	public void ui() {
		setupDockspace();
		menuBar.show();
		sceneHierarchy.show(this);
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

		viewWindow.show(frameBuffer);
		// ImGui.showDemoWindow();
		ImGui.end();
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

	@Override
	public void resume() {
		super.resume();
		game.window().uiLayer().setViewWindow(viewWindow);
	}
}
