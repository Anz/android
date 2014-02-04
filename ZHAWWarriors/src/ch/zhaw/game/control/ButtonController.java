package ch.zhaw.game.control;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.Sprite;

public class ButtonController extends TriggerController {
	private String img2;
	private Sprite second;
	
	@Override
	public Entity onCreate() {
		super.onCreate();
		
		ITexture texture = scene.getResourceManager().getTexture(img2);
		second = new Sprite(scene, entity, entity.getSprite().getX(), entity.getSprite().getY(), TextureRegionFactory.extractTiledFromTexture(texture, 1, 1));
		scene.attachChild(second);
		scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnector(second, entity.getBody(), true, true));
		second.setVisible(false);
		
		return entity;
	}
	

	@Override
	protected void onEvent(String event) {
		super.onEvent(event);
		
		// check event
		if (!event.equals(this.event)) {
			return;
		}

		entity.getSprite().setVisible(!entity.getSprite().isVisible());
		second.setVisible(!second.isVisible());
	}
}
