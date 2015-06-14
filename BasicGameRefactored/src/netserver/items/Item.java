/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.items;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import netclient.gui.ModuleType;
import netserver.WJSFServer;
import netserver.modules.BasicModule;
import netserver.physics.JBox2dNode;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.services.updater.IUpdateable;
import netserver.shipdesigns.TestShipDesigns;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author 1337
 */
public class Item extends JBox2dNode implements IUpdateable {
    
    protected ModuleType type;
    protected Body body;
    protected ArrayList<Spatial> spatials;
    protected Material material;
    protected WJSFServer app;
    protected boolean collected;
    
    public Item (ModuleType type, ArrayList<Spatial> spatials, Vec2 spawnPoint, Quaternion rotation, WJSFServer app) {
        super();
        this.app = app;
        this.type = type;
        this.spatials = spatials;
        ServiceManager.getUpdateableManager().addUpdateable(this);
        
        generatePhysicsBody(spawnPoint.x, spawnPoint.y);
        setPhysicsCenter(body);

        for(Spatial s: spatials) {
            app.getRootNode().attachChild(s);
            //s.setLocalTranslation(new Vector3f(spawnPoint.x, 0, spawnPoint.y));
            s.setLocalScale(0.5f);
        }
        this.updateBoxPosition();
        app.getRootNode().attachChild(this);
    }

    private void generatePhysicsBody(float x, float y) {
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(0.5f, 0.5f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = rect;
        fDef.density = 0.01f;
        fDef.friction = 0.0f;
        fDef.filter.categoryBits = TestShipDesigns.CATEGORY_ITEM;
        fDef.filter.maskBits = TestShipDesigns.MASK_ITEM;

        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.angle = (float) Math.atan2(0, 0);
        bDef.type = BodyType.DYNAMIC;
        bDef.bullet = true;
        bDef.allowSleep = false;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.setUserData(this);
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
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateBoxPosition();
    }
   
   public void handleShipCollision(BasicModule m) {
        if(!collected) {
            collected = true;
            m.getShip().collectItem(type);
            markForDeletion();
        }
    }
    
    public void delete() {
        ServiceManager.getUpdateableManager().removeUpdateable(this);
        PhysicsWorld.world.destroyBody(body);
        
        for(Spatial s: spatials) {
                s.removeFromParent();
            }
        this.removeFromParent();
    }
    
    protected void markForDeletion() {
        app.itemsToRemove.add(this);
     }
}