package ch.zhaw.game;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import ch.zhaw.game.control.CameraController;
import ch.zhaw.game.control.PlayerController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.scene.GameSceneFactory;


public class GameActivity extends SimpleBaseGameActivity {
	private Camera camera;
	private static final int CAMERA_WIDTH = 1280;
	private static final int CAMERA_HEIGHT = 780;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
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
			
			resourceManager.loadTexture("knight.png", 512, 512, 4, 3);
			
			// create player
			List<TextureRegion> textureList = new ArrayList<TextureRegion>();
			textureList.add(new TextureRegion(resourceManager.getTexture("knight.png").getTexture(), 0f, 0f, 512f, 512f));
			TextureEntity texture = scene.createTextureEntity(512, 512, textureList);
			
			Entity entity = scene.createEntity(Category.PLAYER, 100, 0, texture.getTiledTextureRegion(4, 3), true, false);
			entity.setSpeed(10);
			PlayerController playerController = new PlayerController(entity, texture);
			entity.setEntityListener(playerController);
			new CameraController(entity, camera);
			
			
			scene.registerTouchListener(playerController);
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
