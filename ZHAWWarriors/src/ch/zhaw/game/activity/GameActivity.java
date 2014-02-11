package ch.zhaw.game.activity;

import org.andengine.entity.scene.Scene;

import android.util.Log;
import ch.zhaw.game.control.DebugController;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityControllerInvoker;
import ch.zhaw.game.physics.CollisionDebugger;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;

public class GameActivity extends AbstractBaseActivity {
	private GameScene scene;

	@Override
	protected Scene onCreateScene() {
		try {
			// get map from intent
			String map = getIntent().getStringExtra(KEY_MAP);
			if (map == null) {
				map = "scene/menu.json";
			}
			
			// create game scene
			scene = GameSceneFactory.loadScene(camera, resourceManager, map);
			
			// camera should case player
			Entity player = scene.getEntityById("player");
			if (player != null) {
				camera.chase(player);
				if (DebugController.isDebugMode()) {
					scene.registerUpdateHandler(new CollisionDebugger(scene));
				}
			}
			
			return scene;
		} catch (Exception e) {
			// something failed, report and exit
			Log.e("menu", "could not load map", e);
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (scene == null) {
			return;
		}
		for (Entity entity : scene.getEntities()) {
			EntityControllerInvoker.invoke(entity.getEntityController(), "resume");
		}
	}
	
	
	
}