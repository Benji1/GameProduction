/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import services.ServiceManager;
import services.config.ConfigReader;

/**
 *
 * @author 1337
 */
public class Armor extends BasicModule {

    public Armor() {
        moduleName = "Armor";
        color = ColorRGBA.Gray;
        
        ConfigReader cr = ServiceManager.getConfigReader();
        maxHealth = cr.getFromMap(cr.getBaseMap("Armor"), "MaxHealth", int.class);
        
        
        health = maxHealth;
    }
}
