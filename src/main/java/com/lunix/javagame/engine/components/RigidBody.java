package com.lunix.javagame.engine.components;

import org.jbox2d.dynamics.Body;
import org.joml.Vector3f;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.enums.BodyType;

public class RigidBody extends Component {
	private Vector3f velocity;
	private float angularDamping;
	private float linearDamping;
	private float mass;
	private BodyType bodyType;
	private boolean fixedRotation;
	private boolean continiousCollision;
	private transient Body rawBody;

	public RigidBody() {
		velocity = new Vector3f();
		angularDamping = 0.8f;
		linearDamping = 0.9f;
		mass = 0;
		bodyType = BodyType.STATIC;
		fixedRotation = false;
		continiousCollision = true;
	}

	@Override
	public void update(float deltaTime, boolean isPlaying) {
		if (rawBody != null && isPlaying) {
			owner.transform().position(new Vector3f(rawBody.getPosition().x, rawBody.getPosition().y, 0));
			// Math.toDegrees(rawBody.getAngle());
			// owner.transform().facing(velocity);
		}
	}

	public Vector3f velocity() {
		return velocity;
	}

	public RigidBody velocity(Vector3f velocity) {
		this.velocity = velocity;
		return this;
	}

	public float angularDamping() {
		return angularDamping;
	}

	public RigidBody angularDamping(float angularDamping) {
		this.angularDamping = angularDamping;
		return this;
	}

	public float linearDamping() {
		return linearDamping;
	}

	public RigidBody linearDamping(float linearDamping) {
		this.linearDamping = linearDamping;
		return this;
	}

	public float mass() {
		return mass;
	}

	public RigidBody mass(float mass) {
		this.mass = mass;
		return this;
	}

	public BodyType bodyType() {
		return bodyType;
	}

	public RigidBody bodyType(BodyType bodyType) {
		this.bodyType = bodyType;
		return this;
	}

	public boolean isFixedRotation() {
		return fixedRotation;
	}

	public RigidBody fixedRotation(boolean fixedRotation) {
		this.fixedRotation = fixedRotation;
		return this;
	}

	public boolean isContiniousCollision() {
		return continiousCollision;
	}

	public RigidBody continiousCollision(boolean continiousCollision) {
		this.continiousCollision = continiousCollision;
		return this;
	}

	public Body rawBody() {
		return rawBody;
	}

	public RigidBody rawBody(Body rawBody) {
		this.rawBody = rawBody;
		return this;
	}
}
