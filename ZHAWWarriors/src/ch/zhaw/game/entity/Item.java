package ch.zhaw.game.entity;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.scene.GameScene;

public class Item implements EntityClass {
	private String img;
	private String texture;
	
	public Item(String img, String texture) {
		super();
		this.img = img;
		this.texture = texture;
	}

	public String getImg() {
		return img;
	}

	public String getTexture() {
		return texture;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		TiledTextureRegion texture = scene.getResourceManager().loadTexture(img, 512, 512, 4, 3);
		return scene.createEntity(Category.ITEM, x - texture.getWidth()/2, y - texture.getHeight()/2, img, true, false);
	}
	
	
}
