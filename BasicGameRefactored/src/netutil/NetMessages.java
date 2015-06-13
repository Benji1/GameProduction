package netutil;




import netserver.NetInput.InputTypes;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import netclient.gui.OrientedModule;

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
        Serializer.registerClass(OrientedModule.class);
        Serializer.registerClass(ShipChangedMsg.class);
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
    	
    	public Vector3f getPos() {return this.pos;}
    	public Vector3f getDir() {return this.dir;}
    }
    
    @Serializable
    public static class PosMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	private int id;
    	
    	public PosMsg() {}
    	
    	public PosMsg(Vector3f pos, int id) {
    		this.pos = pos;
    		this.id = id;
    	}
    	
    	public Vector3f getPos() {return this.pos;}
    	public int getId() {return this.id;}
    }
    
    @Serializable
    public static class PosAndRotMsg extends AbstractMessage {
    	
    	private Vector3f pos;
    	private Quaternion dir;
        private int id;
    	
    	public PosAndRotMsg() {}
    	
    	public PosAndRotMsg(Vector3f pos, Quaternion dir, int id) {
    		this.pos = pos;
    		this.dir = dir;
                this.id = id;
    	}
    	
    	public Vector3f getPos() {return this.pos;}
    	public Quaternion getDir() {return this.dir;}
        public int getId() {return this.id;}
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

        public String getMessage() {return this.message;}
    }
    
    @Serializable
    public static class ClientEnteredMsg extends AbstractMessage {
    	
        private String name;
        private int id;
        private OrientedModule[][] ship;
        private Vector3f pos;
        private Vector3f dir;

        public ClientEnteredMsg() {}
        
        public ClientEnteredMsg(String name, int id, OrientedModule[][] ship, Vector3f pos, Vector3f dir) {
            this.name = name;
            this.id = id;
            this.ship = ship;
            this.pos = pos;
            this.dir = dir;
        }

        public String getName() {return this.name;}
        public int getId() {return this.id;}
        public OrientedModule[][] getShip() {return this.ship;}
        public Vector3f getPos() {return this.pos;}
        public Vector3f getDir() {return this.dir;}
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
    	
    	public InputTypes getInput() {return this.input;}
    	public boolean getKeyPressed() {return this.keyPressed;}
    }
    
    @Serializable
    public static class ShipChangedMsg extends AbstractMessage {
        private int shipId;
        private OrientedModule[][] modules;
        
        public ShipChangedMsg() {}
        
        public ShipChangedMsg(int shipId, OrientedModule[][] modules) {
            this.shipId = shipId;
            this.modules = modules;
        }
        
        public int getShipId() {return this.shipId;}
        public OrientedModule[][] getModules() {return this.modules;}
    }
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    
    
    
    
}