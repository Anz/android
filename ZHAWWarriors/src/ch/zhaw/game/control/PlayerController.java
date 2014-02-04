package ch.zhaw.game.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Sprite;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;

public class PlayerController extends EntityController implements IOnSceneTouchListener {
	private Entity targetEntity;
	private TextureEntity textureEntity;
	
	@Override
	public Entity onCreate() {
		super.onCreate();
		
		// create player
		List<ITextureRegion> textureList = new ArrayList<ITextureRegion>();
		textureList.add(TextureRegionFactory.extractFromTexture(scene.getResourceManager().getTexture(img)));
		textureEntity = scene.createTextureEntity(512, 512, textureList);
		scene.detachChild(entity.getSprite());
		entity.setSprite(new Sprite(scene, entity, entity.getSprite().getX(), entity.getSprite().getY(), TextureRegionFactory.extractTiledFromTexture(textureEntity.getTexture(), 4, 3)));
		scene.attachChild(entity.getSprite());
		scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(entity.getSprite(), entity.getBody(), true, true));

		entity.setEntityController(this);
		scene.setOnSceneTouchListener(this);
		
		return entity;
	}


	@Override
	public void onContact(EntityController entityController) {
		Entity entity = entityController.getEntity();
		
		if (this.entity.isEnemy(entity) && targetEntity == entity) {
			this.entity.move(null);
			this.entity.getSprite().animate(Entity.FRAME_SPEED, 4, 7, true);
			entityController.onDamage(200);
		}
		
		if (entityController.isPickable()) {
			entity.destroy();

			
			final GameScene scene =  this.entity.getScene();
			final ResourceManager resourceManager = scene.getResourceManager();
			
			List<ITextureRegion> textureList = new ArrayList<ITextureRegion>();
			textureList.add(TextureRegionFactory.extractFromTexture(resourceManager.getTexture("knight.svg")));
			textureList.add(TextureRegionFactory.extractFromTexture(resourceManager.getTexture("witch_hat.svg")));
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
