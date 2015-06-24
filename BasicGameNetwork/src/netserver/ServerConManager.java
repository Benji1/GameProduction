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
import netserver.items.Item;
import netserver.services.ServiceManager;
import netserver.services.updater.INetworkPosAndRotUpdateable;
import netserver.universe.SpaceStation;
import netutil.NetMessages;
import netutil.NetMessages.ClientEnteredMsg;
import netutil.NetMessages.GraphicObjPosAndRotMsg;
import netutil.NetMessages.NearStationMsg;
import netutil.NetMessages.PosAndRotMsg;
import netutil.NetMessages.SpawnItemMsg;
import netutil.NetMessages.SpawnSpaceStationMsg;

import org.jbox2d.common.Vec2;

public class ServerConManager implements ConnectionListener {
    
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    
    private WJSFServer app;
    public ConcurrentLinkedQueue<NetPlayer> players;
    
    private final float posAndRotUpdate = 0.1f;
    private float curPosAndRotUpdate = 0;
    
    
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
    public void connectionAdded(Server arg0, final HostedConnection arg1) {
        this.app.enqueue(new Callable() {
            public Object call() throws Exception {
                // create new netplayer
                NetPlayer newPl = new NetPlayer(app, arg1);
                
                // TODO: load player stuff
                
                // send player ship data
                ClientEnteredMsg msg = new ClientEnteredMsg("PlayerName", arg1.getId(), newPl.ship.getOrientedModuleArray(), newPl.getInventory().getModulesInBase(), new Vector3f(app.rnd.nextFloat() * 20f, 0, app.rnd.nextFloat() * 20f), Vector3f.ZERO);
                msg.setReliable(true);
                app.getServer().broadcast(msg);
                
                // send all other ships to the new player
                for(NetPlayer pl : players) {
                    ClientEnteredMsg syncPl = new ClientEnteredMsg("PlayerName", pl.con.getId(), pl.ship.getOrientedModuleArray(), pl.getInventory().getModulesInBase(), new Vector3f(app.rnd.nextFloat() * 20f, 0, app.rnd.nextFloat() * 20f), Vector3f.ZERO);
                    syncPl.setReliable(true);
                    app.getServer().broadcast(Filters.in(arg1), syncPl);
                }
                
                // send all space stations to the new player
                for (SpaceStation station : app.getUniverse().getAllNearbyStations(newPl.ship.getLocalTranslation())) {
                    SpawnSpaceStationMsg syncStation = new SpawnSpaceStationMsg(station.getId(), station.getLocalTranslation());
                    syncStation.setReliable(true);
                    app.getServer().broadcast(Filters.in(arg1), syncStation);                               
                }
                
                // send all floating items to the new player
                for (Item i : app.floatingItems) {
                    SpawnItemMsg syncItem = new SpawnItemMsg(i.getId(), i.get2DTranslation(), i.getRotation(), i.getOrientedModule());
                    syncItem.setReliable(true);
                    app.getServer().broadcast(Filters.in(arg1), syncItem);
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
    
    public void kickPlayer(int id) {
        for (NetPlayer pl : this.players) {
            if (pl.con.getId() == id) {
                pl.con.close(NetMessages.PLAYER_DIED_MSG);
            }
        }
    }
    
    public void update(float tpf) {
        // update player
        for(NetPlayer pl : this.players)
            pl.update(tpf);
        
        // pos update
        this.curPosAndRotUpdate += tpf;
        if(this.curPosAndRotUpdate >= this.posAndRotUpdate) {
            for(NetPlayer pl : this.players) {
                if (pl.ship.cockpit != null) {
                    this.app.getServer().broadcast(new PosAndRotMsg(pl.ship.cockpit.getLocalTranslation(), pl.ship.cockpit.getLocalRotation(), pl.ship.cockpit.getVelocity(), pl.ship.cockpit.getAngVelocity(), pl.con.getId()));
                
                    // Send also with position??
                    boolean nearby = this.app.getUniverse().nearStation(pl.getShip().cockpit.getWorldTranslation());
                    this.app.getServer().broadcast(new NearStationMsg(nearby, pl.con.getId()));
                }
            }
            
            for (INetworkPosAndRotUpdateable u : ServiceManager.getUpdateableManager().getNetorkUpdateables()) {
                GraphicObjPosAndRotMsg msg = new GraphicObjPosAndRotMsg(u.get3DTranslation(), u.getRotation(), u.getVelocity(), u.getAngVelocity(), u.getId());
                app.getServer().broadcast(msg);
            }
            
            this.curPosAndRotUpdate = 0;
        }
    }
    
    private class physicsCenter {
        
        public physicsCenter() {
        }
    }
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}