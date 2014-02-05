package ch.zhaw.game.control;

import org.andengine.engine.handler.IUpdateHandler;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.util.Util;

public class EnemyController extends EntityController implements IUpdateHandler {
	private Entity target;
	
	@Override
	public Entity onCreate() {
		super.onCreate();
		
		entity.getSprite().registerUpdateHandler(this);
		return entity;
	}




	@Override
	public void onContact(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (!this.entity.isEnemy(entity)) {
			return;
		}
		
		this.entity.move(null);
		this.entity.getSprite().animate(Entity.FRAME_SPEED, 4, 7, true);
		this.entity.getSprite().setFlippedHorizontal(this.entity.getBody().getPosition().x >= entity.getBody().getPosition().x);
		entityController.onDamage(10);
	}
	
	@Override
	public void onContactEnd(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (!this.entity.isEnemy(entity)) {
			return;
		}
		
		this.entity.move(entity.getBody().getPosition());
	}


	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (target == null) {
			return;
		}
		
		if (Util.distance(entity.getBody().getPosition(), target.getBody().getPosition()) > 1) {
			entity.move(entity.getBody().getPosition());
			return;
		}
		
		entity.move(null);
		entity.getSprite().animate(Entity.FRAME_SPEED, 4, 7, true);
		entity.getSprite().setFlippedHorizontal(entity.getBody().getPosition().x >= entity.getBody().getPosition().x);
	}
	
	
	@Override
	public void onDie() {
		entity.destroy();
	}


	@Override
	public void reset() {}

//	@Override
//	public void onSensor(Entity entity) {		
//		if (entity.getEntityType() != Category.PLAYER || contact) {
//			return;
//		}
//		
//		this.entity.move(entity.getBody().getPosition());
//	}
}
