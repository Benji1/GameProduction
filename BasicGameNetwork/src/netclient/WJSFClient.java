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
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import netclient.states.EndGameMenuState;
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
        public EndGameMenuState endGameMenuState;
	
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

        app = new WJSFClient();
        app.setSettings(settings);
        app.settings = settings;
        
        app.setShowSettings(false);

        app.start();
    }
    static WJSFClient app;
    
    @Override
    public void simpleInitApp() {
    	this.getFlyByCamera().setEnabled(false);
    	this.pauseOnFocus = false;
        this.getStateManager().detach(this.getStateManager().getState(FlyCamAppState.class));
        
    	this.inputManager.clearMappings();
    	this.defaultFont = this.guiFont;
    	
    	this.mainMenuState = new MainMenuState(this);
    	this.gameRunState = new GameRunningState(this);
        this.endGameMenuState = new EndGameMenuState(this);
    	this.stateManager.attach(this.mainMenuState);
    	
    	this.gui = new GUI(this);
        ServiceManager.getEditorManager().setClient(this);
        
        CreateBGSound();
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
                this.gameRunState = new GameRunningState(this);
		this.stateManager.attach(this.gameRunState);
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
                Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, arg0.toString(), arg0);
                this.stateManager.detach(this.gameRunState);
                
                if (arg1 != null && arg1.reason != null && arg1.reason.equals(NetMessages.PLAYER_DIED_MSG)) {
                    this.stateManager.attach(this.endGameMenuState);
                } else {
                    this.stateManager.attach(this.mainMenuState);
                }
	}
    public void CreateBGSound()
    {
        AudioNode audioNode = new AudioNode(app.getAssetManager(), "Sound/Effects/bg.wav", false);
        audioNode.setPositional(false);
        audioNode.setLooping(true);
        audioNode.setVolume(1f);
        app.getRootNode().attachChild(audioNode);
        audioNode.play();
    }
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}