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
            this.shipArray = new OrientedModule[][]{
                {null, null, new OrientedModule(ModuleType.WEAPON, FacingDirection.FORWARD), null, null}, 
                {null, new OrientedModule(ModuleType.ARMOR, FacingDirection.FORWARD), new OrientedModule(ModuleType.ENERGY_GENERATOR, FacingDirection.FORWARD), new OrientedModule(ModuleType.ARMOR, FacingDirection.FORWARD), null}, 
                {new OrientedModule(ModuleType.SHIELD, FacingDirection.FORWARD), new OrientedModule(ModuleType.ARMOR, FacingDirection.FORWARD), new OrientedModule(ModuleType.COCKPIT, FacingDirection.FORWARD), new OrientedModule(ModuleType.ARMOR, FacingDirection.FORWARD), new OrientedModule(ModuleType.SHIELD, FacingDirection.FORWARD)}, 
                {null, new OrientedModule(ModuleType.THRUSTER, FacingDirection.FORWARD), new OrientedModule(ModuleType.ENERGY_GENERATOR, FacingDirection.FORWARD), new OrientedModule(ModuleType.THRUSTER, FacingDirection.FORWARD), null}, 
                {null, null, new OrientedModule(ModuleType.THRUSTER, FacingDirection.FORWARD), null, null}};
            
            this.app = app;
            this.con = con;
            this.ship = this.app.designs.createTestShip1();
            this.input = new NetInput(this);
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