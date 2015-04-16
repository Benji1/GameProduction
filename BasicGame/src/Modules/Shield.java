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
public class Shield extends InteractiveModule {

    public Shield() {
        name = "Shield";
        energyConsumptionPerSecond = 50;
        color = ColorRGBA.Blue;
    }
}
