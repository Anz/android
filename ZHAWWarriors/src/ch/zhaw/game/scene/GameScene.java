package ch.zhaw.game.scene;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.TouchListener;
import ch.zhaw.game.physics.CollsisionHandler;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameScene extends Scene  /*ContactListener,*/ {
	protected ResourceManager resourceManager;
	protected PhysicsWorld physicsWorld;
	private List<Entity> entities = new LinkedList<Entity>();
	private Set<Entity> removable = new HashSet<Entity>();
	private TouchListener touchListener = null;
	private float left;
	private float right;
	private float bottom;
	private float top;
	
	public GameScene(ResourceManager resourceManager, float left, float right, float bottom, float top) {
		this.resourceManager = resourceManager;
		
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), true);
		registerUpdateHandler(physicsWorld);
		
		// set world contact listener
		physicsWorld.setContactListener(new CollsisionHandler());
		
		// create map
		createMap(left, right, bottom, top);
	}

	public PhysicsWorld getPhysicsWorld() {
		return physicsWorld;
	}
	
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	
	private void createMap(float left, float right, float bottom, float top) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		
		float width = right - left;
		float height = top - bottom;
		
		final float THICKNESS = 100;
		FixtureDef fixture = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
//		PhysicsFactory.createBoxBody(physicsWorld, width/2, height+THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, width/2, -THICKNESS/2, width, THICKNESS, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, -THICKNESS/2, height/2, THICKNESS, height, 0, BodyType.StaticBody, fixture);
//		PhysicsFactory.createBoxBody(physicsWorld, width+THICKNESS/2, height/2, THICKNESS, height, 0, BodyType.StaticBody, fixture);
		
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
	
	public Entity createEntity(Category category, float x, float y, String texture, boolean dynamic) {
		return createEntity(category, x, y, TextureRegionFactory.extractTiledFromTexture(resourceManager.getTexture(texture), 1, 1), dynamic);
	}
	
	public Entity createEntity(Category category, float x, float y, TiledTextureRegion texture, boolean dynamic) {
		Entity entity = new Entity(this, category, x, y, texture, dynamic);
		if (dynamic) {
			entity.createFixture(GameSceneFactory.createCircularFixture(category));
		}
		entities.add(entity);
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
	
	@Override
	protected void onManagedUpdate(float seconds) {
		super.onManagedUpdate(seconds);
		
		// destroy
		for (Entity entity : removable) {
			entities.remove(entity);
			detachChild(entity.getSprite());
			entity.getBody().getWorld().destroyBody(entity.getBody());
		}
		removable.clear();
		
		// update
		for (Entity entity : entities) {
			entity.onUpdate(seconds);
		}
	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		// z index
		for (Entity entity : entities) {
			int zindex = -1000000;
			
			if (entity.getEntityType() != Category.STATIC) {
				zindex = Math.round(entity.getBody().getPosition().y * 1000);
			}
			entity.getSprite().setZIndex(zindex);
		}
		sortChildren();
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
			if (id.equals(entity.getId())) {
				return entity;
			}
		}
		return null;
	}
	
}
