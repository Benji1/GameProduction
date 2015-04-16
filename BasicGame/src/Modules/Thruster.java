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
public class Thruster extends InteractiveModule {

    protected float maxThrust;
    protected float thrust;
    
    
    public Thruster() {
        moduleName = "Thruster";
        energyConsumptionPerSecond = 10;
        color = ColorRGBA.Orange;
    }
}
