package ch.zhaw.game.control;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.EntityControllerStub;

public class EnemyController extends EntityControllerStub implements EntityController {
	private Entity entity;
	private boolean contact = false;
	
	public EnemyController(Entity entity) {
		this.entity = entity;
	}

	
	@Override
	public void onContact(Entity entity) {
		if (entity.getEntityType() != Category.PLAYER) {
			return;
		}
		
		this.entity.move(null);
		this.entity.getSprite().animate(Entity.FRAME_SPEED, 4, 7, true);
	}
	
	@Override
	public void onContactEnd(Entity entity) {
		if (entity.getEntityType() != Category.PLAYER) {
			return;
		}
		
		this.entity.move(entity.getBody().getPosition());
	}

	@Override
	public void onSensor(Entity entity) {		
		if (entity.getEntityType() != Category.PLAYER || contact) {
			return;
		}
		
		this.entity.move(entity.getBody().getPosition());
	}
}
