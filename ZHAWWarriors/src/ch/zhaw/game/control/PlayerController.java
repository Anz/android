package ch.zhaw.game.control;

import android.util.Log;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityListener;
import ch.zhaw.game.entity.EntityListenerStub;
import ch.zhaw.game.entity.TouchListener;

import com.badlogic.gdx.math.Vector2;

public class PlayerController extends EntityListenerStub implements EntityListener, TouchListener {
	private static long FRAME_SPEED[] = { 200l, 200l, 200l, 200l };
	private Entity entity;
	
	public PlayerController(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void onContact(Entity entity) {
//		entity.move(null);
//		if (!entity.getSprite().isAnimationRunning()) {
			entity.getSprite().animate(FRAME_SPEED, 4, 8, true);
//		}
	}

	@Override
	public void onTouch(Vector2 position) {
		entity.move(position);
	}

	@Override
	public void onTouch(Entity entity) {
		Log.i("tag", "gotcha");
		entity.chase(entity);
	}
}
