package ch.zhaw.game.scene;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.zhaw.game.control.EnemyController;
import ch.zhaw.game.control.PlayerController;
import ch.zhaw.game.control.TriggerController;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.resource.ResourceManager;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameSceneFactory {
	private static int seq = 0;
	private static Map<String, Integer> partyGroupMap = new HashMap<String, Integer>();
	
	private static final Map<String, Class<? extends EntityController>> ENTITY_CONROLLERS = new HashMap<String, Class<? extends EntityController>>() {
		private static final long serialVersionUID = 1L;
		{
			put("player", PlayerController.class);
			put("enemy", EnemyController.class);
			put("trigger", TriggerController.class);
		};
	};
	
	private static final Map<String, Object> PROPERTIES = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("id", "");
			put("handler", "");
			put("party", "");
			put("category", "static");
			put("x", 0);
			put("y", 0);
			put("xFrames", 1);
			put("yFrames", 1);
			put("img", "knight.png");
			put("speed", 0);
			put("pickable", false);
			put("dynamic", false);
			
			// trigger
			put("event", "");
			put("load", "");
		};
	};

	public static GameScene loadScene(ResourceManager resourceManager,
			String fileName) throws Exception {
		// open stream
		InputStream file = resourceManager.getAssetManager().open(fileName);

		// read file
		int size = file.available();
		byte[] buffer = new byte[size];
		file.read(buffer);
		file.close();

		// read json
		JSONObject map = new JSONObject(new String(buffer, "UTF-8"));
		JSONObject classes = map.getJSONObject("classes");
		JSONArray tiles = map.getJSONArray("tiles");
		
		// create game scene
		GameScene scene = new GameScene(resourceManager, -512, tiles.length() * 1024-512, -50, 512);

		// for each tile
		for (int i = 0; i < tiles.length(); i++) {
			int tileX = i * 1024;
			int tileY = 0;

			JSONArray entities = tiles.getJSONArray(i);
			for (int j = 0; j < entities.length(); j++) {
				JSONObject entity = entities.getJSONObject(j);
				int x = tileX;
				int y = tileY;
				
				// create entity
				if (entity.has("class")) {
					String className = entity.getString("class");
					JSONObject clazz = classes.has(className) ? classes.getJSONObject(className) : new JSONObject();
					Map<String, Object> properties = readProperties(clazz, entity);
					
					
					if (properties.containsKey("x")) {
						x += ((Number)properties.get("x")).floatValue();
					}
					if (properties.containsKey("y")) {
						y += ((Number)properties.get("y")).floatValue();
					}

					
					String image = (String)properties.get("img");
					int xFrames = (Integer)properties.get("xFrames");
				    int yFrames = (Integer)properties.get("yFrames");
				    boolean dynamic = (Boolean)properties.get("dynamic");
					
					TiledTextureRegion texture = TextureRegionFactory.extractTiledFromTexture(resourceManager.getTexture(image), xFrames, yFrames);
					
					Entity result = scene.createEntity((String)properties.get("party"), x, y, texture, dynamic);
					result.setId((String)properties.get("id"));
					result.setParty((String)properties.get("party"));
					result.setPickable((Boolean)properties.get("pickable"));
					
					Class<? extends EntityController> entityControllerClass = ENTITY_CONROLLERS.get((String)properties.get("handler"));
					if (entityControllerClass != null) {
						EntityController entityController = 
								entityControllerClass.getDeclaredConstructor(Entity.class, Map.class).newInstance(result, properties);
						result.setEntityController(entityController);
					}
					
					result.setSpeed(((Number)properties.get("speed")).floatValue());
				}
			}
		}

		return scene;
	}
	
	public static Map<String, Object> readProperties(JSONObject clazz, JSONObject entity) {
		Map<String, Object> properties = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : PROPERTIES.entrySet()) {
			try {
				if (entity.has(entry.getKey())) {
					properties.put(entry.getKey(), entity.get(entry.getKey()));
				} else if (clazz.has(entry.getKey())) {
					properties.put(entry.getKey(), clazz.get(entry.getKey()));
				} else {
					properties.put(entry.getKey(), entry.getValue());
				}
			} catch (JSONException e) {
				// do nothing
			}
		}
		return properties;
	}

	public static FixtureDef createCircularFixture(String party) {
		if (!partyGroupMap.containsKey(party)) {
			partyGroupMap.put(party, seq++);
		}
		
		Integer index = partyGroupMap.get(party);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.groupIndex = (short) -index;
		fixtureDef.shape = shape;
		return fixtureDef;
	}
}
