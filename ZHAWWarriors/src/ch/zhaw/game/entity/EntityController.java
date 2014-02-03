package ch.zhaw.game.entity;


public class EntityController {
	protected Entity entity;
	
	public EntityController(Entity entity) {
		this.entity = entity;
	}
	
	public void onTouch() {}
	
	public void onContact(EntityController entity) {}
	
	public void onContactEnd(EntityController entity) {}

	public Entity getEntity() {
		return entity;
	}
}
