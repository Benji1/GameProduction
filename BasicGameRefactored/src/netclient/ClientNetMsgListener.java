package netclient;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.WJSFServer;
import netutil.NetMessages.ClientEnteredMsg;
import netutil.NetMessages.NetMsg;
import netutil.NetMessages.PosMsg;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class ClientNetMsgListener implements MessageListener<Client> {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	
	private WJSFClient app;
	private ConcurrentLinkedQueue<String> msgQueue;
	
	
	
	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public ClientNetMsgListener(WJSFClient app) {
		this.app = app;
		this.msgQueue = new ConcurrentLinkedQueue<String>();
	}
	

	/**********************************
     ************ METHODS  ************
     **********************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(Client source, Message m) {
		if(m instanceof NetMsg) {
			NetMsg msg = (NetMsg)m;
			msgQueue.add(msg.getMessage());
		} else if (m instanceof ClientEnteredMsg) {
			ClientEnteredMsg msg = (ClientEnteredMsg)m;
			
			this.app.enqueue(new Callable() {
				public Object call() throws Exception {
					ClientShip ship = new ClientShip(msg.getName(), msg.getId(), msg.getShip());
					
					if(app.gameRunState.playerShip == null && app.client.getId() == ship.id) {	// new ship is this player
						app.gameRunState.playerShip = ship;
						app.gameRunState.initKeys();
		
				        app.gameRunState.localRootNode.attachChild(ship.shipRoot);
					} else {
						// dont add if already added
						for(ClientShip s : app.gameRunState.clientShips)
							if(s.id == ship.id)
								return null;
						
						app.gameRunState.clientShips.add(ship);
						app.gameRunState.localRootNode.attachChild(ship.shipRoot);
					}
					
					// add testbox
					Spatial spatial;
			        Material material;    
			        
			        Box box = new Box(1, 0.4f, 1);
			        spatial = new Geometry("Box", box);
			        material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		
			        ColorRGBA color = ColorRGBA.Blue;
			        material.setBoolean("UseMaterialColors", true);
			        material.setColor("Ambient", color);
			        material.setColor("Diffuse", color);
		
			        spatial.setMaterial(material);
			        ship.shipRoot.attachChild(spatial);
			        ship.shipRoot.setLocalTranslation(msg.getPos());
			        
			        return null;
				}
			});
		} else if (m instanceof PosMsg) {
			PosMsg msg = (PosMsg)m;
			
			this.app.enqueue(new Callable() {
				public Object call() throws Exception {
					if(app.gameRunState.playerShip.id == msg.getId()) {
						app.gameRunState.playerShip.shipRoot.setLocalTranslation(msg.getPos());
						Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, msg.getPos().toString());
					} else {
						for(ClientShip s : app.gameRunState.clientShips) {
							if(s.id == msg.getId()) {
								s.shipRoot.setLocalTranslation(msg.getPos());
								return null;
							}
						}
					}
					return null;
				}
			});
		}
	}
	
	public void update(float tpf) {
		// handle string msgs
		String msg = this.msgQueue.poll();
		if(msg != null)
			Logger.getLogger(ClientNetMsgListener.class.getName()).log(Level.INFO, msg);
	}
}
