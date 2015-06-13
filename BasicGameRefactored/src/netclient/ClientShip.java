package netclient;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import netclient.gui.OrientedModule;

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
        public GraphicalModule[][] gmodules;
	public WJSFClient app;
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public ClientShip(String name, int id, OrientedModule[][] ship, WJSFClient app) {
                this.app = app;
		this.name = name;
		this.id = id;                
		
		this.shipRoot = new Node("ShipNode " + name);
		this.modules = ship;
                this.gmodules = new GraphicalModule[ship.length][ship[0].length];
                
                // TODO: get real velocity, atm needed for camera movement
                this.velocity = new Vector3f(0, 0, 0);

		// TODO: build ship
                
                for (int i=0; i<ship.length; i++) {
                    for (int j=0; j<ship[0].length; j++) {
                        if(ship[i][j] != null) {
                            // j = x, i = y
                            gmodules[i][j] = createOrientedModuleGraphics(ship[i][j], j, i);
                        }
                    }
                }               
	}
        
        public OrientedModule[][] getModules() {
            return modules;
        }
        
        public void setModules(OrientedModule[][] modules) {
            this.modules = modules;
        }
        
        public GraphicalModule createOrientedModuleGraphics(OrientedModule om, float x, float y) {
            return new GraphicalModule(om, shipRoot, x, y, app);
        }
}
