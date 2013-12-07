package ch.zhaw.game.resource;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.res.AssetManager;

public class ResourceManager {	
	private Map<String, TiledTextureRegion> imgs = new HashMap<String, TiledTextureRegion>();
	private SimpleBaseGameActivity context;
	private AssetManager assetManager;
	private TextureManager textureManager;
	private VertexBufferObjectManager vboManager;
	
	public ResourceManager(SimpleBaseGameActivity context, AssetManager assetManager, VertexBufferObjectManager vboManager, TextureManager textureManager) {
		this.context = context;
		this.assetManager = assetManager;
		this.textureManager = textureManager;
		this.vboManager = vboManager;
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	}

	public TiledTextureRegion loadTexture(String name, int width, int height, int xFrames, int yFrames) {
		if (!imgs.containsKey(name)) {
			BitmapTextureAtlas texture = new BitmapTextureAtlas(textureManager, width, height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			TiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, name, 0, 0, xFrames, yFrames);
			texture.load();
			imgs.put(name, textureRegion);
		}
		
		return imgs.get(name);
	}
	
	public TiledTextureRegion getTexture(String name) {
		return imgs.get(name);
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SimpleBaseGameActivity getContext() {
		return context;
	}

	public TextureManager getTextureManager() {
		return textureManager;
	}

	public VertexBufferObjectManager getVboManager() {
		return vboManager;
	}
}
