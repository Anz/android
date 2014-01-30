package ch.zhaw.game.activity;

import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import ch.zhaw.game.control.CameraController;
import ch.zhaw.game.resource.ResourceManager;


public abstract class AbstractBaseActivity extends SimpleBaseGameActivity {
	// camera
	protected CameraController camera;
	
	// resource manager
	protected ResourceManager resourceManager;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// initialize camera resolution based on display resolution
		camera = new CameraController(getWindowManager().getDefaultDisplay());
		
		// set engine options
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		resourceManager = new ResourceManager(this);
	}
}
