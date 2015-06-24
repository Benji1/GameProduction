package netutil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;

import org.jbox2d.common.Vec2;

public class NetMessages {
    
    public static final String PLAYER_DIED_MSG = "Player died";

	
	/**********************************
     ************ GLOBALS  ************
     **********************************/
	
    public static final int PORT = 6143;
    public static final String IP = "localhost";

    
    
    
    
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
        Serializer.registerClass(SpawnLaserProjectileMsg.class);
        Serializer.registerClass(GraphicObjPosAndRotMsg.class);
        Serializer.registerClass(DeleteGraphicObjectMsg.class);
        Serializer.registerClass(ModuleDestroyedMsg.class);
        Serializer.registerClass(SpawnItemMsg.class);
        Serializer.registerClass(SpawnSpaceStationMsg.class);
        Serializer.registerClass(ExplosionParticleMsg.class);
        Serializer.registerClass(ShieldHitParticleMsg.class);
        Serializer.registerClass(PlayerNameMsg.class);
        Serializer.registerClass(SpawnUniverseEntity.class);
        Serializer.registerClass(UpdateUniverseEntity.class);
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
    
    @Serializable
    public static class SpawnLaserProjectileMsg extends AbstractMessage {
        private int id;
        private float spawnX;
        private float spawnY;
        private float dirX;
        private float dirY;
        
        public SpawnLaserProjectileMsg() {}
        
        public SpawnLaserProjectileMsg(int id, Vec2 spawnPoint, Vec2 dir) {
            this.id = id;
            this.spawnX = spawnPoint.x;
            this.spawnY = spawnPoint.y;
            this.dirX = dir.x;
            this.dirY = dir.y;
        }
        
        public int getId() {return this.id;}
        public Vec2 getSpawnPoint() {return new Vec2(spawnX, spawnY);}
        public Vec2 getDir() {return new Vec2(dirX, dirY);}
    }
    
    @Serializable
    public static class GraphicObjPosAndRotMsg extends AbstractMessage {    	
    	private Vector3f pos;
    	private Quaternion dir;
        private float velX;
        private float velY;
        private float angVel;
        private int id;
    	
    	public GraphicObjPosAndRotMsg() {}
    	
    	public GraphicObjPosAndRotMsg(Vector3f pos, Quaternion dir, Vec2 vel, float angVel, int id) {
    		this.pos = pos;
    		this.dir = dir;
                this.id = id;
                this.velX = vel.x;
                this.velY = vel.y;
                this.angVel = angVel;
    	}
    	
    	public Vector3f getPos() {return this.pos;}
    	public Quaternion getRot() {return this.dir;}
        public Vec2 getVelocity() {return new Vec2(velX, velY);}
        public float getAngVel() {return this.angVel;}
        public int getId() {return this.id;}
    }
    
    @Serializable
    public static class DeleteGraphicObjectMsg extends AbstractMessage {
        private int id;
        
        public DeleteGraphicObjectMsg() {}
        
        public DeleteGraphicObjectMsg(int id) {
            this.id = id;
        }
        
        public int getId() {return this.id;}
    }

    @Serializable
    public static class ModuleDestroyedMsg extends AbstractMessage {
        private int shipId;
        private OrientedModule[][] modules;
        
        public ModuleDestroyedMsg() {}
        
        public ModuleDestroyedMsg(int shipId, OrientedModule[][] modules) {
            this.shipId = shipId;
            this.modules = modules;
        }
        
        public int getShipId() {return this.shipId;}
        public OrientedModule[][] getModules() {return this.modules;}
    }
    
    @Serializable
    public static class SpawnItemMsg extends AbstractMessage {
        private int id;
        private float spawnX;
        private float spawnY;
        private Quaternion rot;
        private OrientedModule om;
        
        public SpawnItemMsg() {}
        
        public SpawnItemMsg(int id, Vec2 spawnPoint, Quaternion rot, OrientedModule om) {
            this.id = id;
            this.spawnX = spawnPoint.x;
            this.spawnY = spawnPoint.y;
            this.rot = rot;
            this.om = om;
        }
        
        public int getId() {return this.id;}
        public Vec2 getSpawnPoint() {return new Vec2(spawnX, spawnY);}
        public Quaternion getRot() {return rot;}
        public OrientedModule getOrientedModule() {return om;}
    }
    
    @Serializable
    public static class SpawnSpaceStationMsg extends AbstractMessage {
        private int id;
        private float spawnX;
        private float spawnY;
        
        public SpawnSpaceStationMsg() {}
        
        public SpawnSpaceStationMsg(int id, Vector3f spawnPoint) {
            this.id = id;
            this.spawnX = spawnPoint.x;
            this.spawnY = spawnPoint.z;
        }
        
        public int getId() {return this.id;}
        public Vec2 getSpawnPoint() {return new Vec2(spawnX, spawnY);}
    }
    
    @Serializable
    public static class ExplosionParticleMsg extends AbstractMessage {
        private float spawnX;
        private float spawnY;
        
        public ExplosionParticleMsg() {}
        
        public ExplosionParticleMsg(Vec2 spawnPoint) {
            this.spawnX = spawnPoint.x;
            this.spawnY = spawnPoint.y;
        }
        
        public Vec2 getSpawnPoint() {return new Vec2(spawnX, spawnY);}
    }
    
    @Serializable
    public static class ShieldHitParticleMsg extends AbstractMessage {
        private float spawnX;
        private float spawnY;
        
        public ShieldHitParticleMsg() {}
        
        public ShieldHitParticleMsg(Vec2 spawnPoint) {
            this.spawnX = spawnPoint.x;
            this.spawnY = spawnPoint.y;
        }
        
        public Vec2 getSpawnPoint() {return new Vec2(spawnX, spawnY);}
    }
    
    @Serializable
    public static class PlayerNameMsg extends AbstractMessage {
    	private int id;
        private String name;
        
        public PlayerNameMsg() {}
        
        public PlayerNameMsg(int id, String name) {
        	this.id = id;
            this.name = name;
        }
        
        public String getName() {return this.name;}
        public int getId() {return this.id;}
    }

    public static class SpawnUniverseEntity extends AbstractMessage {
    	public float spawnX;
        public float spawnY;
        public float size;
        public int texture;
        public ColorRGBA color;
        public boolean light;
        public int ID;
        
        public SpawnUniverseEntity() {}
        
        public SpawnUniverseEntity(float x, float y, float size, int texture, ColorRGBA color, boolean light, int ID){
        	this.spawnX = x;
        	this.spawnY = y;
        	this.size = size;
        	this.texture = texture;
        	this.color = color;
        	this.light = light;
        	this.ID = ID;
        }
    }
    
    @Serializable
    public static class UpdateUniverseEntity extends AbstractMessage {
    	public float x;
    	public float y;
    	public int ID;
    	
    	public UpdateUniverseEntity() {}
    	
    	public UpdateUniverseEntity(float x, float y, int ID){
    		this.x = x;
    		this.y = y;
    		this.ID = ID;
    	}
    }
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    
    
    
    
}