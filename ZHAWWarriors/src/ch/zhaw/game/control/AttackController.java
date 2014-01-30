package ch.zhaw.game.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;

import ch.zhaw.game.Util;
import ch.zhaw.game.entity.Entity;

public class AttackController implements IUpdateHandler {
	private class Connection {
		public Entity attacter;
		public Entity victim; 
		
		public Connection(Entity attacter, Entity victim) {
			this.attacter = attacter;
			this.victim = victim;
		}
	}
	
	private List<Connection> connections = new ArrayList<Connection>();

	public AttackController(List<Entity> attacters, List<Entity> targets) {
		for (Entity attacter : attacters) {
			connections.add(new Connection(attacter, targets.get(0)));
		}
	}

	@Override
	public void onUpdate(float seconds) {
		for (Connection connection : connections) {
			Entity attacter = connection.attacter;
			Entity victim = connection.victim;
			Util.move(attacter.getBody().getPosition(), victim.getBody().getPosition(), attacter.getSpeed(), seconds);
		}
	}

	@Override
	public void reset() {
	}
}
