package ch.zhaw.game.activity;

import org.andengine.entity.scene.Scene;

import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;


public class GameActivity extends AbstractBaseActivity {
	public static final String KEY_MAP = "map";

	@Override
	protected Scene onCreateScene() {
		try {
			// get map from intent
			String map = getIntent().getStringExtra(KEY_MAP);
			
			// create game scene
			GameScene scene = GameSceneFactory.loadScene(resourceManager, map);
			
			// camera should case player
			camera.chase(scene.getPlayer());
			
			return scene;
		} catch (Exception e) {
			// something failed, report and exit
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
}
