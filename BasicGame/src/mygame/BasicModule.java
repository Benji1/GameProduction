/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author 1337
 */
public abstract class BasicModule {

    // RIGIDBODY OBJECT (MASS, COLLIDER)
    // GRAPHICAL STUFF
    protected int maxHealth;
    protected int health;
    protected float dropRateInPercent;
    protected BasicShip ship;
    protected String name;
    
    public BasicModule (BasicShip ship) {
        this.ship = ship;
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

    public void onPlaced(int x, int y) {
    }

    public void onRemoved() {
    }
}
