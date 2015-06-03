package netclient;

import gui.GUI;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import states.GameRunningState;
import states.MainMenuState;
import netserver.GameProductionServer;
import netmsg.NetUtil;
import netmsg.NetUtil.*;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

public class GameProductionClient extends SimpleApplication {
	public AppSettings settings;
	public BitmapFont defaultFont;
	
    public GUI gui;
	
	public GameRunningState gameRunState;
	public MainMenuState mainMenuState;
	
    public Client client;
    private ConcurrentLinkedQueue<String> msgQueue;

    public static void main(String[] args) {
    	NetUtil.initSerializables();
        
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("GameProduction Project");
        settings.setResolution(1600, 900);
        settings.setVSync(true);
        settings.setBitsPerPixel(24);
        settings.setSamples(16);

        GameProductionClient app = new GameProductionClient();
        app.setSettings(settings);
        app.settings = settings;
        
        app.setShowSettings(false);

        app.start();
    }
    
    @Override
    public void simpleInitApp() {
    	this.getFlyByCamera().setEnabled(false);
        this.getStateManager().detach(this.getStateManager().getState(FlyCamAppState.class));
        
    	this.inputManager.clearMappings();
    	this.defaultFont = this.guiFont;
    	
    	this.mainMenuState = new MainMenuState(this);
    	this.gameRunState = new GameRunningState(this);
    	this.stateManager.attach(this.mainMenuState);
    	
    	this.gui = new GUI(this);
    	
    	this.msgQueue = new ConcurrentLinkedQueue<String>();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
    	String msg = this.msgQueue.poll();
    	
    	if(msg != null) {
    		fpsText.setText(msg);
    	} else {
    		fpsText.setText("FPS: " + tpf);
    	}
    }
    
    private class NetworkMessageListener implements MessageListener<Client> {

		@Override
		public void messageReceived(Client source, Message m) {
			if(m instanceof NetMsg) {
				NetMsg msg = (NetMsg)m;
				msgQueue.add(msg.getMessage());
			} else if (m instanceof PosMsg) {
				/*PosMsg msg = (PosMsg)m;
				
				GameProductionClient.this.enqueue(new Callable() {
					public Object call() throws Exception {
						geom.setLocalTranslation(msg.getPos());
						return null;
					}
				});*/
			}
		}
    	
    }
    
    @Override
    public void destroy() {
    	try {
    		client.close();
    	} catch(Exception e) {
    		Logger.getLogger(GameProductionServer.class.getName()).log(Level.SEVERE, null, e);
    	}
    	
        super.destroy();
    }
}