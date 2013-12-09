package ch.zhaw.game.control;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;

import ch.zhaw.game.Util;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.scene.GameScene;


public class CameraController implements IUpdateHandler {
	private Entity entity;
	private Camera camera;
	private float leftLimit;
	private float rightLimit;
	private float topLimit;
	private float bottomLimit;
	
	public CameraController(Entity entity, Camera camera) {
		super();
		this.entity = entity;
		this.camera = camera;
		
		// calculate boundries
		GameScene scene = entity.getScene();
		leftLimit = camera.getWidth()/2;
		bottomLimit = -camera.getHeight() / 2;
		rightLimit = scene.getWidth() - camera.getWidth()/2;
		topLimit = -(scene.getHeight() - camera.getHeight())/2;
		
		// register
		this.entity.getScene().registerUpdateHandler(this);
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		float x = Util.limit(entity.getSprite().getX(), leftLimit, rightLimit);
		float y = Util.limit(entity.getSprite().getY(), bottomLimit, topLimit);
		
		// set camera
		camera.setCenter(x, y);		
	}
	
	@Override
	public void reset() {
		// unregister
		this.entity.getScene().unregisterUpdateHandler(this);
	}
	
	
}
