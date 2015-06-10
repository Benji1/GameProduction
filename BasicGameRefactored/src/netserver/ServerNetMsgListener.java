package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import netclient.states.GameRunningState.InputTypes;
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
			Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, ((KeyPressedMsg)m).getInput().toString());
		}
		//if (name.equals(InputTypes.MoveUp.toString())) {
        	/*playersShip.activateModules("Up");
            up = true;
            if (!keyPressed) {
                playersShip.deactivateModules("Up");
                up = false;
            }*/
        //}
        
        //if (name.equals(InputTypes.MoveLeft.toString())) {
            /*playersShip.activateModules("Left");
            if (!keyPressed) {
                playersShip.deactivateModules("Left");
                if (up) {
                    playersShip.activateModules("Up");
                } else if (down) {
                    playersShip.activateModules("Down");
                }
            }*/
        //}

        //if (name.equals(InputTypes.MoveRight.toString())) {
            /*playersShip.activateModules("Right");
            if (!keyPressed) {
                playersShip.deactivateModules("Right");
                if (up) {
                    playersShip.activateModules("Up");
                } else if (down) {
                    playersShip.activateModules("Down");
                }
            }*/
        //}

        //if (name.equals(InputTypes.MoveDown.toString())) {
            /*playersShip.activateModules("Down");
            down = true;
            if (!keyPressed) {
                playersShip.deactivateModules("Down");
                down = false;
            }*/
        //}

        //if (name.equals(InputTypes.Weapon.toString())) {
            /*playersShip.activateModules("Weapon");
            if (!keyPressed) {
                playersShip.deactivateModules("Weapon");
            }*/
        //}

        //if (name.equals(InputTypes.Shield.toString()) && !keyPressed) {
            // TODO: improve bool test
            /*if (playersShip.getInteractiveModulesWithHotkey("Shield").size() > 0 && playersShip.getInteractiveModulesWithHotkey("Shield").get(0) != null) {
                if (playersShip.getInteractiveModulesWithHotkey("Shield").get(0).isActive()) {
                    playersShip.deactivateModules("Shield");
                    //targetShip.deactivateModules("Shield");
                } else {
                    playersShip.activateModules("Shield");
                    //targetShip.activateModules("Shield");
                }
            }*/

        //}
	}
}
