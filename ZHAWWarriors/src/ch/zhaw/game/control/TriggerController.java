package ch.zhaw.game.control;

import android.app.Activity;
import android.content.Intent;
import ch.zhaw.game.activity.AbstractBaseActivity;
import ch.zhaw.game.activity.GameActivity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.entity.TextHandler;

public class TriggerController extends EntityController {
	protected String event;
	protected String load;
	protected String text;
	
	@Event(filter="create")
	protected void onCreate() {
		super.create();
	}

	@Event
	public final void onAnyEvent(String event) {
		// check event
		if (!event.equals(this.event)) {
			return;
		}
		
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
			camera.getHUD().attachChild(new TextHandler(scene, scene.getResourceManager(), text, 0, camera.getHeight()-200, camera.getWidth(), 200, 0.1f));
		}
	}
}
