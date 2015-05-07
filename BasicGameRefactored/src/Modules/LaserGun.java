/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import config.ConfigReader;
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
        fireRate = ConfigReader.getFromMap(ConfigReader.getBaseMap("Weapon"), "LaserGun_Firerate", float.class);
        energyConsumptionPerAction = 50;
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(orientation);
        LaserProjectile p = new LaserProjectile(body.getPosition().add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
    }
}
