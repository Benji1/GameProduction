package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;

import mygame.BasicShip;

public class NetPlayer {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	private WJSFServer app;
	
	public HostedConnection con;
	//public BasicShip ship;
	
	public Vector3f pos;
	
	public int[][] ship =
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

		//this.ship = BasicShip.
		this.pos = new Vector3f(this.app.rnd.nextFloat() * 20f, 0, this.app.rnd.nextFloat() * 20f);
	}
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}