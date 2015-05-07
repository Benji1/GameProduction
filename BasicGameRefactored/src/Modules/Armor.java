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
public class Armor extends BasicModule {

    public Armor() {
        moduleName = "Armor";
        color = ColorRGBA.Gray;

        maxHealth = ConfigReader.getFromMap(ConfigReader.getBaseMap("Armor"), "MaxHealth", int.class);
        
        
        health = maxHealth;
    }
}
