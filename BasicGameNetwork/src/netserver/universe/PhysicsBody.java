package netserver.universe;

import java.util.List;

import com.jme3.math.Vector3f;

public class PhysicsBody {
	
	public float mass;
	protected Vector3f velocity;
	private Vector3f force1;
	private Vector3f force2;
	public Vector3f position;
	public Vector3f lastPosition;
	
	public PhysicsBody(float mass, Vector3f position){
		this.mass = mass;
		this.position = position;
		this.velocity = Vector3f.ZERO;
	}
	
	public void updateForce(List<CelestialBody> bodies){
		this.force2 = force1;
		this.force1 = Vector3f.ZERO;
		for (PhysicsBody body: bodies){
			if (body == this)
				continue;
			Vector3f dis = body.position.subtract(this.position);
			
			float len = dis.length();
			len = Math.max(len, 0.1f);
			float force = Universe.G * (body.mass * this.mass) / (len*len);
			Vector3f dir = dis.normalize();
			this.force1 = this.force1.add(dir.mult(force));
		}
		
		
	}
	
	public void updatePosition(){
		Vector3f a = this.force1.divide(mass);
		this.lastPosition = this.position;
		this.position = this.position.add((velocity.add((a.mult(Universe.P_DT/2f)))).mult(Universe.P_DT));
	}
	
	public void updateVelocity(){
		Vector3f a1 = this.force1.divide(mass);
		Vector3f a2 = this.force2.divide(mass);
		this.velocity = this.velocity.add((a1.add(a2)).mult(Universe.P_DT/2f));
	}
		
}
