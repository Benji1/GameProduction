/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import org.dyn4j.geometry.Vector2;

/**
 *
 * @author 1337
 */
public class Weapon extends InteractiveModule {

    public Weapon() {
        moduleName = "Weapon";
        color = ColorRGBA.Red;
    }

    protected void onActive() {
    }
}