package ch.zhaw.game.resource;

import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import ch.zhaw.game.scene.GameScene;

public class TextureEntity extends Entity {
	private GameScene scene;
	private RenderTexture renderTexture;
	private boolean update = false;
	
	public TextureEntity(GameScene scene, int width, int height, List<TextureRegion> textures) {
		this.scene = scene;
		renderTexture = new RenderTexture(scene.getResourceManager().getTextureManager(), 512, 512);
		
		update(textures);
		scene.attachChild(this);
	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		if (!update) {
			return;
		}
		
		if (!renderTexture.isInitialized()) {
			renderTexture.init(pGLState);
	    }
		renderTexture.begin(pGLState, false, true, Color.TRANSPARENT);
	    super.onManagedDraw(pGLState, new Camera(-1f, -1f, 1f, 1f));
	    renderTexture.end(pGLState);
	    
	    update = false;
	}

	public TiledTextureRegion getTiledTextureRegion(int frameX, int frameY) {
		return TextureRegionFactory.extractTiledFromTexture(renderTexture, frameX, frameY);
	}
	
	public void update(List<TextureRegion> textures) {
		detachChildren();
		for (TextureRegion texture : textures) {
			attachChild(new Sprite(0, 0, texture, scene.getResourceManager().getVboManager()));
		}
		update = true;
	}
}
