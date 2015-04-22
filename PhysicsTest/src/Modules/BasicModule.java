/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import mygame.BasicShip;
import mygame.PhysicsWorld;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

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
    
    protected Body body;

    public BasicModule() {                
        Rectangle rect = new Rectangle(1, 1);
        // set fixture
        BodyFixture f = new BodyFixture(rect);
        f.setDensity(1);
        f.setFriction(0.6);

        // set body
        body = new Body();
        body.addFixture(f);
        body.setLinearDamping(0.3);
                
        PhysicsWorld.world.addBody(body);
    }
    
    public void lockToShip() {               
        Vector3f cockpitPos = new Vector3f(
            (float)ship.cockpit.body.getWorldPoint(ship.cockpit.body.getLocalCenter()).x, 
            0.0f, 
            (float)ship.cockpit.body.getWorldPoint(ship.cockpit.body.getLocalCenter()).y);
        
        WeldJoint wj = new WeldJoint(ship.cockpit.body, this.body, new Vector2(cockpitPos.x, cockpitPos.z));
        PhysicsWorld.world.addJoint(wj);        
    }

    public String getModuleName() {
        return moduleName;
    }

    public void update(float tpf) {
         Vector3f bodyPos = new Vector3f(
                (float)body.getWorldPoint(body.getLocalCenter()).x, 
                0.0f, 
                (float)body.getWorldPoint(body.getLocalCenter()).y);
         // TODO place 3d model accordingly         
         //ship.setLocalTranslation(bodyPos);
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
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(ship.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);


        //DONT KNOW WHY X and Y have to be this way, but now it looks like in the array
        this.move(ship.getPositionInGrid(this).y * -2, 0, ship.getPositionInGrid(this).x * -2);

        ship.attachChild(this);
        this.attachChild(geom);
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
