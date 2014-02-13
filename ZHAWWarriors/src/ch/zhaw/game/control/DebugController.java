package ch.zhaw.game.control;

import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.Event;

@Controller(name="debug")
public class DebugController extends TriggerController {
	private static boolean debugMode = false;

	@Event
	public void onEvent(String event) {
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
