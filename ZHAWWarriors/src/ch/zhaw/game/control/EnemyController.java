package ch.zhaw.game.control;

import java.util.Random;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.util.MathUtil;

import com.badlogic.gdx.math.Vector2;

@Controller(name="enemy")
public class EnemyController extends EntityController {
	private Entity target;
	private boolean dead = false;
	private TiledTextureRegion fireTexture;
	
	@Event(filter="create")
	public Entity onCreate() {
		super.create();
		
//		entity.getSprite().registerUpdateHandler(this);
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


	@Event(filter="update")
	public void onUpdate() {
		if (dead) {
			Vector2 position = this.entity.getBody().getPosition().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			
			final Entity fire = scene.createEntity("", position.x, position.y, fireTexture, false);
			if (fire.getSprite() instanceof AnimatedSprite) {
				AnimatedSprite animatedSprite = (AnimatedSprite)fire.getSprite();
				animatedSprite.animate(40, new IAnimationListener() {
					@Override
					public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,	int pRemainingLoopCount, int pInitialLoopCount) {
						EntityController entityController = new DefaultController();
						entityController.setX(fire.getSprite().getX());
						entityController.setY(fire.getSprite().getY());
//						entityController.setPickable(true);
						entityController.setDynamic(true);
						entityController.setxFrames(4);
						entityController.setyFrames(3);
						entityController.setScene(fire.getScene());
						
						switch (new Random().nextInt(2)) {
							default:
							case 0: entityController.setImg("witch_hat.svg"); break;
							case 1: entityController.setImg("witch_cloth.svg"); break;
						}
						
						EntityControllerInvoker.invoke(entityController, "create");
						
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
		
		if (MathUtil.distance(entity.getBody().getPosition(), target.getBody().getPosition()) > 1) {
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
}
