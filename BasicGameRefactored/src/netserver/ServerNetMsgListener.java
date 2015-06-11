package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import netutil.NetMessages.KeyPressedMsg;
import netutil.NetMessages.NetMsg;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerNetMsgListener implements MessageListener<HostedConnection> {
	
	WJSFServer app;
	
	public ServerNetMsgListener(WJSFServer app) {
		this.app = app;
	}
	
	@Override
	public void messageReceived(HostedConnection client, Message m) {
		if(m instanceof KeyPressedMsg) {
			KeyPressedMsg msg = (KeyPressedMsg)m;
			
			// find player and update input status
			for(NetPlayer pl : this.app.getConManager().players) {
				if(pl.con.getId() == client.getId()) {
					pl.input.updateInputStatus(msg.getInput(), msg.getKeyPressed());
					return;
				}
			}
		}
	}
}
