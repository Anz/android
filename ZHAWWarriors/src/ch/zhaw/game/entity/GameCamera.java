package ch.zhaw.game.entity;

import org.andengine.engine.camera.Camera;

import android.util.DisplayMetrics;
import android.view.Display;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.util.Util;


public class GameCamera extends Camera {
	private Entity entity;
	private float leftLimit;
	private float rightLimit;
	private float topLimit;
	private float bottomLimit;
	
	public GameCamera(Display display) {
		super(0, 0, 0, 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		int halfWidth = displayMetrics.widthPixels / 2;
		int halfHeight = displayMetrics.heightPixels / 2;
		set(-halfWidth, -halfHeight, halfWidth, halfHeight);
	}
	
	public GameCamera(float width, float height) {
		super(0, 0, width, height);
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		
		if (entity == null) {
			return;
		}
		
		// set camera
		float x = Util.limit(entity.getSprite().getX(), leftLimit, rightLimit);
		float y = Util.limit(entity.getSprite().getY(), bottomLimit, topLimit);
		setCenter(x, y);		
	}
	
	public void chase(Entity entity) {
		this.entity = entity;
		
		// calculate boundries
		GameScene scene = entity.getScene();
		leftLimit = scene.getLeft() + getWidth()/2;
		bottomLimit = scene.getBottom() - getHeight() / 2;
		rightLimit = scene.getRight() - getWidth()/2;
		topLimit = scene.getTop() - getHeight()/2;
	}
	
	
	
}
