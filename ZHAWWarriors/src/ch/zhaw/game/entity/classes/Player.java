package ch.zhaw.game.entity.classes;

import java.util.ArrayList;
import java.util.List;

import org.andengine.opengl.texture.region.TextureRegion;

import ch.zhaw.game.control.PlayerController;
import ch.zhaw.game.entity.Category;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.resource.TextureEntity;
import ch.zhaw.game.scene.GameScene;

public class Player implements EntityClass {
	private String img;
	
	public Player(String img) {
		this.img = img;
	}

	@Override
	public Entity createInstance(GameScene scene, float x, float y) {
		
		// create player
		List<TextureRegion> textureList = new ArrayList<TextureRegion>();
		scene.getResourceManager().loadTexture("knight.png", 512, 512, 4, 3);
		textureList.add(new TextureRegion(scene.getResourceManager().getTexture(img).getTexture(), 0f, 0f, 512f, 512f));
		TextureEntity texture = scene.createTextureEntity(512, 512, textureList);
		
		final Entity entity = scene.createEntity(Category.PLAYER, x, y, texture.getTiledTextureRegion(4, 3), true);
		entity.setSpeed(10);
		PlayerController playerController = new PlayerController(entity, texture);
		entity.setEntityController(playerController);
		
		scene.setOnSceneTouchListener(playerController);
		return entity;
	}
	
	
}
