/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import netclient.WJSFClient;
import netclient.gui.ModuleType;
import netserver.modules.BasicModule;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.shipdesigns.TestShipDesigns;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author 1337
 */

public class GItem extends Node {
    
    protected ModuleType type;
    protected Body body;
    protected ArrayList<Spatial> spatials;
    protected Material material;
    protected WJSFClient app;
    protected boolean collected;
    
    public GItem (ModuleType type, ArrayList<Spatial> spatials, Vec2 spawnPoint, Quaternion rotation, WJSFClient app) {
        super();
        this.app = app;
        this.type = type;
        this.spatials = spatials;

        for(Spatial s: spatials) {
            app.getRootNode().attachChild(s);
            s.setLocalScale(0.5f);
        }
        this.updateBoxPosition();
        app.getRootNode().attachChild(this);
    }
    
    protected final void updateBoxPosition() {
        Vector3f bodyPos = new Vector3f(
                (float) body.getWorldPoint(body.getLocalCenter()).x,
                0.0f,
                (float) body.getWorldPoint(body.getLocalCenter()).y);

        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));

        if(spatials != null) {
            for(Spatial s: spatials) {
                s.setLocalTranslation(bodyPos);
                s.setLocalRotation(q);
            }
        }
    }
    
    public void update(float tpf) {
        updateBoxPosition();
    }
    
    public void delete() {
        PhysicsWorld.world.destroyBody(body);
        
        for(Spatial s: spatials) {
                s.removeFromParent();
            }
        this.removeFromParent();
    }
}