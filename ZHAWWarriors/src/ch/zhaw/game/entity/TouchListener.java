package ch.zhaw.game.entity;

import com.badlogic.gdx.math.Vector2;

public interface TouchListener {
	public void onTouch(Vector2 position);
	public void onTouch(Entity entity);
}
