package ch.zhaw.game.scene;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.zhaw.game.control.EnemyController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.resource.ResourceManager;

public class GameSceneFactory {
	public static GameScene loadScene(ResourceManager resourceManager, String fileName) throws Exception {
		GameScene scene = new GameScene(resourceManager);
		
		try {
			// open stream
			InputStream file = resourceManager.getAssetManager().open(fileName);
			
			// read file
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
			
            // read json
			JSONObject map = new JSONObject(new String(buffer, "UTF-8"));
			
			// load settings
			{
				JSONObject settings = map.getJSONObject("settings");
				float width = (float) settings.getDouble("width");
				float height = (float) settings.getDouble("height");
				scene.createMap(width, height);
			}
			
			// load classes
			HashMap<String, JSONObject> classMap = new HashMap<String, JSONObject>();
			JSONArray classes = map.getJSONArray("classes");
			for (int i = 0; i < classes.length(); i++) {
				JSONObject clazz = classes.getJSONObject(i);
				String id = clazz.getString("id");
				String type = clazz.getString("type");
				String texture = clazz.getString("texture");
				
				if ("dynamic".equalsIgnoreCase(type)) {
					resourceManager.loadTexture(texture, 512, 512, 4, 3);
				} else if ("static".equalsIgnoreCase(type)) {
					resourceManager.loadTexture(texture, 1024, 1024, 1, 1);
				}
				classMap.put(id, clazz);
			}
			
			// load entity
			JSONArray entities = map.getJSONArray("entities");
			for (int i = 0; i < entities.length(); i++) {
				JSONObject entity = entities.getJSONObject(i);
				String className = entity.getString("class");

				float x = (float) entity.getDouble("x");
				float y = (float) entity.getDouble("y");
				JSONObject clazz = classMap.get(className);
				String texture = clazz.getString("texture");
				String type = clazz.getString("type");
				
				if ("dynamic".equalsIgnoreCase(type)) {
					Entity enemy = scene.createEntity(Category.ENEMY, x, y, texture, true, true);
					enemy.setSpeed(5);
					enemy.setEntityListener(new EnemyController(enemy));
				} else if ("static".equalsIgnoreCase(type)) {
					scene.createEntity(Category.STATIC, x, y, texture, false, false);
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return scene;
	}
}
