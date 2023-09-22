package com.lunix.javagame.engine.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Physics {
	private Vec2 gravity;
	private World world;
	private float physicsTime;
	private float physicsTimeStep;
	private short velocityIterations;
	private short positionIterations;

	public Physics() {
		gravity = new Vec2(0, 0);
		world = new World(gravity);
		physicsTime = 0;
		physicsTimeStep = 1f / 60;// We target 60 FPS
		velocityIterations = 8;
		positionIterations = 3;
	}

	public void update(float deltaTime) {
		physicsTime += deltaTime;
		if (physicsTime > 0) {
			physicsTime -= physicsTimeStep;
			world.step(physicsTimeStep, velocityIterations, positionIterations);
		}
	}
}
