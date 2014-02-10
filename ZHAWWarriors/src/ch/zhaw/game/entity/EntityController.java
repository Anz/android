package ch.zhaw.game.entity;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.scene.GameScene;


public class EntityController {
	protected GameScene scene;
	protected Entity entity;
	protected String id;
	protected String party;
	protected int life;
	protected float speed; 
	protected float x;
	protected float y;
	protected int z;
	protected int xFrames = 1;
	protected int yFrames = 1;
	protected boolean dynamic;
	protected boolean pickable;
	protected String img;
	protected long animate;
	
	protected float width;
	protected float height;
	protected float red;
	protected float green;
	protected float blue;

	protected Entity create() {
		TiledTextureRegion region = null;
		if (img == null) {
			Rectangle rect = new Rectangle(x - width/2, y - height/2, width, height, scene.getResourceManager().getVboManager());
			rect.setColor(red, green, blue);
			entity = scene.createEntity(party, x, y, rect, dynamic);
		} else {
			region = TextureRegionFactory.extractTiledFromTexture(scene.getResourceManager().getTexture(img), xFrames, yFrames);
			entity = scene.createEntity(party, x, y, region, dynamic);
		}
		entity.getSprite().setZIndex(z);
		entity.setEntityController(this);
		
		if (animate > 0) {
			if (entity.getSprite() instanceof AnimatedSprite) {
				AnimatedSprite animatedSprite = (AnimatedSprite)entity.getSprite();
				animatedSprite.animate(animate);
			}
		}
		
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
