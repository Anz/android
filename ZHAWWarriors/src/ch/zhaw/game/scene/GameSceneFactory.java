package ch.zhaw.game.scene;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.zhaw.game.control.ButtonController;
import ch.zhaw.game.control.DebugController;
import ch.zhaw.game.control.EnemyController;
import ch.zhaw.game.control.PlayerController;
import ch.zhaw.game.control.TriggerController;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.util.FileUtil;
import ch.zhaw.game.util.JSONUtil;

public class GameSceneFactory {
	private static final Map<String, Class<? extends EntityController>> ENTITY_CONROLLERS = new HashMap<String, Class<? extends EntityController>>() {
		private static final long serialVersionUID = 1L;
		{
			put("player", PlayerController.class);
			put("enemy", EnemyController.class);
			put("trigger", TriggerController.class);
			put("button", ButtonController.class);
			put("debug", DebugController.class);
		};
	};

	public static GameScene loadScene(GameCamera camera, ResourceManager resourceManager, String fileName) throws Exception {
		// open stream
		InputStream input = resourceManager.getAssetManager().open(fileName);

		// read json
		JSONObject map = new JSONObject(FileUtil.readFile(input));
		JSONObject classes = map.getJSONObject("classes");
		JSONArray tiles = map.getJSONArray("tiles");
		
		// create game scene
		GameScene scene = new GameScene(camera, resourceManager, -512, tiles.length() * 1024-512, -50, 512);

		// for each tile
		for (int i = 0; i < tiles.length(); i++) {
			int tileX = i * 1024;

			JSONArray entities = tiles.getJSONArray(i);
			for (int j = 0; j < entities.length(); j++) {
				JSONObject entity = entities.getJSONObject(j);
				
				// create entity
				String className = entity.has("class") ? entity.getString("class") : "";
				JSONObject clazz = classes.has(className) ? classes.getJSONObject(className) : new JSONObject();
				
				clazz.put("x", tileX + (clazz.has("x") ? clazz.getDouble("x") : 0));
				entity.put("x", tileX + (entity.has("x") ? entity.getDouble("x") : 0));
				
				EntityController entityController = resolveEntityController(clazz, entity);
					
				JSONUtil.copy(clazz, entityController);
				JSONUtil.copy(entity, entityController);
				entityController.setScene(scene);
				entityController.onCreate();
			}
		}
		return scene;
	}
	
	private static EntityController resolveEntityController(JSONObject clazz, JSONObject entity) throws Exception {
		String handler;
		if (entity.has("handler")) {
			handler = entity.getString("handler");
		} else if (clazz.has("handler")) {
			handler = clazz.getString("handler");
		} else {
			return new EntityController();
		}
		
		Class<? extends EntityController> entityControllerClass = ENTITY_CONROLLERS.get(handler);
		if (entityControllerClass != null) {
			return entityControllerClass.newInstance();
		}
		
		return new EntityController();
	}
}
