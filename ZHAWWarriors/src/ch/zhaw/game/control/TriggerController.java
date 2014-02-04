package ch.zhaw.game.control;

import android.app.Activity;
import android.content.Intent;
import ch.zhaw.game.activity.AbstractBaseActivity;
import ch.zhaw.game.activity.GameActivity;
import ch.zhaw.game.entity.EntityController;

public class TriggerController extends EntityController {
	private static final String EVENT_TOUCH = "touch";
	private static final String EVENT_CONTACT = "contact";
	
	protected String event;
	protected String load;

	protected void onEvent(String event) {
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
	}

	@Override
	public void onTouch() {
		onEvent(EVENT_TOUCH);
	}

	@Override
	public void onContact(EntityController entity) {
		onEvent(EVENT_CONTACT);
	}
}
