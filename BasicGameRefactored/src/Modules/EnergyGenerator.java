/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import static Modules.BasicModule.fillNotOverLimit;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {
    
    private float energyGeneratedPerSecond = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "EnergyGeneratedPerSecond", float.class);
    private float energyStorageLimit = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "EnergyStorageLimit", float.class);
    private float energyStorage = energyStorageLimit;
    private int radius = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "Radius", int.class);
    
    public EnergyGenerator() {
        moduleName = "E-Gen";
        color = ColorRGBA.Yellow;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        regenerateEnergy(delta);
    }

    public void regenerateEnergy(float delta) {
        energyStorage = fillNotOverLimit(energyStorage, energyGeneratedPerSecond * delta, energyStorageLimit);

        ColorRGBA c = new ColorRGBA();
        c.interpolate(ColorRGBA.DarkGray, color, energyStorage / energyStorageLimit);
    
        material.setColor("Ambient", c);
        material.setColor("Diffuse", c);
    }
    
    public float getEnergy() {
        return energyStorage;
    }
    
    public void reduceEnergy(float amount) {
        energyStorage -= amount;
    }
    
    public int getRadius() {
        return radius;
    }
}
