package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.HostedConnection;

import mygame.BasicShip;

public class NetPlayer {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	
	private WJSFServer app;
	
	public HostedConnection con;
	public BasicShip ship;
	
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public NetPlayer(WJSFServer app, HostedConnection con) {
		this.app = app;
		this.con = con;

		//this.ship = BasicShip.
	}
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}