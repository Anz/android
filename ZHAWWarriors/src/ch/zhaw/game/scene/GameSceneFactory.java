package ch.zhaw.game.scene;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.math.Vector2;

import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.entity.GameCamera;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.util.FileUtil;
import ch.zhaw.game.util.JSONUtil;
import ch.zhaw.game.util.ReflectionUtil;

public class GameSceneFactory {
	private ResourceManager resourceManager;
	private Map<String, Class<?>> controllers = new HashMap<String, Class<?>>();
	
	public GameSceneFactory(ResourceManager resourceManager, String packageName) throws IOException {
		this.resourceManager = resourceManager;
		List<Class<?>> classes = ReflectionUtil.getClasses(resourceManager.getActivity(), packageName);
		
		for (Class<?> clazz : classes) {
			for (Annotation annotation : clazz.getDeclaredAnnotations()) {
				if (!(annotation instanceof Controller)) {
					continue;
				}
				
				Controller controller = (Controller)annotation;
				controllers.put(controller.name(), clazz);
			}
		}
	}

	public GameScene loadScene(GameCamera camera, String fileName) throws Exception {
		// open stream
		InputStream input = resourceManager.getAssetManager().open(fileName);

		// read json
		JSONObject map = new JSONObject(FileUtil.readFile(input));
		JSONObject classes = map.getJSONObject("classes");
		JSONArray tiles = map.getJSONArray("tiles");
		
		Vector2 gravity = new Vector2();
		boolean boundries = false;
		
		if (map.has("settings")) {
			JSONObject settings = map.getJSONObject("settings");
			if (settings.has("gravity")) {
				JSONUtil.copy((JSONObject)settings.get("gravity"), gravity);
			}
			if (settings.has("boundries")) {
				boundries = settings.getBoolean("boundries");
			}
		}
		
		
		
		// create game scene
		GameScene scene = new GameScene(camera, resourceManager, gravity);
		if (boundries) {
			scene.createMap(-512, tiles.length() * 1024-512, -50, 512);
		}

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
				createEntity(scene, clazz, entity);
			}
		}
		scene.onUpdate(0);
		return scene;
	}
	
	public void createEntity(GameScene scene, JSONObject clazz, JSONObject object) throws Exception {
		EntityController entityController = (EntityController)resolveEntityController(clazz, object);
			
		JSONUtil.copy(clazz, entityController);
		JSONUtil.copy(object, entityController);
		entityController.setScene(scene);
		EntityControllerInvoker.invoke(entityController, "create");
	}
	
	private Object resolveEntityController(JSONObject clazz, JSONObject entity) throws Exception {
		String handler = "";
		if (entity.has("handler")) {
			handler = entity.getString("handler");
		} else if (clazz.has("handler")) {
			handler = clazz.getString("handler");
		}
		
		Class<?> entityControllerClass = controllers.get(handler);
		if (entityControllerClass != null) {
			return entityControllerClass.newInstance();
		}
		
		return new EntityController();
	}
}
