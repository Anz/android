package ch.zhaw.game.activity;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;

import android.content.Intent;


public class MenuActivity extends AbstractBaseActivity implements IOnMenuItemClickListener {
	@Override
	protected Scene onCreateScene() {
		Intent intent = new Intent(this, GameActivity.class);
	    intent.putExtra(GameActivity.KEY_MAP, "scene/land.json");
	    startActivity(intent);
		return null;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		return false;
	}
}
