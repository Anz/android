package ch.zhaw.game.control;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import ch.zhaw.game.activity.AbstractBaseActivity;
import ch.zhaw.game.activity.GameActivity;
import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.entity.TextHandler;

@Controller(name = "trigger")
public class TriggerController extends EntityController implements IUpdateHandler {
	protected String event;
	protected String load;
	protected String text;
	protected EntityController generate = new EntityController();
	protected boolean triggered = false;
	private int frame = 0;
	
	@Event(filter="create")
	protected void onCreate() {
		super.create();
		scene.registerUpdateHandler(this);
	}

	@Event
	public final void onAnyEvent(String event) {
		// check event
		if (!event.equals(this.event)) {
			return;
		}
		
		triggered = true;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if ("update".equals(event)) {
			triggered = true;
		}
		
		if (!triggered) {
			return;
		}
		triggered = false;
		

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
		
		// generate action
		if (generate != null) {
			try {
				EntityControllerInvoker.invoke(generate, "create");
			} catch (Exception e) {
				Log.e(this.toString(), "cannot generate entity", e);
			}
		}
		
		// if has animation
		if (entity.getSprite() instanceof AnimatedSprite) {
			frame = (frame + 1) % xFrames;
			AnimatedSprite sprite = (AnimatedSprite)entity.getSprite();
			sprite.setCurrentTileIndex(frame);
		}
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
