package ch.zhaw.game.entity;


public interface EntityListener {
	public void onContact(Entity entity);
	public void onContactEnd(Entity entity);
	public void onSensor(Entity entity);
	public void onSensorEnd(Entity entity);
}
