package ch.zhaw.game.control;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import android.util.Log;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.util.ReflectionUtil;

public class AnimateController extends EntityController {
	protected String event;
	protected String property;
	protected float target;
	protected float speed;
	protected float interval;
	protected Field field;
	protected Method getter;
	protected Method setter;
	protected boolean higher;
	
	
	@Event
	public void onEvent(String name) {
		if (!name.equals(event)) {
			return;
		}
		
		try {
			field = ReflectionUtil.getField(this.entity.getSprite(), property);
			getter = ReflectionUtil.getMethod(this.entity.getSprite(), "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1), new Class<?>[0]);
			setter = ReflectionUtil.getMethod(this.entity.getSprite(), "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1), new Class<?>[] { float.class });
			
			if (field == null && (getter == null || setter == null)) {
				return;
			}
			
//			higher = (Float)ReflectionUtil.get(entity.getSprite(), property) < target;
			higher = getValue() < target;
			
			TimerHandler timerHandler = new TimerHandler(speed, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler timerHandler) {
					try {
//						float value = (Float)ReflectionUtil.get(entity.getSprite(), property) + interval;
						float value = getValue() + interval;
						
						if ((higher &&  value >= target) || (!higher &&  value <= target)) {
							value = target;
							timerHandler.setAutoReset(false);
							scene.unregisterUpdateHandler(timerHandler);
						}
						
						ReflectionUtil.set(AnimateController.this.entity.getSprite(), property, value);
					} catch (Exception e) {
						Log.e("AnimateControoler", "error during interval", e);
					}
				}
			});
			timerHandler.setAutoReset(true);
			scene.registerUpdateHandler(timerHandler);
		} catch (Exception e) {
			Log.e("AnimateControoler", "could not start animatoin", e);
		}
	}



	private float getValue() {
		try {
			if (field != null) {
				return field.getFloat(entity.getSprite());
			} else if (getter != null) {
				return (Float)getter.invoke(entity.getSprite(), new Object[0]);
			}
		} catch (Exception e) {
			Log.e("AnimateController", "cannot get value", e);
		}
		
		return 0;
	}
//	
//	private void setValue(float value) {
//		try {
//			if (field != null) {
//				field.setFloat(entity.getSprite(), value);
//			} else if (setter != null){			
//				setter.invoke(entity.getSprite(), new Object[] { value });
//			}
//		} catch (Exception e) {
//			Log.e("AnimateController", "cannot set value", e);
//		}
//	}
}
