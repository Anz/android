package ch.zhaw.game.entity;

import org.andengine.entity.IEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.util.MathUtil;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Entity {
	public static long FRAME_SPEED[] = { 200l, 200l, 200l, 200l };
	
	private GameScene scene;
	private IEntity sprite;
	private Body body;
	private boolean dynamic;
	private Vector2 target = null;
	private EntityController entityController = new EntityController();
	private long lastTouch = 0;
	
	public Entity(GameScene scene, float x, float y, TiledTextureRegion texture, boolean dynamic) {
		this.scene = scene;
		if (texture != null) {
			this.sprite = new Sprite(scene, this, x - texture.getWidth()/2, y - texture.getHeight()/2, texture);
		}
		this.dynamic = dynamic;
		if (sprite instanceof RectangularShape) {
			scene.registerTouchArea((RectangularShape)sprite);
		}
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
		bodyDef.position.x = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		bodyDef.position.y = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		this.body = scene.getPhysicsWorld().createBody(bodyDef);
		
		this.body.setUserData(this);
		this.body.setFixedRotation(true);
		if (sprite instanceof RectangularShape) {
			scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector((RectangularShape)sprite, this.body, true, true));
		}
		
		sprite.setCullingEnabled(true);
	}
	
	public Entity(GameScene scene, float x, float y, IEntity ientity, boolean dynamic) {
		this.scene = scene;
		this.sprite = ientity;
		this.dynamic = dynamic;
		if (sprite instanceof RectangularShape) {
			scene.registerTouchArea((RectangularShape)sprite);
		}
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = dynamic ? BodyType.DynamicBody : BodyType.StaticBody;
		bodyDef.position.x = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		bodyDef.position.y = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		this.body = scene.getPhysicsWorld().createBody(bodyDef);
		
		this.body.setUserData(this);
		this.body.setFixedRotation(true);
		if (sprite instanceof IAreaShape) {
			scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector((IAreaShape)sprite, this.body, true, true));
		}
		
		sprite.setCullingEnabled(true);
	}


	public GameScene getScene() {
		return scene;
	}

	public IEntity getSprite() {
		return sprite;
	}

	public void setSprite(IEntity sprite) {
		this.sprite = sprite;
	}

	public Body getBody() {
		return body;
	}
	
	public void move(Vector2 position) {
		this.target = position;
	}
	
	public void onTouch() {	
		long current = System.currentTimeMillis();
		if (current - lastTouch < 500) {
			return;
		}
		lastTouch = current;
		
		if (entityController != null) {
//			entityController.onTouch();
			EntityControllerInvoker.invoke(entityController, "touch");
		}
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
		if (sprite instanceof AnimatedSprite) {
			AnimatedSprite animatedSprite = (AnimatedSprite)sprite;
			animatedSprite.setFlippedHorizontal(current.x >= target.x);
			
			// animation
			if (!animatedSprite.isAnimationRunning() || animatedSprite.getCurrentTileIndex() > 3) {
				animatedSprite.animate(FRAME_SPEED, 0, 3, true);
			}
		}
		
		body.setTransform(MathUtil.move(current, target, entityController.getSpeed(), seconds), body.getAngle());
	}
	
	public void destroy() {
		scene.destroyEntity(this);
	}
	
	public void createFixture(FixtureDef fixtureDef) {
		Fixture fixture = body.createFixture(fixtureDef);
		EntityController entityController = new EntityController();
		entityController.setEntity(this);
		fixture.setUserData(entityController);
	}
	
	public void setEntityController(EntityController entityController) {
		this.entityController = entityController;
		for (Fixture fixture : body.getFixtureList()) {
			fixture.setUserData(entityController);
		}
	}
	
	public boolean isEnemy(Entity entity) {
		if (entityController.getParty() == null || entity.getEntityController().getParty() == null) {
			return false;
		}
		
		if ("".equals(entityController.getParty()) || "".equals(entity.getEntityController().getParty())) {
			return false;
		}
		
		return !entityController.getParty().equals(entity.getEntityController().getParty());
	}


	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}


	public static long[] getFRAME_SPEED() {
		return FRAME_SPEED;
	}


	public Vector2 getTarget() {
		return target;
	}


	public EntityController getEntityController() {
		return entityController;
	}
	
	
}
