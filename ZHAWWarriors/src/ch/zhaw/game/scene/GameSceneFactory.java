package ch.zhaw.game.scene;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.zhaw.game.entity.classes.Actor;
import ch.zhaw.game.entity.classes.EntityClass;
import ch.zhaw.game.entity.classes.Item;
import ch.zhaw.game.entity.classes.Tile;
import ch.zhaw.game.resource.ResourceManager;

public class GameSceneFactory {
	private static final Map<String, EntityClass> entityClasses = new HashMap<String, EntityClass>() {
		private static final long serialVersionUID = 1L;
		{
			// tiles
			put("land", new Tile("background.png"));

			// items
			put("witch_hat", new Item("witch_hat.png", "witch_hat.png"));

			// destructables
			put("box", new Item("box.png", "box.png"));
			
			// actors
			put("witch", new Actor("witch2.png"));
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
		JSONArray tiles = new JSONArray(new String(buffer, "UTF-8"));

		// check empty file
		if (tiles.length() == 0) {
			throw new IllegalArgumentException("map empty");
		}

		// create game scene
		GameScene scene = new GameScene(resourceManager, tiles.length() * 1024, 512);

		// for each tile
		for (int i = 0; i < tiles.length(); i++) {
			int tileX = i * 1024 + 512;
			int tileY = 0;

			JSONArray entities = tiles.getJSONArray(i);
			for (int j = 0; j < entities.length(); j++) {
				JSONObject entity = entities.getJSONObject(j);
				int x = tileX;
				int y = tileY;

				if (entity.has("x")) {
					x += entity.getInt("x");
				}
				if (entity.has("y")) {
					y += entity.getInt("y");
				}

				// create entity
				if (entity.has("class")) {
					EntityClass entityClass = entityClasses.get(entity
							.getString("class"));
					entityClass.createInstance(scene, x, y);
				}
			}
		}

		return scene;
	}
}
