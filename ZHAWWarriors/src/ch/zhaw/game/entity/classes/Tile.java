package ch.zhaw.game.entity.classes;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public class Tile implements EntityClass {
	private String img;
	
	public Tile(String img) {
		super();
		this.img = img;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		TiledTextureRegion texture = scene.getResourceManager().loadTexture(img, 1024, 1024, 1, 1);
		return scene.createEntity(Category.STATIC, x, y, img, false);
	}
	
	
}
