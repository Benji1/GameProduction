/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public abstract class Weapon extends InteractiveModule {

    protected float fireRate;       // time between shots
    protected float fireRateTimer;
    protected Vec2 orientation;

    public Weapon(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys);
        color = ColorRGBA.Brown;
        colorActive = ColorRGBA.Red;
        fireRateTimer = 0;

        orientation = FacingDirection.getDirectionVector(orientationDirection);
    }

    protected void onActive() {
        if (fireRateTimer >= fireRate && energyAvailableInPercent >= 100) {
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
