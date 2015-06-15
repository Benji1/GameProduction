package netutil;




import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.util.ArrayList;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import org.jbox2d.common.Vec2;

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
        Serializer.registerClass(PosAndRotMsg.class);
        Serializer.registerClass(ClientEnteredMsg.class);
        Serializer.registerClass(KeyPressedMsg.class);
        Serializer.registerClass(OrientedModule.class);
        Serializer.registerClass(ShipChangedMsg.class);
        Serializer.registerClass(NearStationMsg.class);
        Serializer.registerClass(ModuleActivatedMsg.class);
        Serializer.registerClass(ToggleEditorMsg.class);
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
        private float velX;
        private float velY;
        private float angVel;
        private int id;
    	
    	public PosAndRotMsg() {}
    	
    	public PosAndRotMsg(Vector3f pos, Quaternion dir, Vec2 vel, float angVel, int id) {
    		this.pos = pos;
    		this.dir = dir;
                this.id = id;
                this.velX = vel.x;
                this.velY = vel.y;
                this.angVel = angVel;
    	}
    	
    	public Vector3f getPos() {return this.pos;}
    	public Quaternion getDir() {return this.dir;}
        public Vec2 getVelocity() {return new Vec2(velX, velY);}
        public float getAngVel() {return this.angVel;}
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
        private ModuleType[] modulesInBase;
        private Vector3f pos;
        private Vector3f dir;

        public ClientEnteredMsg() {}
        
        public ClientEnteredMsg(String name, int id, OrientedModule[][] ship, ModuleType[] modulesInBase, Vector3f pos, Vector3f dir) {
            this.name = name;
            this.id = id;
            this.ship = ship;
            this.modulesInBase = modulesInBase;
            this.pos = pos;
            this.dir = dir;
        }

        public String getName() {return this.name;}
        public int getId() {return this.id;}
        public OrientedModule[][] getShip() {return this.ship;}
        public ModuleType[] getModulesInBase() {return this.modulesInBase;}
        public Vector3f getPos() {return this.pos;}
        public Vector3f getDir() {return this.dir;}
    }
    
    @Serializable
    public static class KeyPressedMsg extends AbstractMessage {
    	private Integer keyCode;
    	private boolean keyPressed;
    	
    	public KeyPressedMsg() {}
    	
    	public KeyPressedMsg(Integer keyCode, boolean keyPressed) {
    		this.keyCode = keyCode;
    		this.keyPressed = keyPressed;
    	}
    	
    	public Integer getKeyCode() {return this.keyCode;}
    	public boolean getKeyPressed() {return this.keyPressed;}
    }
    
    @Serializable
    public static class ShipChangedMsg extends AbstractMessage {
        private int shipId;
        private OrientedModule[][] modules;
        private ModuleType[] modulesInBase;
        
        public ShipChangedMsg() {}
        
        public ShipChangedMsg(int shipId, OrientedModule[][] modules, ModuleType[] modulesInBase) {
            this.shipId = shipId;
            this.modules = modules;
            this.modulesInBase = modulesInBase;
        }
        
        public int getShipId() {return this.shipId;}
        public OrientedModule[][] getModules() {return this.modules;}
        public ModuleType[] getModulesInBase() {return this.modulesInBase;}
    }
    
    @Serializable
    public static class NearStationMsg extends AbstractMessage {

        private boolean nearby;
        private int id;

        public NearStationMsg() {}

        public NearStationMsg(boolean nearby, int id) {
            this.nearby = nearby;
            this.id = id;
        }

        public boolean getNearby() {return this.nearby;}
        public int getId() {return this.id;}
    }
    
    @Serializable
    public static class ModuleActivatedMsg extends AbstractMessage {        
        private int shipId;
        private int xPos;
        private int yPos;
        private boolean avctive;
        
        public ModuleActivatedMsg() {}
        
        public ModuleActivatedMsg(int shipId, int xPos, int yPos, boolean active) {
            this.shipId = shipId;
            this.xPos = xPos;
            this.yPos = yPos;
            this.avctive = active;
        }
        
        public int getShipId() {return this.shipId;}
        public int getXPos() {return this.xPos;}
        public int getYPos() {return this.yPos;}
        public boolean isActive() {return this.avctive;}                
    }
    
    @Serializable
    public static class ToggleEditorMsg extends AbstractMessage {        
        private int shipId;
        private ModuleType[] modulesInBase;
        
        public ToggleEditorMsg() {}
        
        public ToggleEditorMsg(int shipId, ModuleType[] modulesInBase) {
            this.shipId = shipId;
            this.modulesInBase = modulesInBase;
        }
        
        public int getShipId() {return this.shipId;}
        public ModuleType[] getModulesInBase() {return this.modulesInBase;}              
    }
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    
    
    
    
}