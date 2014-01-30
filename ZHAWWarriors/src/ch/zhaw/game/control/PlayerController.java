package ch.zhaw.game.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;

public class PlayerController extends EntityController implements IOnSceneTouchListener {
	private Entity targetEntity;
	private TextureEntity textureEntity;
	
	public PlayerController(Entity entity, TextureEntity textureEntity) {
		super(entity);
		this.textureEntity = textureEntity;
	}
	
	@Override
	public void onContact(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (entity.getEntityType() == Category.ENEMY && targetEntity == entity) {
			this.entity.move(null);
			this.entity.getSprite().animate(Entity.FRAME_SPEED, 4, 7, true);
		}
		
		if (entity.getEntityType() == Category.ITEM) {
			entity.destroy();

			
			final GameScene scene =  this.entity.getScene();
			final ResourceManager resourceManager = scene.getResourceManager();
			
			List<TextureRegion> textureList = new ArrayList<TextureRegion>();
			textureList.add(new TextureRegion(resourceManager.getTexture("knight.png").getTexture(), 0f, 0f, 512f, 512f));
			textureList.add(new TextureRegion(resourceManager.getTexture("witch_hat.png").getTexture(), 0f, 0f, 512f, 512f));
			textureEntity.update(textureList);
		}
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {
		if (touchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			Vector2 target = new Vector2(touchEvent.getX(), touchEvent.getY());
			target.mul(1f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			targetEntity = null;
			entity.move(target);
		}
		return false;
	}

//	@Override
//	public void onTouch(Entity entity) {
//		if (entity.getEntityType() == Category.STATIC) {
//			return;
//		}
//		targetEntity = entity;
//		entity.move(entity.getBody().getPosition());
//	}
	
}
