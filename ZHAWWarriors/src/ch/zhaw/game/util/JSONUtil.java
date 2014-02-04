package ch.zhaw.game.util;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.json.JSONObject;

import android.util.Log;

public class JSONUtil {
	@SuppressWarnings("unchecked")
	public static void copy(JSONObject jsonObject, Object object) {
		for (Iterator<String> keys = jsonObject.keys(); keys.hasNext();) {
			try {
				String key = keys.next(); 
				Object value = jsonObject.get(key);
				
				Class<?> clazz = object.getClass();
				while (clazz != null) {
					try {
						Field field = clazz.getDeclaredField(key);
						field.setAccessible(true);
						
						// set value
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
						
						clazz = null;
					} catch (NoSuchFieldException  e) {
						clazz = clazz.getSuperclass();
					} catch (Exception e) {
						Log.e("JSONUtil", "colud not map field: " + key + " value: " + value, e);
						clazz = null;
					}
				}
				
			} catch (Exception e) {
				Log.e("JSONUtil", "could not map json object to object", e);
			}
		}
	}
}