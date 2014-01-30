package ch.zhaw.game.entity.classes;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public class Trigger implements EntityClass {
	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		return scene.createEntity(Category.ITEM, x, y, "witch2.png", true);
	}

}
