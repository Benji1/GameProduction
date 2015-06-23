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
import netclient.gui.OrientedModule;
import netserver.WJSFServer;
import netserver.modules.BasicModule;
import netserver.physics.JBox2dNode;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.services.updater.INetworkPosAndRotUpdateable;
import netserver.services.updater.IUpdateable;
import netserver.shipdesigns.TestShipDesigns;
import netutil.NetMessages;
import netutil.NetMessages.DeleteGraphicObjectMsg;
import netutil.NetMessages.SpawnItemMsg;
import org.jbox2d.collision.shapes.CircleShape;
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
public class Item extends JBox2dNode implements IUpdateable, INetworkPosAndRotUpdateable {
    
    protected OrientedModule om;
    protected Body body;
    protected ArrayList<Spatial> spatials;
    protected Material material;
    protected WJSFServer app;
    protected boolean collected;
    
    protected int id;
    
    public Item (OrientedModule om, ArrayList<Spatial> spatials, Vec2 spawnPoint, Quaternion rotation, WJSFServer app) {
        super();
        this.app = app;
        this.om = om;
        this.spatials = spatials;
        ServiceManager.getUpdateableManager().addUpdateable(this);
        ServiceManager.getUpdateableManager().addNetworkUpdateable(this);
        
        id = ServiceManager.getIdProvider().getFreeId();
        
        // network spawn msg
        SpawnItemMsg msg = new SpawnItemMsg(id, spawnPoint, rotation, om);
        msg.setReliable(true);
        app.getServer().broadcast(msg);

        generatePhysicsBody(spawnPoint.x, spawnPoint.y);
        setPhysicsCenter(body);

        for(Spatial s: spatials) {
            app.getRootNode().attachChild(s);
            s.setLocalScale(0.5f);
        }
        this.updateBoxPosition();
        app.getRootNode().attachChild(this);
    }

    private void generatePhysicsBody(float x, float y) {
        // for collision
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(0.5f, 0.5f);
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = rect;
        fDef.density = 0.01f;
        fDef.friction = 0.0f;
        fDef.filter.categoryBits = TestShipDesigns.CATEGORY_ITEM;
        fDef.filter.maskBits = TestShipDesigns.MASK_ITEM;
        
        // sensor for moving towards player for easier collecting        
        CircleShape sensorShape = new CircleShape();
        sensorShape.m_radius = ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("Storage"), "SensorRange", float.class);
        
        FixtureDef sensorDef = new FixtureDef();
        sensorDef.shape = sensorShape;
        sensorDef.isSensor = true;
        sensorDef.filter.categoryBits = TestShipDesigns.CATEGORY_ITEM;
        sensorDef.filter.maskBits = TestShipDesigns.MASK_ITEM;

        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.angle = (float) Math.atan2(0, 0);
        bDef.type = BodyType.DYNAMIC;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.createFixture(sensorDef);
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
            if(m.getShip().collectItem(om.moduleType)) {
                collected = true;
                markForDeletion();
            }
        }
    }
    
    public void delete() {
        PhysicsWorld.world.destroyBody(body);
        
        for(Spatial s: spatials) {
            s.removeFromParent();
        }
        
        DeleteGraphicObjectMsg msg = new NetMessages.DeleteGraphicObjectMsg(id);
        msg.setReliable(true);
        app.getServer().broadcast(msg);
        ServiceManager.getUpdateableManager().removeUpdateable(this);
        ServiceManager.getUpdateableManager().removeNetworkUpdateable(this);
        
        this.removeFromParent();
    }
    
    protected void markForDeletion() {
        app.itemsToRemove.add(this);
     }

    public Vector3f getTranslation() {
        Vector3f bodyPos = new Vector3f(
                (float) body.getWorldPoint(body.getLocalCenter()).x,
                0.0f,
                (float) body.getWorldPoint(body.getLocalCenter()).y);
        
        
        return bodyPos;
    }

    public Quaternion getRotation() {
        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
        
        return q;
    }

    public Vec2 getVelocity() {
        return this.physicsCenter.getLinearVelocity();
    }

    public float getAngVelocity() {
        return this.physicsCenter.getAngularVelocity();
    }

    public int getId() {
        return id;
    }
    
    public void handleModuleSensor(Vec2 modulePos) {
        Vec2 force = modulePos.sub(body.getPosition());
        
        body.applyForce(force, body.getPosition());
    }
}