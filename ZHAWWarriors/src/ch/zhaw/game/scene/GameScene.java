package ch.zhaw.game.scene;

import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.TouchListener;
import ch.zhaw.game.resource.ResourceManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends Scene implements ContactListener, IOnSceneTouchListener {
	protected ResourceManager resourceManager;
	protected PhysicsWorld physicsWorld;
	private List<Entity> entities = new LinkedList<Entity>();
	private TouchListener touchListener = null;
	
	public GameScene(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
		
		setBackground(new Background(0.3f, 0.3f, 0.3f));
		
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false);
		registerUpdateHandler(physicsWorld);
		
		// set world contact listener
		physicsWorld.setContactListener(this);
		
		// set touch listener
		setOnSceneTouchListener(this);
	}

	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	public void createMap(float width, float height) {
		final float THICKNESS = 50;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
		PhysicsFactory.createBoxBody(physicsWorld, width/2, height/2+THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, width/2, -height/2-THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, -THICKNESS/2, 0, THICKNESS, height, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, width+THICKNESS/2, 0, THICKNESS, height, 0, BodyType.StaticBody, fixture);
	}
	
	public Entity createEntity(Category category, float x, float y, String texture, boolean body, boolean sensor) {
		Entity entity = new Entity(this, category, x, y, texture, body, sensor);
		entities.add(entity);
		attachChild(entity.getSprite());
		return entity;
	}
	
	@Override
	protected void onManagedUpdate(float seconds) {
		super.onManagedUpdate(seconds);
		
		for (Entity entity : entities) {
			entity.onUpdate(seconds);
		}
	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		// z index
		for (Entity entity : entities) {
			switch (entity.getEntityType()) {
				case STATIC: 
					entity.getSprite().setZIndex(-1000000);
					break;
				default:
					entity.getSprite().setZIndex(Math.round(entity.getBody().getPosition().y * 1000));
					break;
			}
		}
		sortChildren();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (touchListener != null) {
			Vector2 target = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()).mul(1f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			touchListener.onTouch(target);
		}
		return false;
	}
	
	public boolean onSceneTouchEvent(Entity entity) {
		if (touchListener != null) {
			touchListener.onTouch(entity);
		}
		return false;
	}
	
	public void registerTouchListener(TouchListener touchListener) {
		this.touchListener = touchListener;
	}

	@Override
	public void beginContact(Contact contact) {
		Object userdataA = contact.getFixtureA().getBody().getUserData();
		Object userdataB = contact.getFixtureB().getBody().getUserData();
		
		if (!(userdataA instanceof Entity && userdataB instanceof Entity)) {
			return;
		}
		
		Entity entityA = (Entity) userdataA;
		Entity entityB = (Entity) userdataB;
		
		if (contact.getFixtureA().isSensor()) {
			entityA.onSensor(entityB);
		} else if (!contact.getFixtureB().isSensor()) {
			entityA.onContact(entityB);
		}
		
		if (contact.getFixtureB().isSensor()) {
			entityB.onSensor(entityA);
		} else if (!contact.getFixtureA().isSensor()) {
			entityB.onContact(entityA);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object userdataA = contact.getFixtureA().getBody().getUserData();
		Object userdataB = contact.getFixtureB().getBody().getUserData();
		
		if (!(userdataA instanceof Entity && userdataB instanceof Entity)) {
			return;
		}
		
		Entity entityA = (Entity) userdataA;
		Entity entityB = (Entity) userdataB;
		
		if (contact.getFixtureA().isSensor()) {
			entityA.onSensorEnd(entityB);
		} else if (!contact.getFixtureB().isSensor()) {
			entityA.onContactEnd(entityB);
		}
		
		if (contact.getFixtureB().isSensor()) {
			entityB.onSensorEnd(entityA);
		} else if (!contact.getFixtureA().isSensor()) {
			entityB.onContactEnd(entityA);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
}
