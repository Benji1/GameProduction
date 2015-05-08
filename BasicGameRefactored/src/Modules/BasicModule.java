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
import java.awt.Point;
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
    protected Body body;
    protected Spatial spatial;
    protected Material material;
    
    protected Point posInShip;

    public BasicModule() {
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

    public void update(float tpf) {
        Vector3f bodyPos = new Vector3f(
                (float) body.getWorldPoint(body.getLocalCenter()).x, 0.0f, (float) body.getWorldPoint(body.getLocalCenter()).y);

        spatial.setLocalTranslation(bodyPos);

        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
        spatial.setLocalRotation(q);
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
        
        Box box = new Box(1, 0.4f, 1);
        spatial = new Geometry("Box", box);
        material = new Material(ship.getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);

        spatial.setMaterial(material);
        int x = ship.getActualPositionInGrid(this).y * 2;
        int y = ship.getActualPositionInGrid(this).x * 2;
        generatePhysicsBody(x, y);

        ship.attachChild(this);
        this.attachChild(spatial);
        
        lockToShip();
    }

    public void otherModulePlaced(BasicModule module, Point p) {
    }

    public void otherModuleRemoved(BasicModule module, Point p) {
    }

    private void generatePhysicsBody(int x, int y) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(1, 1);

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
        body.setUserData(this);
        PhysicsWorld.world.setContactListener(this);
    }
    
    public void destroy() {
        this.detachChild(spatial);
        //body.destroyFixture(body.getFixtureList());
        ship.getApp().bodiesToRemove.add(body);
        ship.removeModuleAt(ship.getActualPositionInGrid(this));
        
        //ship.getApp().getRootNode().attachChild(this);
        // SPAWN WITH DROPABILITY OR JUST DESTROY
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
    }

    public void endContact(Contact cntct) {
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
}