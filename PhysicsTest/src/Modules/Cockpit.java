/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Cockpit extends BasicModule {

    public Cockpit() {
        moduleName = "Cockpit";
        color = ColorRGBA.White;
    }   
    
    public void addToShip() {
        ship.cockpit = this;        
    }
    
    public void update(float delta)
    {
        super.update(delta);
    }
}
