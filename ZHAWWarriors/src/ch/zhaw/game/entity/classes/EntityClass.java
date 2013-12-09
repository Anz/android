package ch.zhaw.game.entity.classes;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;

public interface EntityClass {
	public Entity createInstance(GameScene scene, float x, float y);
}
