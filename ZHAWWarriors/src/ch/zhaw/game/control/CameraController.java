package ch.zhaw.game.control;

import org.andengine.engine.camera.Camera;

import android.util.DisplayMetrics;
import android.view.Display;

import ch.zhaw.game.Util;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;


public class CameraController extends Camera {
	private Entity entity;
	private float leftLimit;
	private float rightLimit;
	private float topLimit;
	private float bottomLimit;
	
	public CameraController(Display display) {
		super(0, 0, 0, 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		set(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
	}
	
	public CameraController(float width, float height) {
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
		leftLimit = getWidth()/2;
		bottomLimit = -getHeight() / 2;
		rightLimit = scene.getWidth() - getWidth()/2;
		topLimit = -(scene.getHeight() - getHeight())/2;
	}
	
	
	
}
