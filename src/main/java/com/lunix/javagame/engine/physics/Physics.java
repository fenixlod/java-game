package com.lunix.javagame.engine.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.lunix.javagame.engine.GameObject;
import com.lunix.javagame.engine.Transform;
import com.lunix.javagame.engine.components.BoxCollider;
import com.lunix.javagame.engine.components.CircleCollider;
import com.lunix.javagame.engine.components.RigidBody;

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

	public void addGameObject(GameObject object) {
		RigidBody rigidBody = object.getComponent(RigidBody.class);
		if (rigidBody != null && rigidBody.rawBody() != null) {
			Transform transform = object.transform();
			BodyDef bodyDefinition = new BodyDef();
			bodyDefinition.angle = 0f;
			bodyDefinition.position.set(transform.position().x, transform.position().y);
			bodyDefinition.angularDamping = rigidBody.angularDamping();
			bodyDefinition.linearDamping = rigidBody.linearDamping();
			bodyDefinition.fixedRotation = rigidBody.isFixedRotation();
			bodyDefinition.bullet = rigidBody.isContiniousCollision();
			switch (rigidBody.bodyType()) {
				case KINEMATIC:
					bodyDefinition.type = BodyType.KINEMATIC;
					break;
				case DYNAMIC:
					bodyDefinition.type = BodyType.DYNAMIC;
					break;
				case STATIC:
				default:
					bodyDefinition.type = BodyType.STATIC;
					break;
			}

			PolygonShape shape = new PolygonShape();
			CircleCollider cCollider;
			BoxCollider bCollider;

			if ((cCollider = object.getComponent(CircleCollider.class)) != null) {
				shape.setRadius(cCollider.radius());
			} else if ((bCollider = object.getComponent(BoxCollider.class)) != null) {
				Vec2 halfSize = new Vec2(bCollider.size().x, bCollider.size().y).mul(0.25f);
				Vec2 offset = new Vec2(bCollider.offset().x, bCollider.offset().y);

				shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset), 0);
				bodyDefinition.position.set(bodyDefinition.position.add(offset));
			}

			Body body = world.createBody(bodyDefinition);
			rigidBody.rawBody(body);
			body.createFixture(shape, rigidBody.mass());
		}
	}

	public void update(float deltaTime) {
		physicsTime += deltaTime;
		if (physicsTime > 0) {
			physicsTime -= physicsTimeStep;
			world.step(physicsTimeStep, velocityIterations, positionIterations);
		}
	}
}
