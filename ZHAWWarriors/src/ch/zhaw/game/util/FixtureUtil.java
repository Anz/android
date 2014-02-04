package ch.zhaw.game.util;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
}
