/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import netserver.BasicShip;
import netserver.items.EncapsulatingItem;
import netserver.physics.JBox2dNode;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.services.config.ConfigReader;
import netserver.shipdesigns.TestShipDesigns;
import netutil.NetMessages;
import netutil.NetMessages.ExplosionParticleMsg;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

/**
 *
 * @author 1337
 */
public abstract class BasicModule extends JBox2dNode  {

    ConfigReader cr = ServiceManager.getConfigReader();
    protected int maxHealth = cr.getFromMap(cr.getBaseMap("Basic"), "MaxHealth", int.class);
    protected int health = maxHealth;
    protected float dropRateInPercent = cr.getFromMap(cr.getBaseMap("Basic"), "DropRateInPercent", float.class);
    protected float linearDampingFactor = cr.getFromMap(cr.getBaseMap("Thruster"), "LinearDamping", float.class);
    protected BasicShip ship;
    protected String moduleName;
    protected ColorRGBA color = ColorRGBA.Gray;
    protected Body body;
    protected Spatial spatial;
    protected Material material;  
    protected ModuleType type;
    protected FacingDirection orientation;
    private float oldDamping = 10000f;
    
    public int group = 0;

    public BasicModule() {
        super();
    }

    private void lockToShip() {
        Point pos = ship.getActualPositionInGrid(this);
        
        lockTo(ship.getModule(new Point(pos.x+1, pos.y)));
        lockTo(ship.getModule(new Point(pos.x-1, pos.y)));
        lockTo(ship.getModule(new Point(pos.x, pos.y+1)));
        lockTo(ship.getModule(new Point(pos.x, pos.y-1)));
    }
    
    private void lockTo(BasicModule lockon) {
        if(lockon != null) {
            WeldJointDef wjDef = new WeldJointDef();
            wjDef.initialize(lockon.body, this.body, lockon.body.getPosition());
            wjDef.collideConnected = false;
            PhysicsWorld.world.createJoint(wjDef);
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public Body getBody() {
        return body;
    }
    
    public BasicShip getShip() {
        return ship;
    }
    
    public Spatial getSpatial() {
    	return this.spatial;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public Vec2 getVelocity() {
        return body.getLinearVelocity();
    }
    public float getAngVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            destroy();
        }
    }

    public int getHealth() {
        return health;
    }

    public void onPlaced(BasicShip ship) {
        this.ship = ship;
        
        create3DBody();
        int x = ship.getActualPositionInGrid(this).x * 2;
        int y = ship.getActualPositionInGrid(this).y * 2;
        if(ship != null && ship.cockpitPos != null){
        		x += ship.cockpitPos.x;
        		y += ship.cockpitPos.z;
        }
        generatePhysicsBody(x, y);
        setPhysicsCenter(body);

        this.attachChild(spatial);
        ship.attachChild(this);
        
        lockToShip();
    }
    
    protected void create3DBody() {
        Box box = new Box(1, 0.4f, 1);
        spatial = new Geometry("Box", box);
        material = new Material(ship.getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);

        spatial.setMaterial(material);
    }
    
    
    public void onMovedToOtherShip (BasicShip s) {
        this.ship = s;
    }
    
    public void onRemove() {
        ship.removeModuleAt(ship.getActualPositionInGrid(this));
        
        if(!ship.hasStillModules()) {
            ship.delete();
        }
    }

    public void otherModulePlaced(BasicModule module, Point p) {
    }

    public void otherModuleRemoved(BasicModule module, Point p) {
    }

    private void generatePhysicsBody(int x, int y) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(0.95f, 0.95f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        fDef.filter.categoryBits = TestShipDesigns.CATEGORY_SHIP;
        fDef.filter.maskBits = TestShipDesigns.MASK_SHIP;
                             
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyType.DYNAMIC;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.setUserData(this);
        body.setLinearDamping(linearDampingFactor);
    }
    
    public void destroy() {
        onRemove();
        
        Explosion exp = new Explosion(
                ship.getApp().getAssetManager(), 
                new Vector3f (this.body.getPosition().x, 0, this.body.getPosition().y), 
                ship.getApp().getRootNode()
                );
        
        ExplosionParticleMsg msg = new ExplosionParticleMsg(new Vec2(this.body.getPosition().x, this.body.getPosition().y));
        ship.getApp().getServer().broadcast(msg);
        
        if (shouldSpawnItem()) {
            spawnItem();
        }
        
        this.detachAllChildren();
        ship.getApp().bodiesToRemove.add(body);
        ship.seperateInNewShips();
    }
    
    public boolean shouldSpawnItem() {
        Random rn = new Random();
        return rn.nextFloat() <= dropRateInPercent / 100;
    }
    
    public void spawnItem() {
        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
        
        ArrayList<Spatial> saveSpatials = new ArrayList<Spatial>();
        for(Spatial s : children) {
            saveSpatials.add(s.clone());
        }
        
        OrientedModule om = new OrientedModule(type, orientation);
        ship.getApp().itemsToCreate.add(new EncapsulatingItem(om, saveSpatials, body.getPosition(), q, ship.getApp()));
    }
    
     public void destroyWithoutSeperation() {
        onRemove();
        spatial.removeFromParent();
        ship.getApp().bodiesToRemove.add(body);
    }

    // HELPER METHOD MAYBE SOMEWHERE ELSE WOULD BE A BETTER PLACE
    public static float fillNotOverLimit(float actualValue, float increase, float limit) {
        if (actualValue + increase > limit) {
            return limit;
        } else {
            return actualValue + increase;
        }
    }
    
    public ModuleType getType() {
        return type;
    }
    public FacingDirection getOrientation() {
        return orientation;
    }
    
    public void toggleDamping(){
    	float temp = oldDamping;
    	oldDamping = body.getLinearDamping();
    	body.setLinearDamping(temp);
    }
}
