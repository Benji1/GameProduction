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
import mygame.BasicShip;
import mygame.PhysicsWorld;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

/**
 *
 * @author 1337
 */
public abstract class BasicModule extends Node {

    // RIGIDBODY OBJECT (MASS, COLLIDER)
    // GRAPHICAL STUFF
    // DIRECTION THE BLOCK FACES
    protected int maxHealth = 100;
    protected int health = maxHealth;
    protected float dropRateInPercent = 2;
    protected BasicShip ship;
    protected String moduleName;
    protected ColorRGBA color = ColorRGBA.Gray;
    protected Spatial b;
    protected Body body;
    protected Spatial spatial;

    public BasicModule() {    
       
        
    }
    
    public void lockToShip() {               
//        Vector3f cockpitPos = new Vector3f(
//            (float)ship.cockpit.body.getWorldPoint(ship.cockpit.body.getLocalCenter()).x, 
//            0.0f, 
//            (float)ship.cockpit.body.getWorldPoint(ship.cockpit.body.getLocalCenter()).y);
        WeldJointDef wjDef = new WeldJointDef();        
        //wjDef.bodyA = ship.cockpit.body;
        //wjDef.bodyB = this.body;
        wjDef.initialize(ship.cockpit.body, this.body, ship.cockpit.body.getPosition());
        wjDef.collideConnected = false;
        PhysicsWorld.world.createJoint(wjDef);
        
        //WeldJoint wj = new WeldJoint(ship.cockpit.body, this.body, new Vector2(cockpitPos.x, cockpitPos.z));
    }

    public String getModuleName() {
        return moduleName;
    }

    public void update(float tpf) {
         Vector3f bodyPos = new Vector3f(
                (float)body.getWorldPoint(body.getLocalCenter()).x, 
                0.0f, 
                (float)body.getWorldPoint(body.getLocalCenter()).y);

         spatial.setLocalTranslation(bodyPos);
         
         float angleRad = body.getAngle();
         Quaternion q = new Quaternion();
         q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
         spatial.setLocalRotation(q);
         
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
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

        Box b = new Box(1, 1, 1);
        spatial = new Geometry("Box", b);
        Material mat = new Material(ship.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        spatial.setMaterial(mat);


        //DONT KNOW WHY X and Y have to be this way, but now it looks like in the array
        int x = ship.getPositionInGrid(this).y * -2;
        int y = ship.getPositionInGrid(this).x * -2;
        //this.move(, x, y);
        
        generatePhysicsBody(x, y);
        
        ship.attachChild(this);
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
}
