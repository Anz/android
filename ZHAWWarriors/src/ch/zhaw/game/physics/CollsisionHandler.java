package ch.zhaw.game.physics;

import ch.zhaw.game.entity.EntityController;
import ch.zhaw.game.entity.EntityControllerInvoker;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollsisionHandler implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		EntityController entityControllerA = (EntityController)contact.getFixtureA().getUserData();
		EntityController entityControllerB = (EntityController)contact.getFixtureB().getUserData();
		
		if (entityControllerA == null || entityControllerB == null) {
			return;
		}
		
		EntityControllerInvoker.invoke(entityControllerA, "contact", entityControllerB);
		EntityControllerInvoker.invoke(entityControllerB, "contact", entityControllerA);
	}

	@Override
	public void endContact(Contact contact) {
		EntityController entityControllerA = (EntityController)contact.getFixtureA().getUserData();
		EntityController entityControllerB = (EntityController)contact.getFixtureB().getUserData();
		
		if (entityControllerA == null || entityControllerB == null) {
			return;
		}
		
		EntityControllerInvoker.invoke(entityControllerA, "contact_end", entityControllerB);
		EntityControllerInvoker.invoke(entityControllerB, "contact_end", entityControllerA);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
}
