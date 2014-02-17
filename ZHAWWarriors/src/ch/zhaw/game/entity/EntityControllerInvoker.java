package ch.zhaw.game.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import ch.zhaw.game.util.StringUtils;

import com.badlogic.gdx.math.Vector2;


public class EntityControllerInvoker {
	public static void invoke(Object controller, String eventName) {
		invoke(controller, eventName, null);
	}
	
	public static void invoke(Object controller, String eventName, EntityController entityController) {
		invoke(controller, eventName, entityController, null);
	}
	
	public static void invoke(Object controller, String eventName, EntityController entityController, Vector2 position) {
		Class<?> clazz = controller.getClass();
		
		long current = System.currentTimeMillis();
		while (clazz != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				method.setAccessible(true);
				Event event = method.getAnnotation(Event.class);
				if (event == null) {
					continue;
				}
				
				if (!(StringUtils.isEmtpy(event.filter()) || eventName.equals(event.filter()))) {
					continue;
				}
				
				if (current - event.called() < event.delay()) {
					continue;
				}
				try {
					Field called = event.getClass().getField("called");
					called.setAccessible(true);
					called.set(event, current);
				} catch (Exception e) {
					// ignore
				}
				
				try {
					List<Object> args = new ArrayList<Object>();
					for (Class<?> parameter : method.getParameterTypes()) {
						if (String.class.equals(parameter)) {
							args.add(eventName);
//						} else if (EntityController.class.equals(parameter)) {
//						} else if (parameter.isInstance(EntityController.class)) {
						} else if (EntityController.class.isAssignableFrom(parameter)) {
							args.add(entityController);
						} else if (Vector2.class.equals(parameter)) {
							args.add(position);
						} else {
							args.add(null);
						}
					}
					method.invoke(controller, args.toArray());
				} catch (Exception e) {
					Log.e(controller.toString(), "could not invoke event method", e);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
}
