/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.editor;

import java.util.ArrayList;
import java.util.Stack;
import netclient.ClientShip;
import netclient.gui.OrientedModule;

import netserver.modules.BasicModule;
import netserver.services.Service;

public class EditorManager extends Service {
    
    private ClientShip ship;
    
    public EditorManager() {
    }
    
    public void notifyOfShipChange(BasicModule[][] modules) {
        // TODO
    }
    
    public void setShip(ClientShip ship) {
        this.ship = ship;
    }
    
    public OrientedModule[][] getShipModules() {
        return ship.getModules();
    }
}
