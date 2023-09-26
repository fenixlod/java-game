package com.lunix.javagame.engine.components;

import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.enums.ObjectEventType;

public class SnapToGrid extends Component {
	private int gridSize;

	public SnapToGrid() {
		this(10);
	}

	public SnapToGrid(int gridSize) {
		this.gridSize = gridSize;
	}
	
	@Override
	public void start() {
		super.start();
	}

	@Override
	protected void onNotify(ObjectEventType e) {
		if (e != ObjectEventType.POSITION_CHANGED)
			return;

		Vector3f worldPos = owner.transform().positionCopy();

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
	}
}
