package ch.zhaw.game.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import dalvik.system.DexFile;

public class ReflectionUtil {
	public static Field getField(Object object, String property) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(property);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException  e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				clazz = null;
			}
		}
		return null;
	}
	
	public static Method getMethod(Object object, String property, Class<?>[] parameters) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Method method = clazz.getDeclaredMethod(property, parameters);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException  e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				clazz = null;
			}
		}
		return null;
	}
	
	public final static List<Class<?>> getClasses(Activity context, String packageName) throws IOException {
		final List<Class<?>> classes = new LinkedList<Class<?>>();
		
		DexFile df = new DexFile(context.getPackageCodePath());
        for (Enumeration<String> iter = df.entries(); iter.hasMoreElements();) {
            String className = iter.nextElement();
            if (className.startsWith(packageName)) {
            	try {
					Class<?> clazz = Class.forName(className);
					classes.add(clazz);
				} catch (ClassNotFoundException e) {
					// could not load
				}
            }
        }
        return classes;
	}
	
	public static Object get(Object object, String property) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Field field = getField(object, property);
		if (field != null) {
			return field.get(object);
		}
		
		String methodName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
		Method method = getMethod(object, methodName, new Class<?>[0]);
		if (method != null) {
			return method.invoke(object, new Object[0]);
		}
		
		return null;
	}
	
	public static void set(Object object, String property, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Field field = getField(object, property);
		if (field != null) {
			Class<?> fieldClass = field.getType();
			if (byte.class.equals(fieldClass)) {
				field.setInt(object, ((Number)value).byteValue());
			} else if (char.class.equals(fieldClass)) {
				field.setChar(object, ((Character)value).charValue());
			} else if (short.class.equals(fieldClass)) {
				field.setShort(object, ((Number)value).shortValue());
			} else if (int.class.equals(fieldClass)) {
				field.setInt(object, ((Number)value).intValue());
			} else if (long.class.equals(fieldClass)) {
				field.setLong(object, ((Number)value).longValue());
			} else if (float.class.equals(fieldClass)) {
				field.setFloat(object, ((Number)value).floatValue());
			} else if (double.class.equals(fieldClass)) {
				field.setDouble(object, ((Number)value).doubleValue());
			} else if (boolean.class.equals(fieldClass)) {
				field.setBoolean(object, ((Boolean)value).booleanValue());
			} else {
				field.set(object, value);
			}
			return;
		}
		
		String methodName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
//		Method method = getMethod(object, methodName, new Class<?>[] { value.getClass() });
		Method method = getMethod(object, methodName, new Class<?>[] { float.class });
		if (method != null) {
			method.invoke(object, new Object[] { value });
		}
	}
}
