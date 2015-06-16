/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class ShipModule {
    
    GraphicalModule gm;
    protected WJSFClient app;
    protected ClientShip ship;
    protected Node shipRoot;
    protected OrientedModule orientedModule;
    
    public ShipModule(OrientedModule orientedModule, float x, float y, Node shipRoot, ClientShip ship, WJSFClient app) {
        this.app = app;
        this.shipRoot = shipRoot;
        this.ship = ship;
        this.orientedModule = orientedModule;
        this.gm = ModuleCreator.createOrientedGraphicalModule(orientedModule, shipRoot, x, y, app);
        
        shipRoot.attachChild(gm);
    }
    
    public void activate() {
        gm.activate();
    }
    
    public void deactivate() {
        gm.deactivate();
    }
    
    public boolean isActive() {
        return gm.isActive();
    }
    
    public void update() {       
        gm.update();
    }
    
    public void remove() {
        gm.remove();
    }
}
