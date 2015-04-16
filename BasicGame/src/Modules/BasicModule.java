/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import mygame.BasicShip;

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

    public BasicModule() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void update() {
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
        this.move(ship.getPositionInGrid(this).y * 2, ship.getPositionInGrid(this).x * -2, 0);

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
