package ch.zhaw.game.util;

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
				if (value instanceof JSONObject) {
					try {
						Object child = ReflectionUtil.get(object, key);
						copy((JSONObject) value, child);
					} catch (Exception e) {
						// ignore
					}
				} else {
					try {
						ReflectionUtil.set(object, key, value);
					} catch (Exception e) {
						// ignore
					}
				}

			} catch (Exception e) {
				Log.e("JSONUtil", "could not map json object to object", e);
			}
		}
	}
}
