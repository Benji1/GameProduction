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
public class Cockpit extends BasicModule {

    public Cockpit() {
        moduleName = "Cockpit";
        color = ColorRGBA.White;
    }
    
    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
        ship.cockpit = this;
    }
    
    @Override
    public void onRemove() {
        super.onRemove();
        ship.cockpit = null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void destroy() {
        super.destroy();
        ship.disable();
    }
}
