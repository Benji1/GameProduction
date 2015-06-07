/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import gui.ModuleType;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import weapons.LaserProjectile;

/**
 *
 * @author Eva
 */
public class LaserGun extends Weapon {

    public LaserGun(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys, orientationDirection);
        moduleName = "LaserGun";

        fireRate = cr.getFromMap(cr.getBaseMap("LaserGun"), "Firerate", float.class);
        energyConsumptionPerAction = cr.getFromMap(cr.getBaseMap("LaserGun"), "EnergyConsumptionPerAction", float.class);
        type = ModuleType.WEAPON;
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(FacingDirection.getDirectionVector(orientation).mul(-1));
        LaserProjectile p = new LaserProjectile(body.getPosition().add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
    }
}
