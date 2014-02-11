package ch.zhaw.game.util;

import com.badlogic.gdx.math.Vector2;

public final class MathUtil {
	private MathUtil() {}
	
	public static float distance(Vector2 a, Vector2 b) {
		return a.cpy().sub(b).len();
	}
	
	public static float limit(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static Vector2 move(final Vector2 source, final Vector2 target,  final float speed, final float seconds) {
		if (source.equals(target)) {
			return source;
		}
		
		
        Vector2 direction = target.cpy().sub(source);
        float distance = direction.len();
        float travelDistance = speed * seconds;

        // the target is very close, so we set the position to the target directly
        if (distance <= travelDistance) {
            return target;
        }
        
        return source.cpy().add(direction.nor().mul(travelDistance));
	}
}
