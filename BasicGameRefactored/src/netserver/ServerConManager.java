package netserver;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

import netmsg.NetMessages.*;

public class ServerConManager implements ConnectionListener {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	private WJSFServer app;
	public ConcurrentLinkedQueue<NetPlayer> players;
	
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public ServerConManager(WJSFServer app) {
		// init stuff
		this.app = app;
		this.players = new ConcurrentLinkedQueue<NetPlayer>();
		
		// setup listener
		this.app.getServer().addConnectionListener(this);
	}
	
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	@Override
	public void connectionAdded(Server arg0, HostedConnection arg1) {
		// create new netplayer
		this.players.add(new NetPlayer(this.app, arg1));
		Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg1.toString());
		
		// TODO: load player stuff
		
		// send player ship data
    	int[][] ship =
			{
				{0, 0, 4, 0, 0},
				{0, 2, 3, 2, 0},
				{7, 2, 1, 2, 7},
				{0, 5, 3, 5, 0},
				{0, 0, 5, 0, 0}				
			};
		
		this.app.getServer().broadcast(new ClientEnteredMsg("PlayerName", arg1.getId(), ship, new Vector3f(this.app.rnd.nextFloat() * 5f, 0, this.app.rnd.nextFloat() * 5f), Vector3f.ZERO));
		
		// send the new player all other ships
		
	}

	@Override
	public void connectionRemoved(Server arg0, HostedConnection arg1) {
		for(NetPlayer pl : this.players) {
			if(pl.con == arg1) {
				this.players.remove(pl);
				Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg1.toString());
			}
		}
	}

	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}