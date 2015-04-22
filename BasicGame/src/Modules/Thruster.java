/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import config.ConfigReader;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float thrusterMaxAcceleration;
    protected float thrust;

    public Thruster() {
        moduleName = "Thruster";
        energyConsumptionPerSecond = 10;
        color = ColorRGBA.Orange;
        thrusterMaxAcceleration = ConfigReader.get("ThrusterMaxAcceleration", Float.class);
    }

    protected void onActive() {
    }
}
