package ch.zhaw.game.control;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.util.Util;

import com.badlogic.gdx.math.Vector2;

public class EnemyController extends EntityController implements IUpdateHandler {
	private Entity target;
	private boolean dead = false;
	private TiledTextureRegion fireTexture;
	
	@Event(filter="create")
	public Entity onCreate() {
		super.create();
		
		entity.getSprite().registerUpdateHandler(this);
		fireTexture = TextureRegionFactory.extractTiledFromTexture(scene.getResourceManager().getTexture("fire.svg"), 8, 1);
		return entity;
	}




	@Event(filter="contact")
	private void onContact(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (!this.entity.isEnemy(entity)) {
			return;
		}
		
		this.entity.move(null);
		entity.move(null);
		if (entity.getSprite() instanceof AnimatedSprite) {
			AnimatedSprite animatedSprite = (AnimatedSprite)entity.getSprite();
			animatedSprite.animate(Entity.FRAME_SPEED, 4, 7, true);
			animatedSprite.setFlippedHorizontal(this.entity.getBody().getPosition().x >= entity.getBody().getPosition().x);
		}
		entityController.onDamage(10);
	}
	
	@Event(filter="contact_end")
	public void onContactEnd(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (!this.entity.isEnemy(entity)) {
			return;
		}
		
		this.entity.move(entity.getBody().getPosition());
	}


	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (dead) {
			Vector2 position = this.entity.getBody().getPosition().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			
			final Entity fire = scene.createEntity("", position.x, position.y, fireTexture, false);
			if (fire.getSprite() instanceof AnimatedSprite) {
				AnimatedSprite animatedSprite = (AnimatedSprite)fire.getSprite();
				animatedSprite.animate(40, new IAnimationListener() {
					@Override
					public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
							int pRemainingLoopCount, int pInitialLoopCount) {
						fire.destroy();
					}
					@Override
					public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {}
					@Override
					public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {}
					@Override
					public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {}
				});
			}
			entity.destroy();
			
			return;
		}
		
		if (target == null) {
			return;
		}
		
		if (Util.distance(entity.getBody().getPosition(), target.getBody().getPosition()) > 1) {
			entity.move(entity.getBody().getPosition());
			return;
		}
		
		entity.move(null);
		if (entity.getSprite() instanceof AnimatedSprite) {
			AnimatedSprite animatedSprite = (AnimatedSprite)entity.getSprite();
			animatedSprite.animate(Entity.FRAME_SPEED, 4, 7, true);
			animatedSprite.setFlippedHorizontal(entity.getBody().getPosition().x >= entity.getBody().getPosition().x);
		}
	}
	
	
	@Override
	public void onDie() {
		dead = true;
	}


	@Override
	public void reset() {}
}
