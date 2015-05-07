/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import config.ConfigReader;
import mygame.BasicShip;
import mygame.PhysicsWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.WeldJointDef;

/**
 *
 * @author 1337
 */
public abstract class BasicModule extends Node implements ContactListener {

    // RIGIDBODY OBJECT (MASS, COLLIDER)
    // GRAPHICAL STUFF
    // DIRECTION THE BLOCK FACES
    protected int maxHealth = ConfigReader.getFromMap(ConfigReader.getBaseMap("Basic"), "MaxHealth", int.class);
    protected int health = maxHealth;
    protected float dropRateInPercent = ConfigReader.getFromMap(ConfigReader.getBaseMap("Basic"), "DropRateInPercent", float.class);
    protected BasicShip ship;
    protected String moduleName;
    protected ColorRGBA color = ColorRGBA.Gray;
    protected Spatial b;
    protected Body body;
    protected Spatial spatial;
    protected Material material;

    public BasicModule() {}
    
    public void lockToShip() {
        WeldJointDef wjDef = new WeldJointDef();        
        wjDef.initialize(ship.getPhysicsCenter(), this.body, ship.getPhysicsCenter().getPosition());
        wjDef.collideConnected = false;
        PhysicsWorld.world.createJoint(wjDef);
    }

    public String getModuleName() {
        return moduleName;
    }
    
    public Body getBody() {
        return body;
    }
    
    public Spatial getSpatial() {
    	return this.spatial;
    }
    
    public void update(float tpf) {}

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

    public void destroy() {
        // SPAWN WITH DROPABILITY OR JUST DESTROY
    }

    public void onPlaced(BasicShip ship) {
        this.ship = ship;
        
        // create module model
        Box box = new Box(1, 1, 1);
        spatial = new Geometry("Box", box);
        spatial.scale(1f, 1f, 1f);
        material = new Material(ship.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        
        material.setBoolean("UseMaterialColors",true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);

        spatial.setMaterial(material);
        
        // create physics
        int x = (ship.getPositionInGrid(this).y * 2) - 4;
        int y = (ship.getPositionInGrid(this).x * 2) - 4;

        generatePhysicsBody(x, y);
        
        // position jme node
        Vector3f bodyPos = new Vector3f(
                (float)body.getWorldPoint(body.getLocalCenter()).x, 
                0.0f, 
                (float)body.getWorldPoint(body.getLocalCenter()).y);

		this.setLocalTranslation(bodyPos);
		 
		float angleRad = body.getAngle();
		Quaternion q = new Quaternion();
		q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
		this.setLocalRotation(q);
        
        this.ship.attachChild(this);
        this.attachChild(spatial);
    }

    private void generatePhysicsBody(int x, int y) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(1, 1);
        //CircleShape circle = new CircleShape();
        //circle.m_radius = 1.0f;
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        //fDef.restitution = 0.5f;
        
        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyType.DYNAMIC;
        
        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        
        PhysicsWorld.world.setContactListener(this);
    }
    
    public void onRemoved() {
    }

    // HELPER METHOD MAYBE SOMEWHERE ELSE WOULD BE A BETTER PLACE
    public static float fillNotOverLimit(float actualValue, float increase, float limit) {
        if (actualValue + increase > limit) {
            return limit;
        } else {
            return actualValue + increase;
        }
    }
    
    public void beginContact(Contact cntct) {
            //System.out.println("beginContact");
    }

    public void endContact(Contact cntct) {
            //System.out.println("endContact");
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
            //System.out.println("preSolve");
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
            //System.out.println("postSolve");
    }
}
