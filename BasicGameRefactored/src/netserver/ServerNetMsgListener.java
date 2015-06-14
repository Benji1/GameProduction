package netserver;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import netutil.NetMessages.KeyPressedMsg;
import netutil.NetMessages.NetMsg;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import netutil.NetMessages.ShipChangedMsg;

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
							pl.handleKeyEvent(msg.getKeyCode(), msg.getKeyPressed());
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
                }
	}
}
