/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    public Thruster() {
        name = "Thruster";
        energyConsumptionPerSecond = 10;
        color = ColorRGBA.Orange;
    }
}
