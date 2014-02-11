package ch.zhaw.game.entity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

import android.util.DisplayMetrics;
import android.view.Display;
import ch.zhaw.game.scene.GameScene;
import ch.zhaw.game.util.MathUtil;


public class GameCamera extends Camera {
	private Entity entity;
	private float leftLimit;
	private float rightLimit;
	private float topLimit;
	private float bottomLimit;
	private HUD hud = new HUD();
	private Rectangle healthBar;
	
	public GameCamera(Display display) {
		super(0, 0, 0, 0);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		int halfWidth = displayMetrics.widthPixels / 2;
		int halfHeight = displayMetrics.heightPixels / 2;
		set(-halfWidth, -halfHeight, halfWidth, halfHeight);
		setHUD(hud);
	}
	
	public GameCamera(int width, int height) {
		super(0, 0, 0, 0);
		set(-width/2, -height/2, width/2, height/2);
		setHUD(hud);
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		
		if (entity == null) {
			return;
		}
		
		healthBar.setWidth(MathUtil.limit(entity.getEntityController().getLife()*150/100, 0, 100));
		
		// set camera
		float x = MathUtil.limit(entity.getSprite().getX(), leftLimit, rightLimit);
		float y = MathUtil.limit(entity.getSprite().getY(), bottomLimit, topLimit);
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
		
		Rectangle border = new Rectangle(45, 45, 160, 40, scene.getResourceManager().getVboManager());
		border.setColor(Color.BLACK);
		hud.attachChild(border);
		
		healthBar = new Rectangle(50, 50, 150, 30, scene.getResourceManager().getVboManager());
		healthBar.setColor(Color.RED);
		hud.attachChild(healthBar);
	}
}
