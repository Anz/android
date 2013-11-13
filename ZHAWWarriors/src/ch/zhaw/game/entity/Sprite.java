package ch.zhaw.game.entity;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ch.zhaw.game.scene.GameScene;

public class Sprite extends AnimatedSprite {
	private GameScene scene;
	private Entity entity;

	public Sprite(GameScene scene, Entity entity, float x, float y, ITiledTextureRegion texture, VertexBufferObjectManager vbo) {
		super(x, y, texture, vbo);
		this.scene = scene;
		this.entity = entity;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		scene.onSceneTouchEvent(entity);
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

}
