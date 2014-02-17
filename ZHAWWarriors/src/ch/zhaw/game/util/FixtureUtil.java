package ch.zhaw.game.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class FixtureUtil {
	private static int seq = 0;
	private static Map<String, Integer> partyGroupMap = new HashMap<String, Integer>();
	
	
	public static FixtureDef createCircularFixture(String party) {
		if (!partyGroupMap.containsKey(party)) {
			partyGroupMap.put(party, seq++);
		}
		
		Integer index = partyGroupMap.get(party);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.groupIndex = (short) -index;
		fixtureDef.shape = shape;
		return fixtureDef;
	}
	
	public static FixtureDef createBoxFixture(String party, float width, float height) {
		if (!partyGroupMap.containsKey(party)) {
			partyGroupMap.put(party, seq++);
		}
		
		Integer index = partyGroupMap.get(party);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.filter.groupIndex = (short) -index;
		fixtureDef.friction = 0.01f;
		fixtureDef.density = 1;
		fixtureDef.shape = shape;
		return fixtureDef;
	}
}
