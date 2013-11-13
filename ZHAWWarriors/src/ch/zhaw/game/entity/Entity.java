package ch.zhaw.game.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Entity implements EntityListener {
	private static long FRAME_SPEED[] = { 200l, 200l, 200l, 200l };
	
	private GameScene scene;
	private Sprite sprite;
	private Body body;
	private Category category;
	private float speed;
	private Vector2 target = null;
	private Entity targetEntity = null;
	private EntityListener entityListener = new EntityListenerStub();

	public Entity(GameScene scene, Category category, float x, float y, String texture, boolean body, boolean sensor) {
		this.scene = scene;
		this.category = category;
		this.sprite = new Sprite(scene, this, x, y, scene.getResourceManager().getTexture(texture), scene.getResourceManager().getVboManager());
		scene.registerTouchArea(sprite);
		
		if (body)
			createBody();
		
		if (sensor)
			createSensor();
		
		sprite.setCullingEnabled(true);
	}

	public GameScene getScene() {
		return scene;
	}

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public Body getBody() {
		return body;
	}
	
	public Category getEntityType() {
		return category;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public EntityListener getEntityListener() {
		return entityListener;
	}

	public void setEntityListener(EntityListener entityListener) {
		this.entityListener = entityListener;
	}
	
	public void move(Vector2 position) {
		this.target = position;
		this.targetEntity = null;
	}
	
	public void chase(Entity entity) {
		this.targetEntity = entity;
	}

	public void onUpdate(final float seconds) {
		if (body == null) {
			return;
		}
		
		if (target == null && targetEntity == null) {
			body.setLinearVelocity(0, 0);
			sprite.stopAnimation(0);
			return;
		}
		
		Vector2 position = body.getPosition();
		Vector2 velocity = body.getLinearVelocity();
			
		// chase
		if (targetEntity != null) {
			target = targetEntity.getBody().getPosition();
		}
		
		// calculate distance
		Vector2 distance = distance(target, position);
		if (distance.len() < 1f) {
			if (targetEntity != null) {
				sprite.animate(FRAME_SPEED, 4, 7, true);
				body.setLinearVelocity(0, 0);
			} else {
				target = null;
				body.setLinearVelocity(0, 0);
				sprite.stopAnimation(0);
			}
			return;
		}
		
		// move
		Vector2 targetVelocity = distance.nor().mul(speed);
		body.setLinearVelocity(targetVelocity.sub(velocity));
		
		// flip
		sprite.setFlippedHorizontal(position.x >= target.x);
		
		// animation
		if (!sprite.isAnimationRunning()) {
			sprite.animate(FRAME_SPEED, 0, 3, true);
		}
	}
	
	public void onContact(final Entity entity) {
		entityListener.onContact(entity);
	}
	
	public void onContactEnd(final Entity entity) {
		entityListener.onContactEnd(entity);
	}
	
	public void onSensor(final Entity entity) {
		entityListener.onSensor(entity);
	}
	
	public void onSensorEnd(final Entity entity) {
		entityListener.onSensorEnd(entity);
	}
	
	private void createBody() {		
		FixtureDef fixture = new FixtureDef();
		fixture.filter.groupIndex = (short) -category.ordinal();
		
		body = PhysicsFactory.createCircleBody(scene.getPhysicsWorld(), sprite, BodyType.DynamicBody, fixture);
	    body.setUserData(this);
	    body.setFixedRotation(true);
	    scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));
	}
	
	private void createSensor() {
		CircleShape circleShape = new CircleShape();
	    circleShape.setRadius(10);
	    body.createFixture(circleShape, 0).setSensor(true);
	}
	
	private Vector2 distance(Vector2 a, Vector2 b) {
		return a.cpy().sub(b);
	}
}
