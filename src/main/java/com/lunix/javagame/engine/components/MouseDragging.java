package com.lunix.javagame.engine.components;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.GameInstance;

public class MouseDragging extends Component {
	private boolean snapToGrid;
	private int gridSize;

	private transient boolean isPicked;

	public void pickup() {
		this.isPicked = true;
	}

	public void place() {
		this.isPicked = false;
	}

	public MouseDragging snapToGrid(boolean enableSnap) {
		this.snapToGrid = enableSnap;
		return this;
	}

	public MouseDragging gridSize(int gridSize) {
		this.gridSize = gridSize;
		return this;
	}

	@Override
	public void update(float deltaTime) {
		if (isPicked) {
			Vector3f worldPos = GameInstance.get().mouse().worldPositionProjected();

			if (snapToGrid) {
				int newX = ((int) worldPos.x / gridSize) * gridSize;
				int newY = ((int) worldPos.y / gridSize) * gridSize;
				int newZ = ((int) worldPos.z / gridSize) * gridSize;

				worldPos.x = (worldPos.x - newX) > (gridSize / 2) ? newX + gridSize : newX;
				worldPos.y = (worldPos.y - newY) > (gridSize / 2) ? newY + gridSize : newY;
				worldPos.z = (worldPos.z - newZ) > (gridSize / 2) ? newZ + gridSize : newZ;
			}

			owner.transform().position(worldPos);
			if (GameInstance.get().mouse().isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
				place();
			}
		}
	}
}