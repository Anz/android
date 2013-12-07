package ch.zhaw.game.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.opengl.texture.region.TextureRegion;

import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerStub;
import ch.zhaw.game.entity.TouchListener;
import ch.zhaw.game.resource.ResourceManager;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;

public class PlayerController extends EntityControllerStub implements EntityController, TouchListener {
	private Entity entity;
	private TextureEntity textureEntity;
	
	public PlayerController(Entity entity, TextureEntity textureEntity) {
		this.entity = entity;
		this.textureEntity = textureEntity;
	}
	
	@Override
	public void onContact(Entity entity) {
		if (entity.getEntityType() == Category.ENEMY) {
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
	public void onTouch(Vector2 position) {
		entity.move(position);
	}

	@Override
	public void onTouch(Entity entity) {
		if (entity.getBody() == null) {
			return;
		}
		entity.move(entity.getBody().getPosition());
	}
}
