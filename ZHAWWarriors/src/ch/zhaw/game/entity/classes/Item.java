package ch.zhaw.game.entity.classes;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public class Item implements EntityClass {
	private String img;
	private String texture;
	
	public Item(String img, String texture) {
		this.img = img;
		this.texture = texture;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		TiledTextureRegion texture = scene.getResourceManager().loadTexture(img, 512, 512, 4, 3);
		return scene.createEntity(Category.ITEM, x, y, img, true, false);
	}
	
	
}
