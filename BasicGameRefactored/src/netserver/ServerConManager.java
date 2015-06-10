package netserver;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

import netutil.NetMessages.*;

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
		NetPlayer newPl = new NetPlayer(this.app, arg1);
		
		// TODO: load player stuff
		
		// send player ship data
    	ClientEnteredMsg msg = new ClientEnteredMsg("PlayerName", arg1.getId(), newPl.ship, newPl.pos, Vector3f.ZERO);
    	msg.setReliable(true);
		this.app.getServer().broadcast(msg);
		
		// send all other ships to the new player
		for(NetPlayer pl : this.players) {
			ClientEnteredMsg syncPl = new ClientEnteredMsg("PlayerName", pl.con.getId(), pl.ship, pl.pos, Vector3f.ZERO);
			syncPl.setReliable(true);
			this.app.getServer().broadcast(Filters.in(arg1), syncPl);
		}
		
		// add new player to list
		this.players.add(newPl);
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