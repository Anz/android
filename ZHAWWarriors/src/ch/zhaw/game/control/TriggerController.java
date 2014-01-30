package ch.zhaw.game.control;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;

public class TriggerController extends EntityController  {

	public TriggerController(Entity entity) {
		super(entity);
	}

	@Override
	public void onContact(EntityController entity) {
//		this.entity.destroy();
	}

	@Override
	public void onContactEnd(EntityController entity) {}
}
