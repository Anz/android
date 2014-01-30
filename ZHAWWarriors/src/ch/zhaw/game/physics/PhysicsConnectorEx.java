package ch.zhaw.game.physics;

import java.util.Iterator;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsConnectorEx extends PhysicsConnector {

	public PhysicsConnectorEx(IAreaShape pAreaShape, Body pBody) {
		super(pAreaShape, pBody);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);
		
		if (mBody.isActive()) {
			mShape.setColor(0f, 1f, 0f, 0.5f);
		} else if (mBody.isAwake()) {
			mShape.setColor(1f, 1f, 0f);
		} else {
			mShape.setColor(1f, 0f, 0f);
		}
		
		World world = mBody.getWorld();
		for (Iterator<Body> body = world.getBodies(); body.hasNext();) {
			if (body.next() == mBody) {
				return;
			}
		}
		
		GameScene scene = (GameScene)mShape.getParent();
		scene.getPhysicsWorld().unregisterPhysicsConnector(this);
		scene.detachChild(mShape);
		
	}
	
	

}
