package ch.zhaw.game.activity;

import org.andengine.entity.scene.Scene;

import android.util.Log;

import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;


public class MenuActivity extends AbstractBaseActivity {
	@Override
	protected Scene onCreateScene() {
		try {
			// create game scene
			GameScene scene = GameSceneFactory.loadScene(resourceManager, "scene/menu.json");
			
			return scene;
		} catch (Exception e) {
			// something failed, report and exit
			Log.e("menu", "could not load map", e);
			return null;
		}
	}
}
