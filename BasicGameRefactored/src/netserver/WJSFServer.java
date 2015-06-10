package netserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import mygame.BasicShip;
import netmsg.NetMessages;
import netmsg.NetMessages.*;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Geometry;
import com.jme3.system.JmeContext;


public class WJSFServer extends SimpleApplication {

	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
    private Server server;
    private ServerConManager conManager;
    
    public Random rnd;

    
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
    }

    @Override
    public void destroy() {
        
    	// properly close all connections
        server.close();
        super.destroy();
    }

    @Override
    public void simpleUpdate(float tpf) {
    	/*this.counter += tpf;
    	
    	if(this.counter >= 0.5f) {
    		Vector3f newPos = new Vector3f(rnd.nextFloat() * 3, rnd.nextFloat() * 3, rnd.nextFloat() * 3);
    		server.broadcast(new PosMsg(newPos));
    		geom.setLocalTranslation(newPos);
    		this.counter = 0;
    	}
    	
        server.broadcast(new NetMsg("Hello World!!!" + tpf));*/
    }
    
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Server getServer() {return this.server;}

}