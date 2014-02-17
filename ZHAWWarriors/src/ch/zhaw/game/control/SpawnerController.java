package ch.zhaw.game.control;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.json.JSONObject;

import android.util.Log;
import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.entity.Event;

@Controller(name="spawner")
public class SpawnerController extends EntityController {
	private EntityController generate = new DefaultController();

	@Event(filter="create")
	public void onCreate() {
		super.create();
		
		try {
			final JSONObject color = new JSONObject();
			color.put("red", 1);
			color.put("green", 0);
			color.put("blue", 0);
			color.put("alpha", 1);
			
			final JSONObject impulse = new JSONObject();
			impulse.put("y", -15);
			
			final JSONObject object = new JSONObject();
			object.put("x", entity.getSprite().getX());
			object.put("y", entity.getSprite().getY());
			object.put("width", 50f);
			object.put("height", 50f);
			object.put("color", color);
			object.put("zindex", 10000);
			object.put("type", "dynamic");
			object.put("bound", "box");
			object.put("rotation", "dynamic");
			object.put("impulse", impulse);
			
			generate.setScene(scene);
			
			TimerHandler timerHandler = new TimerHandler(1f, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler timerHandler) {
					try {
						EntityControllerInvoker.invoke(generate, "create");
					} catch (Exception e) {
						Log.e("", "could not create element", e);
					}
				}
			});
			timerHandler.setAutoReset(true);
			scene.registerUpdateHandler(timerHandler);
		} catch (Exception e) {
			Log.e("", "could not create spawner", e);
		}
	}
}
