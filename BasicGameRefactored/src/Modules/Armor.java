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
public class Armor extends BasicModule {

    public Armor() {
        super();
        moduleName = "Armor";
        color = ColorRGBA.Gray;
        
        maxHealth = cr.getFromMap(cr.getBaseMap("Armor"), "MaxHealth", int.class);
        health = maxHealth;
    }
}
