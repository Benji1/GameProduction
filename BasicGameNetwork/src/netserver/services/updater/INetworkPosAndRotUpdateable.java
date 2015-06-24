/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.updater;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.jbox2d.common.Vec2;

public interface INetworkPosAndRotUpdateable {
    
    public Vector3f get3DTranslation();
    public Quaternion getRotation();
    public Vec2 getVelocity();
    public float getAngVelocity();
    public int getId();
    
}
