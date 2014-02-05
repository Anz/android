package ch.zhaw.game.control;


public class DebugController extends ButtonController {
	private static boolean debugMode = false;

	@Override
	protected void onEvent(String event) {
		super.onEvent(event);
		
		// check event
		if (!event.equals(this.event)) {
			return;
		}

		debugMode = !debugMode;
	}

	public static boolean isDebugMode() {
		return debugMode;
	}
}
