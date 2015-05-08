/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import services.config.ConfigReader;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import services.ServiceManager;
import weapons.LaserProjectile;

/**
 *
 * @author Eva
 */
public class LaserGun extends Weapon {

    public LaserGun(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys, orientationDirection);
        moduleName = "LaserGun";
        
        ConfigReader cr = ServiceManager.getConfigReader();
        
        fireRate = cr.getFromMap(cr.getBaseMap("LaserGun"), "Firerate", float.class);
        energyConsumptionPerAction = cr.getFromMap(cr.getBaseMap("LaserGun"), "EnergyConsumptionPerAction", float.class);
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(orientation);
        LaserProjectile p = new LaserProjectile(body.getPosition().add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
    }
}
