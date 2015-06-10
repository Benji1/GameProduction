/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import gui.ModuleType;
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
    private int keyPressedCounter = 0;

    public LaserGun(ArrayList<Integer> keyCodes, FacingDirection orientationDirection) {
        super(keyCodes, orientationDirection);
        moduleName = "LaserGun";

        fireRate = cr.getFromMap(cr.getBaseMap("LaserGun"), "Firerate", float.class);
        energyConsumptionPerAction = cr.getFromMap(cr.getBaseMap("LaserGun"), "EnergyConsumptionPerAction", float.class);
        type = ModuleType.WEAPON;
    }

    @Override
    protected void fire() {
        Vec2 fireDirection = body.getWorldVector(FacingDirection.getDirectionVector(orientation).mul(-1));
        
        LaserProjectile p = new LaserProjectile(body.getWorldPoint(body.getLocalCenter()).add(new Vec2(2f * fireDirection.x, 2f * fireDirection.y)), fireDirection, ship.getApp());
        fire_sound.setPitch((float) Math.random() * 0.1f + 0.95f);
        fire_sound.playInstance();
        
    }
    
    @Override
    public void onPlaced (BasicShip ship) {
        super.onPlaced(ship);
        fire_sound = new AudioNode(ship.getApp().getAssetManager(), "Sound/Effects/lasershot2.ogg", false);
        fire_sound.setPositional(false);
        fire_sound.setLooping(false);
        fire_sound.setVolume(1);
        this.attachChild(fire_sound);
    }
    
    @Override
    public void update(float delta) {
    	super.update(delta);
    }
    
    @Override
    protected void create3DBody() {
        super.create3DBody();
        AssetManager a = ship.getApp().getAssetManager();
        spatial = a.loadModel("3dmodels/gun.obj");
        material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = a.loadTexture("3dmodels/gun_ao.png");
        material.setTexture("DiffuseMap", t);
        spatial.setMaterial(material);

        materialActive.setTexture("DiffuseMap", t);
    }

    @Override
    public void handleKeyPressed(Integer keyCode) {
        if (keyCodes.contains(keyCode)) {
            keyPressedCounter++;     
            
            if (keyPressedCounter > 0 && !isActive()) {
                activate();
            }
        }        
    }

    @Override
    public void handleKeyReleased(Integer keyCode) {
        if (keyCodes.contains(keyCode)) {
            keyPressedCounter--;
            
            if (keyPressedCounter <= 0) {
                deactivate();
            }
        }        
    }
}
