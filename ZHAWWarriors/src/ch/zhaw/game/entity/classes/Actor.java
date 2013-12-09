package ch.zhaw.game.entity.classes;

import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.control.EnemyController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public class Actor implements EntityClass {
	private String img;
	
	public Actor(String img) {
		this.img = img;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		TiledTextureRegion texture = scene.getResourceManager().loadTexture(img, 512, 512, 4, 3);
		Entity entity = scene.createEntity(Category.ENEMY, x, y, texture, true, true);
		entity.setSpeed(3);
		entity.setEntityListener(new EnemyController(entity));
		return entity;
	}
	
	
}
