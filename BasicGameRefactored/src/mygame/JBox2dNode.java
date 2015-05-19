package mygame;

import org.jbox2d.dynamics.Body;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import universe.Abs_ChunkNode;

public class JBox2dNode extends Abs_ChunkNode {
	protected Body physicsCenter;
	private Vector3f bodyPos;
	private Quaternion bodyAngle;
	
	public JBox2dNode(Main app, String name, Abs_ChunkNode.ChunkNodeType type) {
		super(app, name, type);
		
		this.bodyPos = Vector3f.ZERO;
		this.bodyAngle = new Quaternion();
	}
	
	public void setPhysicsCenter(Body c) {
		this.physicsCenter = c;
	}
	
	public Body getPhysicsCenter() {
		return this.physicsCenter;
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		// update pos
     		this.bodyPos.set(
                (float)this.physicsCenter.getWorldPoint(this.physicsCenter.getLocalCenter()).x, 
                this.getLocalTranslation().y, 
                (float)this.physicsCenter.getWorldPoint(this.physicsCenter.getLocalCenter()).y);
        this.setLocalTranslation(bodyPos);
         
        // update rotation
        this.bodyAngle.fromAngleAxis(-this.physicsCenter.getAngle(), new Vector3f(0f, 1f, 0f));
        this.setLocalRotation(this.bodyAngle);
	}
}