package ch.zhaw.game.entity;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import ch.zhaw.game.resource.ResourceManager;

public class TextHandler extends org.andengine.entity.Entity  implements ITouchArea {
	private static int MAX_LINES = 3;
	private Scene scene;
	private int begin = 0;
	private int end = 1;
	private int maxLength;
	private float speed = 1;
	private int line = 0;
	private Text text;
	private boolean cursor = false;
	private int i = 0;
	private int wait = 2;
	private TimerHandler timerHandler;
	
	public TextHandler(final Scene scene, ResourceManager resourceManager, final String str, float x, float y, float width, float height, final float seconds) {
		//setZIndex(10000);
		
		Rectangle body = new Rectangle(x, y, width, height, resourceManager.getVboManager());
		body.setColor(Color.BLACK);
		attachChild(body);
		
		Rectangle border = new Rectangle(x, y, width, 2, resourceManager.getVboManager());
		border.setColor(Color.WHITE);
		attachChild(border);
		
		this.scene = scene;
		maxLength = str.length();
		text = new Text(x+50, y+30, resourceManager.getFont(), str, resourceManager.getVboManager());
		text.setText("");
		//text.setZIndex(2000);
		
		timerHandler = new TimerHandler(seconds, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler timerHandler) {
				if (maxLength <= end ) {
					line = MAX_LINES;
				}
				
				if (cursor) {
					text.setText(str.substring(begin, end).replaceAll("\n+$", "") + "_");
				} else {
					text.setText(str.substring(begin, end).replaceAll("\n+$", ""));
				}
				
				cursor = !cursor;
				
				if (i % wait != 0) {
					i++;
					return;
				}
				
				if (line >= MAX_LINES) {
					timerHandler.setTimerSeconds(0.25f);
					return;
				}
				
				end++;
				char lastChar = str.charAt(end-1);
				if (lastChar == '\n') {
					line++;
				}
				
				if ("?!.".indexOf(lastChar) > 0) {
					timerHandler.setTimerSeconds(seconds * speed * 2);
				} else if (" \n".indexOf(lastChar) > 0) {
					timerHandler.setTimerSeconds(seconds * speed / 2);
				} else {
					timerHandler.setTimerSeconds(seconds * speed);
				}
			}
		});
		timerHandler.setAutoReset(true);
		
		attachChild(text);
		registerUpdateHandler(timerHandler);
		scene.setIgnoreUpdate(true);
		scene.registerTouchArea(this);
	}

	@Override
	public boolean contains(float pX, float pY) {
		return true;
	}

	@Override
	public boolean onAreaTouched(TouchEvent event, float x, float y) {
		if (!event.isActionDown())  {
			return false;
		}
		
		if (line >= MAX_LINES) {
			if (maxLength <= end ) {
				detroy();
				return false;
			}
			begin = end;
			line = 0;
			speed = 1f;
		} else {
			speed = 0.1f;
		}
		return false;
	}
	
	private void detroy() {
//		scene.unregisterTouchArea(this);
		unregisterUpdateHandler(timerHandler);
		detachSelf();
		scene.setIgnoreUpdate(false);
	}
	
}
