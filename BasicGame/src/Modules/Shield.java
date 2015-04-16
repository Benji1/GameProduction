/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author 1337
 */
public class Shield extends InteractiveModule {

    protected float maxShieldPower = 700;
    protected float shieldPower = maxShieldPower;
    protected float shieldRegenRate = 40;
    
    public Shield() {
        moduleName = "Shield";
        energyConsumptionPerSecond = 50;
        color = ColorRGBA.Blue;
    }
    
    @Override
    public void update() {
        super.update();
        if(energyAvailableInPercent >= 100) {
            shieldPower = fillNotOverLimit(shieldPower, shieldRegenRate, maxShieldPower);
        }
    }
}
