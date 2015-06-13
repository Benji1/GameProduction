package netclient;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

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
	
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public ClientShip(String name, int id, int[][] ship) {
		this.name = name;
		this.id = id;
		
		this.shipRoot = new Node("ShipNode " + name);
		
                
                
                
                // TODO: get real velocity, atm needed for camera movement
                this.velocity = new Vector3f(0, 0, 0);
                
                
		// TODO: build ship
	}
}
