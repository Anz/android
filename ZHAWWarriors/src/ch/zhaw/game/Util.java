package ch.zhaw.game;

public final class Util {
	private Util() {}
	
	public static float limit(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
}
