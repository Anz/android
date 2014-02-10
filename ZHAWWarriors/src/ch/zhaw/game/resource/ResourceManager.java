package ch.zhaw.game.resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.w3c.dom.Document;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.DisplayMetrics;
import android.util.Log;

public class ResourceManager {	
	private Map<String, Texture> imgs = new HashMap<String, Texture>();
	private SimpleBaseGameActivity context;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private Font font;
	
	public ResourceManager(SimpleBaseGameActivity context) {
		this.context = context;
		context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
        font = FontFactory.createFromAsset(context.getFontManager(), context.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR, context.getAssets(), "font/cour.ttf", 40, true, Color.WHITE_ABGR_PACKED_INT);
		font.load();
	}
	
	public Texture getTexture(String name) {
//		if (!imgs.containsKey(name)) {
//			try {
//				Drawable drawable = Drawable.createFromStream(context.getResources().getAssets().open("gfx/" + name), null);
//				int height = drawable.getIntrinsicHeight() * 2;
//				int width =  drawable.getIntrinsicWidth() * 2;
//				
//				BitmapTextureAtlas texture = new BitmapTextureAtlas(context.getTextureManager(), width, height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//				BitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, name, 0, 0);
//				texture.load();
//				imgs.put(name, texture);
//			} catch (Exception e) {
//				Log.e("resource", "could not load texture", e);
//			}
//		}
//		
//		return imgs.get(name);
		return getTexture2(name);
	}
	
	public Texture getTexture2(String name) {
		if (!imgs.containsKey(name)) {
			try {
				String fileName = "gfx/" + name;
				InputStream file = context.getResources().getAssets().open(fileName);
		        
		        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		        Document doc = builder.parse(file);
		        XPath xpath = XPathFactory.newInstance().newXPath();
		        
		        int width = ((Number)xpath.evaluate("//svg/@width", doc, XPathConstants.NUMBER)).intValue();
		        int height = ((Number)xpath.evaluate("//svg/@height", doc, XPathConstants.NUMBER)).intValue();
				
				BitmapTextureAtlas texture = new BitmapTextureAtlas(context.getTextureManager(), width, height, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
				
				SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(texture, context, fileName, width, height, null, 0, 0);
				texture.load();
				imgs.put(name, texture);
			} catch (Exception e) {
				Log.e("resource", "could not load texture", e);
			}
		}
		
		return imgs.get(name);
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

	public Font getFont() {
		return font;
	}
	
	
}
