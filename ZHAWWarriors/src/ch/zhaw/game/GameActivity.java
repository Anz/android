package ch.zhaw.game;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import ch.zhaw.game.control.PlayerController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;


public class GameActivity extends SimpleBaseGameActivity {
	private Camera camera;
	private static final int CAMERA_WIDTH = 1280;
	private static final int CAMERA_HEIGHT = 780;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
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
			
			// create player
			resourceManager.loadTexture("knight.png", 512, 512, 4, 4);
			Entity entity = scene.createEntity(Category.PLAYER, 10, 0, "knight.png", true, false);
			entity.setSpeed(10);
			camera.setChaseEntity(entity.getSprite());
			camera.setCenter(entity.getSprite().getX(), entity.getSprite().getY());
			
			
			scene.registerTouchListener(new PlayerController(entity));
			return scene;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onCreateResources() {}
}
