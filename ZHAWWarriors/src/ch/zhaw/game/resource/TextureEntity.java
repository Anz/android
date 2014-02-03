package ch.zhaw.game.resource;

import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import ch.zhaw.game.scene.GameScene;

public class TextureEntity extends Entity {
	private GameScene scene;
	private RenderTexture renderTexture;
	private boolean update = false;
	
	public TextureEntity(GameScene scene, int width, int height, List<ITextureRegion> textures) {
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
	
	public ITexture getTexture() {
		return renderTexture;
	}
	
	public void update(List<ITextureRegion> textures) {
		detachChildren();
		for (ITextureRegion texture : textures) {
			attachChild(new Sprite(0, 0, texture, scene.getResourceManager().getVboManager()));
		}
		update = true;
	}
}
