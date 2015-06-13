/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.services.editor;

import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

import netserver.services.Service;
import netutil.NetMessages.ShipChangedMsg;

public class EditorManager extends Service {
    
    private ClientShip ship;
    private WJSFClient client;
    
    public EditorManager() {
    }
    
    public void notifyOfShipChange(OrientedModule[][] modules) {
        ShipChangedMsg msg = new ShipChangedMsg(ship.id, modules);
        msg.setReliable(true);
        client.client.send(msg);
    }
    
    public void setShip(ClientShip ship) {
        this.ship = ship;
    }
    public void setClient(WJSFClient client) {
        this.client = client;
    }
    
    public OrientedModule[][] getShipModules() {
        return ship.getModules();
    }
}
