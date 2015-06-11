package netserver;

import java.util.concurrent.Callable;
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
	
	private final float posUpdate = 0.1f;
	private float curPosUpdate = 0;
	
	
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void connectionAdded(Server arg0, HostedConnection arg1) {
		this.app.enqueue(new Callable() {
			public Object call() throws Exception {
				// create new netplayer
				NetPlayer newPl = new NetPlayer(app, arg1);
				
				// TODO: load player stuff
				
				// send player ship data
		    	ClientEnteredMsg msg = new ClientEnteredMsg("PlayerName", arg1.getId(), newPl.shipArray, new Vector3f(app.rnd.nextFloat() * 20f, 0, app.rnd.nextFloat() * 20f), Vector3f.ZERO);
		    	msg.setReliable(true);
				app.getServer().broadcast(msg);
				
				// send all other ships to the new player
				for(NetPlayer pl : players) {
					ClientEnteredMsg syncPl = new ClientEnteredMsg("PlayerName", pl.con.getId(), pl.shipArray, new Vector3f(app.rnd.nextFloat() * 20f, 0, app.rnd.nextFloat() * 20f), Vector3f.ZERO);
					syncPl.setReliable(true);
					app.getServer().broadcast(Filters.in(arg1), syncPl);
				}
				
				// add new player to list
				players.add(newPl);
				
				return null;
			}
		});
	}

	@Override
	public void connectionRemoved(Server arg0, HostedConnection arg1) {
		for(NetPlayer pl : this.players) {
			if(pl.con == arg1) {
				this.app.getRootNode().getChildren().remove(pl.ship);
				this.players.remove(pl);
				Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg1.toString());
			}
		}
	}
	
	public void update(float tpf) {
		// update player
		for(NetPlayer pl : this.players)
			pl.update(tpf);
		
		// pos update
		this.curPosUpdate += tpf;
		if(this.curPosUpdate >= this.posUpdate) {
			for(NetPlayer pl : this.players) {
				this.app.getServer().broadcast(new PosMsg(pl.ship.cockpit.getLocalTranslation(), pl.con.getId()));
			}
			
			this.curPosUpdate = 0;
		}
	}
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}