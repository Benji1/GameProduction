package netutil;


import netclient.states.GameRunningState.InputTypes;

import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

public class NetMessages {

	
	/**********************************
     ************ GLOBALS  ************
     **********************************/
	
    public static final int PORT = 6143;

    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public static void initSerializables() {
        Serializer.registerClass(NetMsg.class);
        Serializer.registerClass(PosMsg.class);
        Serializer.registerClass(PosAndDirMsg.class);
        Serializer.registerClass(ClientEnteredMsg.class);
        Serializer.registerClass(KeyPressedMsg.class);
    }

    @Serializable
    public static class PosAndDirMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	private Vector3f dir;
    	
    	public PosAndDirMsg() {}
    	
    	public PosAndDirMsg(Vector3f pos, Vector3f dir) {
    		this.pos = pos;
    		this.dir = dir;
    	}
    	
    	public Vector3f getPos() {
    		return this.pos;
    	}
    	
    	public Vector3f getDir() {
    		return this.dir;
    	}
    }
    
    @Serializable
    public static class PosMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	
    	public PosMsg() {}
    	
    	public PosMsg(Vector3f pos) {
    		this.pos = pos;
    	}
    	
    	public Vector3f getPos() {
    		return this.pos;
    	}
    }
    
    @Serializable
    public static class NetMsg extends AbstractMessage {

        private String message;

        public NetMsg() {
        	this.message = "No msg sent.";
        }

        public NetMsg(String msg) {
            this.message = msg;
        }

        public String getMessage() {
            return this.message;
        }
    }
    
    /*
     * elements of ship[][]
     * 0 - empty; 1 - cockpit; 2 - armor; 3 - generator; 4 - laser; 5 - thruster; 6 - weak thruster; 7 - shield;
     */
    
    @Serializable
    public static class ClientEnteredMsg extends AbstractMessage {
    	
        private String name;
        private int id;
        private int[][] ship;
        private Vector3f pos;
        private Vector3f dir;

        public ClientEnteredMsg() {}
        
        public ClientEnteredMsg(String name, int id, int[][] ship, Vector3f pos, Vector3f dir) {
            this.name = name;
            this.id = id;
            this.ship = ship;
            this.pos = pos;
            this.dir = dir;
        }

        public String getName() {
            return this.name;
        }
        
        public int getId() {
        	return this.id;
        }
        
        public int[][] getShip() {
        	return this.ship;
        }
        
        public Vector3f getPos() {
        	return this.pos;
        }
        
        public Vector3f getDir() {
        	return this.dir;
        }
    }
    
    @Serializable
    public static class KeyPressedMsg extends AbstractMessage {
    	private InputTypes input;
    	private boolean keyPressed;
    	
    	public KeyPressedMsg() {}
    	
    	public KeyPressedMsg(InputTypes input, boolean keyPressed) {
    		this.input = input;
    		this.keyPressed = keyPressed;
    	}
    	
    	public InputTypes getInput() {
    		return this.input;
    	}
    	
    	public boolean getKeyPressed() {
    		return this.keyPressed;
    	}
    }
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    
    
    
    
}