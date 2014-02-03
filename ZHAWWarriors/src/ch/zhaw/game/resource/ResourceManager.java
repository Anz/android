package ch.zhaw.game.resource;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class ResourceManager {	
	private Map<String, Texture> imgs = new HashMap<String, Texture>();
	private SimpleBaseGameActivity context;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	
	public ResourceManager(SimpleBaseGameActivity context) {
		this.context = context;
		context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	}
	
	public Texture getTexture(String name) {
		if (!imgs.containsKey(name)) {
			try {
				Drawable drawable = Drawable.createFromStream(context.getResources().getAssets().open("gfx/" + name), null);
				int height = drawable.getIntrinsicHeight() * 2;
				int width =  drawable.getIntrinsicWidth() * 2;
				
				BitmapTextureAtlas texture = new BitmapTextureAtlas(context.getTextureManager(), width, height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, name, 0, 0);
				texture.load();
				imgs.put(name, texture);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return imgs.get(name);
	}

//	public TiledTextureRegion loadTexture(String name, int xFrames, int yFrames) {
//		if (!imgs.containsKey(name)) {
//			try {
//				Drawable drawable = Drawable.createFromStream(context.getResources().getAssets().open("gfx/" + name), null);
//				int height = drawable.getIntrinsicHeight() * 2;
//				int width =  drawable.getIntrinsicWidth() * 2;
//				
//				BitmapTextureAtlas texture = new BitmapTextureAtlas(context.getTextureManager(), width, height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//				TiledTextureRegion textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, context, name, 0, 0, xFrames, yFrames);
//				texture.load();
//				imgs.put(name, textureRegion);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return imgs.get(name);
//	}
	
	public TiledTextureRegion getTextureRegion(Texture texture, int xFrames, int yFrames) {
		return TextureRegionFactory.extractTiledFromTexture(texture, xFrames, yFrames);
	}
	
	public TiledTextureRegion getTextureRegion(String name, int xFrames, int yFrames) {
		return getTextureRegion(getTexture(name), xFrames, yFrames);
	}
	
	public Activity getActivity() {
		return context;
	}

	public AssetManager getAssetManager() {
		return context.getAssets();
	}

	public SimpleBaseGameActivity getContext() {
		return context;
	}

	public TextureManager getTextureManager() {
		return context.getTextureManager();
	}

	public VertexBufferObjectManager getVboManager() {
		return context.getVertexBufferObjectManager();
	}
	
	public int getDisplayWidth() {
		return displayMetrics.widthPixels;
	}
	
	public int getDisplayHeight() {
		return displayMetrics.heightPixels;
	}
}
