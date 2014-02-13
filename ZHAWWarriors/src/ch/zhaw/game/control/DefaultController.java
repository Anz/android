package ch.zhaw.game.control;

import ch.zhaw.game.entity.Controller;
import ch.zhaw.game.entity.Entity;
import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.Event;

@Controller
public class DefaultController extends EntityController {

	@Event(filter="create")
	public Entity onCreate() {
		return super.create();
	}
}
