package ch.zhaw.game.entity;

import ch.zhaw.game.scene.GameScene;

public interface EntityClass {
	public Entity createInstance(GameScene scene, float x, float y);
}
