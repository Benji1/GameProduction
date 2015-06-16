package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import netserver.modules.FacingDirection;

public class NetPlayer {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	private WJSFServer app;
	
	public HostedConnection con;
        
	public BasicShip ship;
        private Inventory inventory;

	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public NetPlayer(WJSFServer app, HostedConnection con) {           
            this.inventory = new Inventory(ship);
            this.app = app;
            this.con = con;
            this.ship = this.app.designs.createPlayerShip(this);            
	}
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	public void update(float tpf) {
		
	}
        
        public void handleKeyEvent(Integer keyCode, boolean keyPressed) {
            if (keyPressed) {
                ship.handleKeyPressed(keyCode);
            } else {
                ship.handleKeyReleased(keyCode);
            }
        }
        
        public void updateBaseInventory(ModuleType[] itemsInBase) {
            inventory.setItemsInBase(itemsInBase);
        }
	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
        public void setShip(BasicShip ship) {
            this.ship = ship;
        }
        
        public Inventory getInventory() {
            return inventory;
        }
        public BasicShip getShip() {
            return ship;
        }
}