/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.audio.AudioNode;
import java.util.ArrayList;
import mygame.BasicShip;
import org.jbox2d.common.Vec2;
import weapons.LaserProjectile;

/**
 *
 * @author Eva
 */
public class LaserGun extends Weapon {
    
    private AudioNode fire_sound;

    public LaserGun(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys, orientationDirection);
        moduleName = "LaserGun";

        fireRate = cr.getFromMap(cr.getBaseMap("LaserGun"), "Firerate", float.class);
        energyConsumptionPerAction = cr.getFromMap(cr.getBaseMap("LaserGun"), "EnergyConsumptionPerAction", float.class);
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(orientation);
        LaserProjectile p = new LaserProjectile(body.getWorldPoint(body.getLocalCenter()).add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
        fire_sound.playInstance();
    }
    
    @Override
    public void onPlaced (BasicShip ship) {
        super.onPlaced(ship);
        fire_sound = new AudioNode(ship.getApp().getAssetManager(), "Sound/Effects/LaserProjectile.ogg", false);
        fire_sound.setPositional(false);
        fire_sound.setLooping(false);
        fire_sound.setVolume(1);
        this.attachChild(fire_sound);
    }
    
    @Override
    public void update(float delta) {
    	super.update(delta);
    }
}
