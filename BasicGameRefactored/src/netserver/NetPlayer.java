package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.NetInput.InputTypes;

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
	public NetInput input;
	public BasicShip ship;
	
	public OrientedModule[][] shipArray;

	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public NetPlayer(WJSFServer app, HostedConnection con) {            
            this.app = app;
            this.con = con;
            this.ship = this.app.designs.createTestShip1();
            this.input = new NetInput(this);
            this.shipArray = ship.getOrientedModuleArray();
	}
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	public void update(float tpf) {
		
	}
	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}