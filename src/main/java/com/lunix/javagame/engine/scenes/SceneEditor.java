package com.lunix.javagame.engine.scenes;

import java.util.Map.Entry;

import org.joml.Vector2f;

import com.lunix.javagame.engine.ResourcePool;
import com.lunix.javagame.engine.Scene;
import com.lunix.javagame.engine.controlls.EditorControlls;
import com.lunix.javagame.engine.enums.ShaderType;
import com.lunix.javagame.engine.enums.TextureType;
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

public class SceneEditor extends SceneExecutor {
	private FrameBuffer frameBuffer;
	private GameViewWindow viewWindow;
	private ObjectInspector objInspector;
	private SceneHierarchyWindow sceneHierarchy;
	private EditorControlls controlls;
	private EditorMenuBar menuBar;

	public SceneEditor() {
		super();
		viewWindow = new GameViewWindow();
		objInspector = new ObjectInspector();
		controlls = new EditorControlls();
		menuBar = new EditorMenuBar();
		sceneHierarchy = new SceneHierarchyWindow();
	}

	@Override
	public void init() throws Exception {
		super.init();
		viewWindow.init();
		objInspector.init();
		controlls.init();
		frameBuffer = new FrameBuffer(game.window().windowSize().x, game.window().windowSize().y);
		ResourcePool.loadResources(ShaderType.PICKING, ShaderType.DEBUG, TextureType.ARROW);
	}

	@Override
	public void start(Scene scene) throws Exception {
		currentScene = scene;
		controlls.start(game.camera(), currentScene);
		objInspector.start(currentScene);
		currentScene.start();
		game.window().uiLayer().setViewWindow(viewWindow);
	}

	@Override
	public void destroy() {
		super.destroy();
		game.window().uiLayer().setViewWindow(null);
	}

	@Override
	public void update(float deltaTime) throws Exception {
		controlls.update(deltaTime);
		objInspector.update(deltaTime, currentScene);
		currentScene.update(deltaTime, false);
		Debugger.drawAxis(true);
		Debugger.outlineSelected(true, currentScene);
		currentScene.render();
	}


	@Override
	public void newFrame() {
		super.newFrame();
		frameBuffer.bind();
		game.window().clearColor(1f, 1f, 1f, 1f);
	}

	@Override
	public void ui() {
		setupDockspace();
		menuBar.show();
		sceneHierarchy.show(currentScene);
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
					controlls.placeGround(entry.getKey());
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
}
