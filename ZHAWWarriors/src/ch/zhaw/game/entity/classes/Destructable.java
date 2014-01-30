package ch.zhaw.game.entity.classes;

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
		scene.getResourceManager().loadTexture(img, 1024, 256, 4, 1);
		Entity entity = scene.createEntity(Category.ENEMY, x, y, img, true);
		entity.getSprite().setScale(.3f);
		return entity;
	}
	
	
}
