package ch.zhaw.game.entity.classes;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public class Destructable implements EntityClass {
	private String img;
	
	public Destructable(String img) {
		this.img = img;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		TiledTextureRegion texture = scene.getResourceManager().loadTexture(img, 512, 512, 1, 1);
		return scene.createEntity(Category.ITEM, x - texture.getWidth()/2, y - texture.getHeight()/2, img, true, false);
	}
	
	
}
