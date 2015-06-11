package netutil;

import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.NetPlayer;
import netserver.WJSFServer;

public class NetInput {
	public static enum InputTypes {
    	MoveUp, MoveLeft, MoveRight, MoveDown, Weapon, Shield
    }
	
	private NetPlayer player;
	private boolean[] inputStatus;
	
	public NetInput(NetPlayer player) {
		this.player = player;
		this.inputStatus = new boolean[InputTypes.values().length];
	}
	
	public void updateInputStatus(InputTypes type, boolean keyPressed) {
		for(int i = 0; i < InputTypes.values().length; i++) {
			if(InputTypes.values()[i] == type) {
				inputStatus[i] = keyPressed;
				Logger.getLogger(NetInput.class.getName()).log(Level.INFO, "InputType " + type.toString() + " set to " + keyPressed + " on client " + this.player.con.getId());
				return;
			}
		}
	}
	
	public boolean getInputStatus(InputTypes type) {
		for(int i = 0; i < InputTypes.values().length; i++) {
			if(InputTypes.values()[i] == type) {
				return inputStatus[i];
			}
		}
		
		Logger.getLogger(NetInput.class.getName()).log(Level.WARNING, "InputType " + type.toString() + " not found.");
		return false;
	}
}