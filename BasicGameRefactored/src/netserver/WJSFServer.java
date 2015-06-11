package netserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbox2d.dynamics.Body;

import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.services.updater.UpdateableManager;
import netserver.shipdesigns.TestShipDesigns;
import netserver.universe.Universe;
import netserver.universe.UniverseGenerator;
import netutil.NetMessages;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;


public class WJSFServer extends SimpleApplication {

	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	// Networking
    private Server server;
    private ServerConManager conManager;
    private ServerNetMsgListener msgListener;
    
    public Random rnd;

    
    
    public TestShipDesigns designs;
    protected float shipSpeed = 0;
    protected float shipRotation = 1.5f;
    protected int rotDir = 0;
    protected float maxSpeed = 5f;
    private Universe u;
    
    //public ArrayList<BasicShip> ships = new ArrayList<BasicShip>();
    //public BasicShip playersShip;
    //public BasicShip targetShip;
	
    UpdateableManager updateableManager = ServiceManager.getUpdateableManager();
    
    public ArrayList<Body> bodiesToRemove = new ArrayList<Body>();
    
    //boolean up = false, down = false;
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public static void main(String[] args) {
        NetMessages.initSerializables();

        WJSFServer app = new WJSFServer();
        
        app.start(JmeContext.Type.Display); // headless type for servers!
    }

    @Override
    public void simpleInitApp() {
    	// jme settings
    	this.flyCam.setEnabled(false);
    	this.pauseOnFocus = false;
    	
    	// start server
        try {
            server = Network.createServer(NetMessages.PORT);
            server.start();
        } catch (IOException e) {
            Logger.getLogger(WJSFServer.class.getName()).log(Level.SEVERE, null, e);
            return;
        }
        
        // init member
        this.rnd = new Random();
        this.conManager = new ServerConManager(this);
        this.msgListener = new ServerNetMsgListener(this);
        this.server.addMessageListener(this.msgListener);
        
        // init game
        this.initWorld();
    }

    private void initWorld() {
        this.u = new Universe(this);
        UniverseGenerator.debugSystem(this, u);
    }
    
    @Override
    public void destroy() {
        
    	// properly close all connections
        server.close();
        super.destroy();
    }

    @Override
    public void simpleUpdate(float tpf) {
    	//
    	// UPDATE GAME
    	//
    	
    	phyicsUpdate(tpf);
    	
    	updateableManager.update(tpf);
        
        for(Body b: bodiesToRemove)
            PhysicsWorld.world.destroyBody(b);

        bodiesToRemove.clear();
        
        this.u.update(tpf);
        
        //
        // UPDATE NET
        //
        
        this.conManager.update(tpf);
    }
    
    public void phyicsUpdate(float delta) {
        PhysicsWorld.world.step(delta, 8, 8);
    }
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Server getServer() {return this.server;}
    public ServerConManager getConManager() {return this.conManager;}
    public ServerNetMsgListener getMsgListener() {return this.msgListener;}
    
    public Universe getUniverse() {
        return this.u;
    }
}