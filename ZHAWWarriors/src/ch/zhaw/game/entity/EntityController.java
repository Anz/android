package ch.zhaw.game.entity;

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
	protected int xFrames = 1;
	protected int yFrames = 1;
	protected boolean dynamic;
	protected boolean pickable;
	protected String img;
	
	public Entity onCreate() {
		TiledTextureRegion region = null;
		if (img == null) {
			region = TextureRegionFactory.extractTiledFromTexture(scene.getResourceManager().getTexture("knight.svg"), 1, 1);
		} else {
			region = TextureRegionFactory.extractTiledFromTexture(scene.getResourceManager().getTexture(img), xFrames, yFrames);
		}
			
		
		entity = scene.createEntity(party, x, y, region, dynamic);
		if (img == null) {
			entity.getSprite().setVisible(false);
			img = "knight.svg";
		}
		entity.setEntityController(this);
		return entity;
	}
	
	public void onTouch() {}
	
	public void onContact(EntityController entity) {}
	
	public void onContactEnd(EntityController entity) {}
	
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
