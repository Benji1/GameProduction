package netserver.universe;

import netserver.WJSFServer;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class CelestialBody extends PhysicsBody {
	int ID;
	Geometry model;
	private WJSFServer app;
	

	public CelestialBody(float mass, Vector3f position, WJSFServer app) {
		super(mass, position);
		this.app = app;
	}
	
}
