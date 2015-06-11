package netserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import netutil.NetInput;
import netutil.NetInput.InputTypes;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;

import mygame.BasicShip;

public class NetPlayer {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
	private WJSFServer app;
	
	public HostedConnection con;
	public NetInput input;
	public BasicShip ship;
	
	public Vector3f pos;
	
	public int[][] shipArray =
		{
			{0, 0, 4, 0, 0},
			{0, 2, 3, 2, 0},
			{7, 2, 1, 2, 7},
			{0, 5, 3, 5, 0},
			{0, 0, 5, 0, 0}
		};

	/**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
	
	public NetPlayer(WJSFServer app, HostedConnection con) {
		this.app = app;
		this.con = con;

		//this.ship = ShipDesigns.TestShipDesigns.
		this.input = new NetInput(this);
		this.pos = new Vector3f(this.app.rnd.nextFloat() * 20f, 0, this.app.rnd.nextFloat() * 20f);
	}
	
	
	/**********************************
     ************ METHODS  ************
     **********************************/
	
	public void update(float tpf) {
		if(this.input.getInputStatus(InputTypes.MoveUp)) {
			Logger.getLogger(WJSFServer.class.getName()).log(Level.INFO, this.pos.toString());
			this.pos.addLocal(new Vector3f(0, 0, -tpf));
		}
		// ship update here
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
	
	
	/**********************************
     ******** GETTER & SETTER  ********
     **********************************/
}