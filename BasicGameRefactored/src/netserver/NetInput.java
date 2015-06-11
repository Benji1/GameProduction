package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

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
		// keep track of input status
		for(int i = 0; i < InputTypes.values().length; i++) {
			if(InputTypes.values()[i] == type) {
				inputStatus[i] = keyPressed;
				break;
			}
		}
		
		// update ship modules
		if(	type == InputTypes.Shield) {
			if (this.player.ship.getInteractiveModulesWithHotkey("Shield").size() > 0 && this.player.ship.getInteractiveModulesWithHotkey("Shield").get(0) != null) {
		        if (this.player.ship.getInteractiveModulesWithHotkey("Shield").get(0).isActive()) {
		        	this.player.ship.deactivateModules("Shield");
		            //targetShip.deactivateModules("Shield");
		        } else {
		        	this.player.ship.activateModules("Shield");
		            //targetShip.activateModules("Shield");
		        }
		    }
		} else {
			if(keyPressed)
				this.player.ship.activateModules(type.toString());
			else
				this.player.ship.deactivateModules(type.toString());
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