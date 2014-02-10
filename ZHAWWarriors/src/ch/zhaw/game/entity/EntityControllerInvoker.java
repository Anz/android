package ch.zhaw.game.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.game.util.StringUtils;

import android.util.Log;


public class EntityControllerInvoker {
	public static void invoke(Object controller, String eventName) {
		invoke(controller, eventName, null);
	}
	
	public static void invoke(Object controller, String eventName, EntityController entityController) {
		Class<?> clazz = controller.getClass();
		
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
				
				try {
					List<Object> args = new ArrayList<Object>();
					for (Class<?> parameter : method.getParameterTypes()) {
						if (String.class.equals(parameter)) {
							args.add(eventName);
						} else if (EntityController.class.equals(parameter)) {
							args.add(entityController);
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
