package netclient;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import netserver.services.ServiceManager;

public class ClientShip {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	// client stuff
	public String name;
	public int id;
	
	// ship stuff
	public Node shipRoot;
	public Vector3f velocity;
        public OrientedModule[][] modules;
	
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public ClientShip(String name, int id, OrientedModule[][] ship) {
            System.out.println("here");
		this.name = name;
		this.id = id;                
		
		this.shipRoot = new Node("ShipNode " + name);
		this.modules = ship;
                
                // TODO: get real velocity, atm needed for camera movement
                this.velocity = new Vector3f(0, 0, 0);

		// TODO: build ship
                
                for (int i=0; i<ship.length; i++) {
                    for (int j=0; j<ship[0].length; j++) {
                        //System.out.print(ship[i][j]);
                    }
                    //System.out.println("");
                }               
	}
        
        public OrientedModule[][] getModules() {
            return modules;
        }
}
