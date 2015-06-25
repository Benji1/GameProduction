package netserver;

import com.jme3.input.KeyInput;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import netutil.NetMessages.KeyPressedMsg;
import netutil.NetMessages.NetMsg;
import netutil.NetMessages.PlayerNameMsg;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import netutil.NetMessages.ShipChangedMsg;
import netutil.NetMessages.ToggleEditorMsg;

public class ServerNetMsgListener implements MessageListener<HostedConnection> {
	
	WJSFServer app;
	
	public ServerNetMsgListener(WJSFServer app) {
		this.app = app;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(final HostedConnection client, Message m) {
		if(m instanceof KeyPressedMsg) {
			final KeyPressedMsg msg = (KeyPressedMsg)m;
			
			this.app.enqueue(new Callable() {
				public Object call() throws Exception {
					// find player and update input status
					for(NetPlayer pl : app.getConManager().players) {
						if(pl.con.getId() == client.getId()) {
	                        if (msg.getKeyCode().equals(KeyInput.KEY_E) && !msg.getKeyPressed()) {
                                // toggle editor
                                pl.getInventory().moveItemsFromShipToBaseStorage();
                                ToggleEditorMsg msg = new ToggleEditorMsg(pl.con.getId(), pl.getInventory().getModulesInBase());
                                msg.setReliable(true);
                                app.getServer().broadcast(msg);
	                        } else {
                                pl.handleKeyEvent(msg.getKeyCode(), msg.getKeyPressed());
	                        }
	                        
	                        return null;
						}
					}
					
					return null;
				}
			});
		} else if (m instanceof ShipChangedMsg) {
	        final ShipChangedMsg msg = (ShipChangedMsg)m;
	        
	        this.app.enqueue(new Callable() {
	            public Object call() throws Exception {                            
	                for (NetPlayer pl : app.getConManager().players) {
	                    if (pl.con.getId() == client.getId()) {
	                        // update ship on server
	                        pl.ship.onShipChanged(msg.getModules());
	                        pl.updateBaseInventory(msg.getModulesInBase());
	                    }                                
	                }
	                // send update to all clients
	                app.getServer().broadcast(msg);
	                
	                return null;
	            }
	        });
		} else if (m instanceof PlayerNameMsg) {
			final PlayerNameMsg msg = (PlayerNameMsg)m;
	        
	        this.app.enqueue(new Callable() {
	            public Object call() throws Exception {                            
	                for (NetPlayer pl : app.getConManager().players) {
	                    if (pl.con.getId() == client.getId()) {
	                        pl.getShip().setName(msg.getName());
	                        
	                        PlayerNameMsg newMsg = new PlayerNameMsg(pl.con.getId(), msg.getName());
	                        newMsg.setReliable(true);
	    	                app.getServer().broadcast(newMsg);
	                    }
	                }
	                
	                return null;
	            }
	        });
		}
	}
}
