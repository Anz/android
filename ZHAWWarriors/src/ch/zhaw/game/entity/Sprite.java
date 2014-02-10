package ch.zhaw.game.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import ch.zhaw.game.scene.GameScene;

public class Sprite extends AnimatedSprite {
	private GameScene scene;
	private Entity entity;

	public Sprite(GameScene scene, Entity entity, float x, float y, ITiledTextureRegion texture) {
		super(x, y, texture, scene.getResourceManager().getVboManager());
		this.scene = scene;
		this.entity = entity;
		
		scene.registerTouchArea(this);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (!pSceneTouchEvent.isActionDown()) {
			return false;
		}
		entity.onTouch();
		scene.onSceneTouchEvent(entity);
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

}
