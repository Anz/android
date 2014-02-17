package ch.zhaw.game.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Event;
import ch.zhaw.game.entity.Sprite;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;

@Controller(name="player")
public class PlayerController extends EntityController {
	private EntityController targetEntity;
	private TextureEntity textureEntity;
	private List<ITextureRegion> textureList = new ArrayList<ITextureRegion>();
	
	@Event(filter="create")
	public Entity onCreate() {
		super.create();
		
		// create player
		textureList.add(TextureRegionFactory.extractFromTexture(scene.getResourceManager().getTexture(img)));
		textureEntity = scene.createTextureEntity(512, 512, textureList);
		scene.detachChild(entity.getSprite());
		entity.setSprite(new Sprite(scene, entity, entity.getSprite().getX(), entity.getSprite().getY(), TextureRegionFactory.extractTiledFromTexture(textureEntity.getTexture(), 4, 3)));
		scene.attachChild(entity.getSprite());
		if (entity.getSprite() instanceof RectangularShape) {
			RectangularShape rect = (RectangularShape)entity.getSprite();
			scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(rect, entity.getBody(), true, true));
		}

		entity.setEntityController(this);
		
		return entity;
	}


	@Event(filter="contact")
	private void onContact(EntityController entityController) {
		if (entityController == null) {
			return;
		}
		
		Entity entity = entityController.getEntity();
		
		if (this.entity.isEnemy(entity)) {
			this.entity.move(null);
			if (this.entity.getSprite() instanceof AnimatedSprite) {
				AnimatedSprite animatedSprite = (AnimatedSprite)this.entity.getSprite();
				animatedSprite.animate(Entity.FRAME_SPEED, 4, 7, true);
			}
			targetEntity = entityController;
		}
		
		if (entityController.isPickable()) {
			entity.destroy();

			
			final GameScene scene =  this.entity.getScene();
			final ResourceManager resourceManager = scene.getResourceManager();
			
			textureList.add(TextureRegionFactory.extractFromTexture(resourceManager.getTexture(entityController.getImg())));
			textureEntity.update(textureList);
		}
	}
	
	@Event(filter="resume")
	private void onResume() {
		ResourceManager resourceManager = scene.getResourceManager();
		textureList.clear();
		textureList.add(TextureRegionFactory.extractFromTexture(resourceManager.getTexture("knight.svg")));
		textureList.add(TextureRegionFactory.extractFromTexture(resourceManager.getTexture("witch_hat.svg")));
		textureEntity.update(textureList);
	}
	
	@Event(filter="screen")
	public boolean onScreen(Scene scene, EntityController entityController, Vector2 position) {
		position.mul(1f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		targetEntity = null;
		entity.move(position);
		return false;
	}
	
	@Event(filter="update")
	public void onUpdate() {
		if (targetEntity != null) {
			if (!targetEntity.getEntity().getSprite().hasParent()) {
				targetEntity = null;
				return;
			}
			
			targetEntity.onDamage(100);
		}
		
	}
}
