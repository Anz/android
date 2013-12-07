package ch.zhaw.game.entity;


public class EntityControllerStub implements EntityController {
	@Override
	public void onContact(Entity entity) {}
	
	@Override
	public void onContactEnd(Entity entity) {}
	
	@Override
	public void onSensor(Entity entity) {}
	
	@Override
	public void onSensorEnd(Entity entity) {}
}
