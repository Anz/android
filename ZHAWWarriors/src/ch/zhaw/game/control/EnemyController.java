package ch.zhaw.game.control;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityListener;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.EntityListenerStub;

public class EnemyController extends EntityListenerStub implements EntityListener {
	private Entity entity;
	
	public EnemyController(Entity entity) {
		this.entity = entity;
	}

	
//	@Override
//	public void onContact(Entity entity) {
//		this.entity.chase(null);
//	}


	@Override
	public void onSensor(Entity enemy) {		
		if (enemy.getEntityType() != Category.PLAYER) {
			return;
		}
		
		this.entity.chase(enemy);
	}
}
