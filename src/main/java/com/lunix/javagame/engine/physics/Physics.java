package com.lunix.javagame.engine.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector3f;

import com.lunix.javagame.engine.GameObject;
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
		gravity = new Vec2(0, 10);
		world = new World(gravity);
		physicsTime = 0;
		physicsTimeStep = 1f / 60;// We target 60 FPS
		velocityIterations = 8;
		positionIterations = 3;
	}

	public void addGameObject(GameObject object) {
		RigidBody rigidBody = object.getComponent(RigidBody.class);
		if (rigidBody != null && rigidBody.rawBody() == null) {
			Vector3f position = object.transform().positionCopy();
			BodyDef bodyDefinition = new BodyDef();
			bodyDefinition.angle = 0f;
			bodyDefinition.position.set(position.x, position.y);
			bodyDefinition.angularDamping = rigidBody.angularDamping();
			bodyDefinition.linearDamping = rigidBody.linearDamping();
			bodyDefinition.fixedRotation = rigidBody.isFixedRotation();
			bodyDefinition.bullet = rigidBody.isContiniousCollision();
			bodyDefinition.type = switch (rigidBody.bodyType()) {
				case KINEMATIC -> BodyType.KINEMATIC;
				case DYNAMIC -> BodyType.DYNAMIC;
				case STATIC -> BodyType.STATIC;
				default -> BodyType.STATIC;
			};


			CircleCollider cCollider;
			BoxCollider bCollider;
			Shape bodyShape = null;

			if ((cCollider = object.getComponent(CircleCollider.class)) != null) {
				CircleShape shape = new CircleShape();
				shape.setRadius(cCollider.radius());
				bodyShape = shape;
			} else if ((bCollider = object.getComponent(BoxCollider.class)) != null) {
				PolygonShape shape = new PolygonShape();
				Vec2 halfSize = new Vec2(bCollider.size().x, bCollider.size().y).mul(0.5f);
				Vec2 offset = new Vec2(bCollider.offset().x, bCollider.offset().y);

				shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset), 0);
				bodyDefinition.position.set(bodyDefinition.position.add(offset));
				bodyShape = shape;
			}

			Body body = world.createBody(bodyDefinition);
			rigidBody.rawBody(body);
			body.createFixture(bodyShape, rigidBody.mass());
		}
	}

	public void removeGameObject(GameObject object) {
		RigidBody rigidBody = object.getComponent(RigidBody.class);
		if (rigidBody != null && rigidBody.rawBody() != null) {
			world.destroyBody(rigidBody.rawBody());
			rigidBody.rawBody(null);
		}
	}

	public void update(float deltaTime, boolean isPlaying) {
		if (!isPlaying)
			return;

		physicsTime += deltaTime;
		if (physicsTime > 0) {
			physicsTime -= physicsTimeStep;
			world.step(physicsTimeStep, velocityIterations, positionIterations);
		}
	}
}
