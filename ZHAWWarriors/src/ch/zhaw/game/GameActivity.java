package ch.zhaw.game;

import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import ch.zhaw.game.control.CameraController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.physics.CollisionDebugger;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;


public class GameActivity extends SimpleBaseGameActivity {
	private CameraController camera;
	private static final int CAMERA_WIDTH = 1280;
	private static final int CAMERA_HEIGHT = 780;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new CameraController(CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	@Override
	protected Scene onCreateScene() {
		try {
			ResourceManager resourceManager = new ResourceManager(this, getAssets(), getVertexBufferObjectManager(), getTextureManager());
			GameScene scene;
			
			scene = GameSceneFactory.loadScene(resourceManager, "scene/land.json");
			
			for (Entity entity : scene.getEntities()) {
				if (entity.getEntityType() == Category.PLAYER) {
					camera.chase(entity);
					break;
				}
			}
			
			scene.registerUpdateHandler(new CollisionDebugger(scene));
			return scene;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	@Override
	protected void onCreateResources() {}
}
