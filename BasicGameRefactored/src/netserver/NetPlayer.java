package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.NetInput.InputTypes;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;

public class NetPlayer {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	private WJSFServer app;
	
	public HostedConnection con;
	public NetInput input;
	public BasicShip ship;
	
	public int[][] shipArray =
		{
			{0, 0, 4, 0, 0},
			{0, 2, 3, 2, 0},
			{7, 2, 1, 2, 7},
			{0, 5, 3, 5, 0},
			{0, 0, 5, 0, 0}
		};

	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public NetPlayer(WJSFServer app, HostedConnection con) {
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