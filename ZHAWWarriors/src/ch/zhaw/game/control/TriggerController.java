package ch.zhaw.game.control;

import android.app.Activity;
import android.content.Intent;
import ch.zhaw.game.activity.AbstractBaseActivity;
import ch.zhaw.game.activity.GameActivity;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.entity.TextHandler;

public class TriggerController extends EntityController {
	private static final String EVENT_CREATE = "create";
	private static final String EVENT_TOUCH = "touch";
	private static final String EVENT_CONTACT = "contact";
	
	protected String event;
	protected String load;
	protected String text;

	
	protected void onEvent(String event) {
		// load action
		if (load != null) {
			Activity activity = entity.getScene().getResourceManager().getActivity();
			if ("".equals(load)) {
				activity.finish();
			} else {
				Intent intent = new Intent(activity, GameActivity.class);
			    intent.putExtra(AbstractBaseActivity.KEY_MAP, load);
			    activity.startActivity(intent);
			}
		}
		
		// text action
		if (text != null) {
			GameCamera camera = scene.getCamera();
			//scene.attachChild(new TextHandler(scene, scene.getResourceManager(), text, 0, camera.getHeight()-200, camera.getWidth(), 200, 0.1f));
			camera.getHUD().attachChild(new TextHandler(scene, scene.getResourceManager(), text, 0, camera.getHeight()-200, camera.getWidth(), 200, 0.1f));
		}
	}
	
	
	@Override
	public Entity onCreate() {
		super.onCreate();
		checkEvent(EVENT_CREATE);
		return entity;
	}



	private void checkEvent(String event) {
		// check event
		if (event.equals(this.event)) {
			onEvent(event);
		}
	}

	@Override
	public void onTouch() {
		checkEvent(EVENT_TOUCH);
	}

	@Override
	public void onContact(EntityController entity) {
		checkEvent(EVENT_CONTACT);
	}
}
