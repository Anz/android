package ch.zhaw.game.util;

public class StringUtils {
	public static boolean isEmtpy(String str) {
		return str == null || "".equals(str);
	}
	
	public static boolean isBlank(String str) {
		return "".equals(str);
	}
}
