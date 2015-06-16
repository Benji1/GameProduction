/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import netutil.NetMessages;
import netutil.NetMessages.GraphicObjPosAndRotMsg;
import org.jbox2d.common.Vec2;

public abstract class GraphicObject extends Node {   
    
    private Vec2 velocity;
    private float angVelocity;
    
    public GraphicObject() {
        this.velocity = new Vec2();
        this.angVelocity = 0;
    }
    
    public void updatePosition(Vector3f pos) {
        this.setLocalTranslation(pos);
    }
    public void updateRotation(Quaternion rot) {
        this.setLocalRotation(rot);
    }
    public void updateVelocity(Vec2 vel) {
        this.velocity = vel;
    }
    public void updateAngVelocity(float angVel) {
        this.angVelocity = angVel;
    }
    
    public void update(GraphicObjPosAndRotMsg msg) {
        updatePosition(msg.getPos());
        updateRotation(msg.getRot());
        updateAngVelocity(msg.getAngVel());
        updateVelocity(msg.getVelocity());
    }
    
    public abstract void delete();
    
}
