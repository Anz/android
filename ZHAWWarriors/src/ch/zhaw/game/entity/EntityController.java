package ch.zhaw.game.entity;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;

import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.util.FixtureUtil;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public class EntityController {
	protected GameScene scene;
	protected String layer;
	protected Entity entity;
	protected String id;
	protected String party;
	protected int life = 100;
	protected float speed; 
	protected float x;
	protected float y;
	protected int zindex;
	protected int xFrames = 1;
	protected int yFrames = 1;
	protected boolean dynamic;
	protected boolean pickable;
	protected String img;
	protected long animate;
	
	protected float width;
	protected float height;
	
	// graphic
	protected Color color = new Color(1, 1, 1, 1);
	
	// physic
	protected String type = "static";
	protected String rotation = "fixed";
	protected String bound = "none";
	protected Vector2 impulse = new Vector2();

	protected Entity create() {
		TiledTextureRegion region = null;
		if (img == null) {
			Rectangle rect = new Rectangle(x - width/2, y - height/2, width, height, scene.getResourceManager().getVboManager());
			entity = scene.createEntity(party, x, y, rect, dynamic);
		} else {
			region = TextureRegionFactory.extractTiledFromTexture(scene.getResourceManager().getTexture(img), xFrames, yFrames);
			entity = scene.createEntity(party, x, y, region, dynamic);
		}
		
		if ("dynamic".equals(type)) {
			entity.getBody().setType(BodyType.DynamicBody);
		}
		
		if ("box".equals(bound)) {
			entity.createFixture(FixtureUtil.createBoxFixture(
					party, 
					width/2/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
					height/2/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
		}
		
		
		if ("dynamic".equals(rotation)) {
			entity.getBody().setFixedRotation(false);
		}
		
		entity.getSprite().setZIndex(zindex);
		entity.getSprite().setColor(color);
		entity.setEntityController(this);
		
		entity.getBody().applyLinearImpulse(impulse, entity.getBody().getLocalCenter());
		
		return entity;
	}
	
	public void onDamage(int damage) {
		life -= damage;
		
		if (life < 0) {
			onDie();
			life = 0;
		}
	}
	
	public void onDie() {}

	/*******************
	 * Getter / Setter *
	 *******************/
	
	public GameScene getScene() {
		return scene;
	}

	public void setScene(GameScene scene) {
		this.scene = scene;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getxFrames() {
		return xFrames;
	}

	public void setxFrames(int xFrames) {
		this.xFrames = xFrames;
	}

	public int getyFrames() {
		return yFrames;
	}

	public void setyFrames(int yFrames) {
		this.yFrames = yFrames;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isPickable() {
		return pickable;
	}

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
