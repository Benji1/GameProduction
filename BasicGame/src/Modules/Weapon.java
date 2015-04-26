/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public abstract class Weapon extends InteractiveModule {
    
    protected float fireRate;       // time between shots
    protected float fireRateTimer;
    protected Vec2 orientation;

    public Weapon(int orientation) {
        color = ColorRGBA.Brown;
        colorActive = ColorRGBA.Red;
        fireRateTimer = 0;
        
        switch(orientation) {
            case 0: this.orientation = new Vec2(0, -1); break;
            case 1: this.orientation = new Vec2(1, 0); break;
            case 2: this.orientation = new Vec2(0, 1); break;
            case 3: this.orientation = new Vec2(-1, 0); break;
            default: this.orientation = new Vec2(0, -1);
        }
    }

    protected void onActive() {
        if (fireRateTimer >= fireRate) {
            fire();
            fireRateTimer = 0;
        }        
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (fireRateTimer < fireRate) {
            fireRateTimer = Math.min(fireRateTimer+delta, fireRate);
        }
    }
    
    protected abstract void fire();
}
