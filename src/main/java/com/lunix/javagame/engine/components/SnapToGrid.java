package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;

public class SnapToGrid extends Component {
	private int gridSize;
	private transient Vector3f lastPosition;

	public SnapToGrid() {
		this(100);
	}

	public SnapToGrid(int gridSize) {
		this.gridSize = gridSize;
	}
	
	@Override
	public void start() {
		super.start();
		lastPosition = new Vector3f();
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		Vector3f worldPos = owner.transform().position();
		if (lastPosition.equals(worldPos))
			return;

		int newX = ((int) worldPos.x / gridSize) * gridSize;
		int newY = ((int) worldPos.y / gridSize) * gridSize;
		int newZ = ((int) worldPos.z / gridSize) * gridSize;

		float offsetX = worldPos.x - newX;
		float offsetY = worldPos.y - newY;
		float offsetZ = worldPos.z - newZ;

		worldPos.x = offsetX > (gridSize / 2) ? newX + gridSize : offsetX < (-gridSize / 2) ? newX - gridSize : newX;
		worldPos.y = offsetY > (gridSize / 2) ? newY + gridSize : offsetY < (-gridSize / 2) ? newY - gridSize : newY;
		worldPos.z = offsetZ > (gridSize / 2) ? newZ + gridSize : offsetZ < (-gridSize / 2) ? newZ - gridSize : newZ;

		owner.transform().position(worldPos);
		lastPosition = worldPos;
	}

	/**
	 * All Components needs to implement this method. This value determine the order
	 * of execution of components within a game object. The lower the priority value
	 * = the sooner this component will be executed. Priority of 1 - first to
	 * execute, 1000 - last to execute.
	 * 
	 * @return
	 */
	public static int priority() {
		return 300;
	}
}
