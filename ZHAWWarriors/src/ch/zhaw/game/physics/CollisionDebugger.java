package ch.zhaw.game.physics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.Shape;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import ch.zhaw.game.scene.GameScene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class CollisionDebugger implements IUpdateHandler {
	private GameScene scene;
	private Map<Fixture, Shape> bodies = new HashMap<Fixture, Shape>();
	
	public CollisionDebugger(GameScene scene) {
		this.scene = scene;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		Iterator<Body> it = scene.getPhysicsWorld().getBodies();
		while (it.hasNext()) {
			Body body = it.next();
			
			for (Fixture fixture : body.getFixtureList()) {
				Shape shape = bodies.get(fixture);
				
				if (shape == null) {
					float width = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					float height = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
					
					if (fixture.getShape() instanceof CircleShape) {
						width *= ((CircleShape)fixture.getShape()).getRadius() * 2;
						height = width;
					} else if (fixture.getShape() instanceof PolygonShape) {
						PolygonShape polygon = ((PolygonShape)fixture.getShape());
						float minX = 0, minY = 0, maxX = 0, maxY = 0;
						Vector2 vertex = new Vector2();
						for (int i = 0; i < polygon.getVertexCount(); i++) {
							polygon.getVertex(i, vertex);
							minX = Math.min(minX, vertex.x);
							minY = Math.min(minY, vertex.y);
							maxX = Math.max(maxX, vertex.x);
							maxY = Math.max(maxY, vertex.y);
						}
						width *= maxX - minX;
						height *= maxY - minY;
					}
					
					shape = new Rectangle(0, 0, width, height, scene.getResourceManager().getVboManager());
					shape.setColor(0f, 1f, 0f, 0.5f);
					shape.setZIndex(100000);
					scene.attachChild(shape);
					bodies.put(fixture, shape);
					
					scene.getPhysicsWorld().registerPhysicsConnector(new PhysicsConnectorEx((IAreaShape)shape, body));
				}
			}
		}
		
	}

	@Override
	public void reset() {
	}

}
