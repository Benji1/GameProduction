/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import config.ConfigReader;
import org.jbox2d.common.Vec2;
import weapons.LaserProjectile;

/**
 *
 * @author Eva
 */
public class LaserGun extends Weapon {
    
    public LaserGun(int orientation) {
        super(orientation);
        moduleName = "LaserGun";
        fireRate = ConfigReader.get("LaserGun_Firerate", float.class);
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(orientation);
        LaserProjectile p = new LaserProjectile(body.getPosition().add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
    }
    
}
