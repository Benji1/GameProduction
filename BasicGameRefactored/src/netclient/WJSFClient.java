package netclient;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.WJSFServer;
import netutil.NetMessages;
import netutil.NetMessages.*;
import netclient.gui.GUI;
import netclient.states.GameRunningState;
import netclient.states.MainMenuState;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import netserver.services.ServiceManager;

public class WJSFClient extends SimpleApplication implements ClientStateListener {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	public AppSettings settings;
	public BitmapFont defaultFont;
	
    public GUI gui;
	
	public GameRunningState gameRunState;
	public MainMenuState mainMenuState;
	
    public Client client;
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public static void main(String[] args) {
    	NetMessages.initSerializables();
        
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("GameProduction Project");
        settings.setResolution(1600, 900);
        settings.setVSync(true);
        settings.setBitsPerPixel(24);
        settings.setSamples(16);

        WJSFClient app = new WJSFClient();
        app.setSettings(settings);
        app.settings = settings;
        
        app.setShowSettings(false);

        app.start();
    }
    
    @Override
    public void simpleInitApp() {
    	this.getFlyByCamera().setEnabled(false);
    	this.pauseOnFocus = false;
        this.getStateManager().detach(this.getStateManager().getState(FlyCamAppState.class));
        
    	this.inputManager.clearMappings();
    	this.defaultFont = this.guiFont;
    	
    	this.mainMenuState = new MainMenuState(this);
    	this.gameRunState = new GameRunningState(this);
    	this.stateManager.attach(this.mainMenuState);
    	
    	this.gui = new GUI(this);
        ServiceManager.getEditorManager().setClient(this);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
    }
    
    @Override
    public void destroy() {
    	if(client != null && client.isConnected()) {
	    	try {
	    		client.close();
	    	} catch(Exception e) {
	    		Logger.getLogger(WJSFServer.class.getName()).log(Level.SEVERE, null, e);
	    	}
    	}
    	
        super.destroy();
    }

	@Override
	public void clientConnected(Client arg0) {
		Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg0.toString(), arg0);
		
		this.stateManager.detach(this.mainMenuState);
		this.stateManager.attach(this.gameRunState);
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg0.toString(), arg0);
		
		this.stateManager.detach(this.gameRunState);
		this.stateManager.attach(this.mainMenuState);
	}
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}