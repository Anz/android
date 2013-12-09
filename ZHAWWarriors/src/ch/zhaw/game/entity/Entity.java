package ch.zhaw.game.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;;

public class Entity implements EntityController {
	public static long FRAME_SPEED[] = { 200l, 200l, 200l, 200l };
	
	private GameScene scene;
	private Sprite sprite;
	private Body body;
	private Category category;
	private float speed;
	private Vector2 target = null;
	private EntityController entityListener = new EntityControllerStub();

	public Entity(GameScene scene, Category category, float x, float y, String texture, boolean body, boolean sensor) {
		this(scene, category, x, y, scene.getResourceManager().getTexture(texture), body, sensor);
	}
	
	public Entity(GameScene scene, Category category, float x, float y, TiledTextureRegion texture, boolean body, boolean sensor) {
		this.scene = scene;
		this.category = category;
		this.sprite = new Sprite(scene, this, x - texture.getWidth()/2, y - texture.getHeight()/2, texture, scene.getResourceManager().getVboManager());
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
	
	

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
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
	
	public EntityController getEntityListener() {
		return entityListener;
	}

	public void setEntityListener(EntityController entityListener) {
		this.entityListener = entityListener;
	}
	
	public void move(Vector2 position) {
		this.target = position;
	}

	public void onUpdate(final float seconds) {
		if (body == null) {
			return;
		}
		
		if (target == null) {
			return;
		}
		
		body.setAwake(true);
		Vector2 current = body.getPosition();
		
		// flip
		sprite.setFlippedHorizontal(current.x >= target.x);
		
		// animation
		if (!sprite.isAnimationRunning() || sprite.getCurrentTileIndex() > 3) {
			sprite.animate(FRAME_SPEED, 0, 3, true);
		}
		
	    if (!current.equals(target)) {
	        float speed = 7f;
	        Vector2 direction = target.cpy().sub(current);
	        float distanceToTarget = direction.len();
	        float travelDistance = speed * seconds;

	        // the target is very close, so we set the position to the target directly
	        if (distanceToTarget <= travelDistance) {
	            body.setTransform(target, body.getAngle());
	            target = null;
	            return;
	        } else {
	            direction.nor();
	            // move a bit in the target direction 
	            body.setTransform(current.cpy().add(direction.mul(travelDistance)), body.getAngle());
	            sprite.stopAnimation();
	        }
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
	
	public void destroy() {
		scene.destroyEntity(this);
	}
	
	private void createBody() {		
		FixtureDef fixture = new FixtureDef();
		fixture.filter.groupIndex = (short) -category.ordinal();
		
//		body = PhysicsFactory.createCircleBody(scene.getPhysicsWorld(), sprite, BodyType.DynamicBody, fixture);
		final float[] sceneCenterCoordinates = sprite.getSceneCenterCoordinates();
		body = PhysicsFactory.createCircleBody(scene.getPhysicsWorld(), sceneCenterCoordinates[0], sceneCenterCoordinates[1], 0.5f * PIXEL_TO_METER_RATIO_DEFAULT, BodyType.DynamicBody, fixture);
//		body = PhysicsFactory.createBoxBody(scene.getPhysicsWorld(), sceneCenterCoordinates[0], sceneCenterCoordinates[1], PIXEL_TO_METER_RATIO_DEFAULT, PIXEL_TO_METER_RATIO_DEFAULT, BodyType.DynamicBody, fixture);
	    body.setUserData(this);
	    body.setFixedRotation(true);
	    scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(sprite, body, true, false));
	}
	
	private void createSensor() {
		CircleShape circleShape = new CircleShape();
	    circleShape.setRadius(10);
	    body.createFixture(circleShape, 0).setSensor(true);
	}
}
