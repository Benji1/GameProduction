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
public class Cockpit extends BasicModule {

    public Cockpit() {
        moduleName = "Cockpit";
        color = ColorRGBA.White;
    }   
    
    public void addToShip() {
        ship.setPhysicsCenter(this.body);        
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        //this.ship.setLocalTranslation(this.getLocalTranslation());
    }
    
    @Override
    public void destroy() {
        super.destroy();        
        ship.disable();
    }
}
