package ch.zhaw.game.scene;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.physics.CollsisionHandler;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.util.FixtureUtil;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScene extends Scene  /*ContactListener,*/ {
	protected ResourceManager resourceManager;
	private GameCamera camera;
	protected PhysicsWorld physicsWorld;
	private List<Entity> entities = new LinkedList<Entity>();
	private List<Entity> addable = new LinkedList<Entity>();
	private List<Entity> removable = new LinkedList<Entity>();

	private float left;
	private float right;
	private float bottom;
	private float top;
	
	public GameScene(GameCamera camera, ResourceManager resourceManager, Vector2 gravity) {
		this.camera = camera;
		this.resourceManager = resourceManager;
		
		physicsWorld = new FixedStepPhysicsWorld(60, gravity, true);
		registerUpdateHandler(physicsWorld);
		
		// set world contact listener
		physicsWorld.setContactListener(new CollsisionHandler());
		
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent event) {
				if (!event.isActionDown()) {
					return false;
				}
				
				for (Entity entity : entities) {
					EntityControllerInvoker.invoke(entity.getEntityController(), "screen", null, new Vector2(event.getX(), event.getY()));
				}
				return false;
			}
		});
		
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				// destroy
				for (Entity entity : removable) {
					entities.remove(entity);
					detachChild(entity.getSprite());
					entity.getBody().getWorld().destroyBody(entity.getBody());
				}
				removable.clear();
				
				// create
				for (Entity entity : addable) {
					entities.add(entity);
				}
				addable.clear();
				
				for (Entity entity : entities) {
					EntityControllerInvoker.invoke(entity.getEntityController(), "update");
				}
				
				// update
				for (Entity entity : entities) {
					entity.onUpdate(pSecondsElapsed);
				}
			}
		});
	}

	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	public void createMap(float left, float right, float bottom, float top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		
		float width = right - left;
		float height = top - bottom;
		
		final float THICKNESS = 100;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
		
		PhysicsFactory.createBoxBody(physicsWorld, left+width/2, top+THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
		PhysicsFactory.createBoxBody(physicsWorld, left+width/2, bottom-THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
		PhysicsFactory.createBoxBody(physicsWorld, left-THICKNESS/2, bottom+height/2, THICKNESS, height, 0, BodyType.StaticBody, fixture);
		PhysicsFactory.createBoxBody(physicsWorld, right+THICKNESS/2, bottom+height/2, THICKNESS, height, 0, BodyType.StaticBody, fixture);
	}
	
	public void createRectangle(float x, float y, float width, float height) {
		Rectangle rect = new Rectangle(x-width/2, y-height/2, width, height, resourceManager.getVboManager());
		rect.setColor(Color.GREEN);
		rect.setZIndex(100000);
		attachChild(rect);
	}
	
	public Entity createEntity(String party, float x, float y, TiledTextureRegion texture, boolean dynamic) {
		Entity entity = new Entity(this, x, y, texture, dynamic);
		if (dynamic) {
			entity.createFixture(FixtureUtil.createCircularFixture(party));
		}
//		entities.add(entity);
		addable.add(entity);
		attachChild(entity.getSprite());
		return entity;
	}
	
	public Entity createEntity(String party, float x, float y, IEntity ientity, boolean dynamic) {
		Entity entity = new Entity(this, x, y, ientity, dynamic);
		if (dynamic) {
			entity.createFixture(FixtureUtil.createCircularFixture(party));
		}
//		entities.add(entity);
		addable.add(entity);
		attachChild(entity.getSprite());
		return entity;
	}
	
	public TextureEntity createTextureEntity(int width, int height, List<ITextureRegion> textures) {
		return new TextureEntity(this, width, height, textures);
	}
	
	public void destroyEntity(Entity entity) {
		if (!entities.contains(entity)) {
			return;
		}
		
		removable.add(entity);
	}
	
//	@Override
//	protected void onManagedUpdate(float seconds) {
//		super.onManagedUpdate(seconds);
//		
//		// destroy
//		for (Entity entity : removable) {
//			entities.remove(entity);
//			detachChild(entity.getSprite());
//			entity.getBody().getWorld().destroyBody(entity.getBody());
//		}
//		removable.clear();
//		
//		// update
//		for (Entity entity : entities) {
//			entity.onUpdate(seconds);
//		}
//	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		// z index
		for (Entity entity : entities) {
			if (entity.isDynamic()) {
				int zindex = Math.round(entity.getBody().getPosition().y * 1000);
				entity.getSprite().setZIndex(zindex);
			}
			
		}
		sortChildren();
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}

	public float getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public float getBottom() {
		return bottom;
	}

	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

	public float getTop() {
		return top;
	}

	public void setTop(float top) {
		this.top = top;
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
	public Entity getEntityById(String id) {
		for (Entity entity : entities) {
			if (id.equals(entity.getEntityController().getId())) {
				return entity;
			}
		}
		return null;
	}

	public GameCamera getCamera() {
		return camera;
	}

	public void setCamera(GameCamera camera) {
		this.camera = camera;
	}
	
}
