package ch.zhaw.game.util;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	public static String readFile(InputStream inputStream) throws IOException {
		int size = inputStream.available();
		byte[] buffer = new byte[size];
		inputStream.read(buffer);
		inputStream.close();
		return new String(buffer, "UTF-8");
	}
}
