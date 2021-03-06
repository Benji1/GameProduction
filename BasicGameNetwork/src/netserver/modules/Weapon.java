/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;

/**
 *
 * @author 1337
 */
public abstract class Weapon extends InteractiveModule {

    protected float fireRate;       // time between shots
    protected float fireRateTimer;

    public Weapon(ArrayList<Integer> keyCodes, FacingDirection orientationDirection) {
        super(keyCodes);
        color = ColorRGBA.Brown;
        colorActive = ColorRGBA.Red;
        fireRateTimer = 0;

        orientation = orientationDirection;
    }

    protected void onActive() {
        if (fireRateTimer >= fireRate && hasEnoughEnergyForAction()) {
            fire();
            fireRateTimer = 0;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (fireRateTimer < fireRate) {
            fireRateTimer = Math.min(fireRateTimer + delta, fireRate);
        }
    }

    protected abstract void fire();
}
