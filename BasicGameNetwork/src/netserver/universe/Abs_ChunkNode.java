package netserver.universe;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.universe.Universe;
import netutil.NetMessages.*;

import com.jme3.math.Vector3f;
import com.jme3.network.Filters;
import com.jme3.scene.Node;

/**
 * ChunkNode extends Node and adds functionality to use the entities
 * within our universe system, which is divided into chunks.
 * @author 
 */
public abstract class Abs_ChunkNode extends Node {
	/**********************************
     ************ GLOBALS  ************
     **********************************/
	
	public enum ChunkNodeType { PlayerShips, Computerships, SolarSystems, SpaceStations }
	
	
	
	
	
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
    protected WJSFServer app;  		// the main game class
    private ChunkNodeType type;		// the type of the Node
    
    private int chunkX;            	// the current chunkX
    private int chunkZ;            	// the current chunkZ
    private int chunkXLast;			// the last chunkX
    private int chunkZLast;			// the last chunkZ
    private Vector3f posCurChunk;   // the position within the current chunk
    private Vector3f posToCheck;
    
    private boolean isStatic;
    
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Abs_ChunkNode(WJSFServer app, String name, ChunkNodeType t, boolean isStatic, int chunkX, int chunkZ, Vector3f posInChunk) {
    	super(name);
    	System.out.println(name + ": " + chunkX + "/" + chunkZ + ", " + posInChunk.toString());
        this.app = app;
        this.type = t;
        this.isStatic = isStatic;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkXLast = chunkX;
        this.chunkZLast = chunkZ;
        this.posCurChunk = posInChunk;
        this.posToCheck = new Vector3f(posInChunk.x + chunkX * Universe.CHUNK_SIZE, posInChunk.y, posInChunk.z + chunkZ * Universe.CHUNK_SIZE);
        this.setLocalTranslation(this.posToCheck);
        this.app.getUniverse().getChunk(chunkX, chunkZ).getListOfType(this.type).add(this);
        this.app.getRootNode().attachChild(this);
    }
    
    public Abs_ChunkNode(WJSFServer app, String name, ChunkNodeType t, boolean isStatic, Vector3f posAbsolute) {
    	this(app, name, t, isStatic, 
    			(int)((posAbsolute.x - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE), (int)((posAbsolute.z - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE),
    			new Vector3f(posAbsolute.x + ((int)((posAbsolute.x - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE * -1), posAbsolute.y, posAbsolute.z + ((int)((posAbsolute.z - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE * -1)));
    }
    
    protected Abs_ChunkNode() {}
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public void update (float tpf) {
    	this.update(tpf, this.getLocalTranslation());
    }
    
    public void update(float tpf, Vector3f posToCheck) {
    	this.posToCheck = posToCheck;
    	
    	if(!this.isStatic) {
	    	this.posCurChunk = new Vector3f(this.posToCheck.x + this.chunkX * Universe.CHUNK_SIZE * -1, this.posCurChunk.y, this.posToCheck.z + this.chunkZ * Universe.CHUNK_SIZE * -1);
	    	this.recalcChunkPos();
    	}
    }
    
    private void recalcChunkPos() {
    	/// TODO remove redundant code
    	this.chunkXLast = 0;
    	this.chunkZLast = 0;
        if(this.posCurChunk.x > Universe.CHUNK_SIZE / 2) {
            this.chunkX++;
            this.chunkXLast++;
            this.posCurChunk.x = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\n" + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ);
        } else if(this.posCurChunk.x < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkX--;
            this.chunkXLast--;
            this.posCurChunk.x = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\n" + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ);
        }
        
        if(this.posCurChunk.z > Universe.CHUNK_SIZE / 2) {
            this.chunkZ++;
            this.chunkZLast++;
            this.posCurChunk.z = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\n" + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ);
        } else if(this.posCurChunk.z < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkZ--;
            this.chunkZLast--;
            this.posCurChunk.z = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\n" + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ);
        }
        
        // entity entered new chunk
        this.app.getUniverse().changedChunkForEntity(this, this.chunkXLast, this.chunkZLast);
        
        // check universe if we discovered a new chunk
        if(this.checkForNewChunk(this.chunkX, this.chunkZ)) {
            this.newChunkDiscovered();
        }
    }
    
    private boolean checkForNewChunk(int chunkX, int chunkZ) {
        if(this.app.getUniverse().getChunk(chunkX, chunkZ).visited == true)
            return false;
        
        this.app.getUniverse().getChunk(chunkX, chunkZ).visited = true;
        return true;
    }

    private void newChunkDiscovered() {
    	// NEW CHUNK
    	this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\n" + this.name + " discovered Chunk: " + this.chunkX + "/" + this.chunkZ);
        
        NetMsg msg = new NetMsg(this.name + " discovered new Chunk " + this.chunkX + "/" + this.chunkZ);
        msg.setReliable(true);
        this.app.getServer().broadcast(msg);
        
        // go through neighboring chunks and create solarsystems
        for(int x = -1; x <= 1; x++) {
        	for(int z = -1; z <= 1; z++) {
        		if(this.app.getUniverse().getChunk(chunkX + x, chunkZ + z).generated == true)
        			return;
        		
        		this.app.getUniverse().getChunk(chunkX + x, chunkZ + z).generated = true;
        		
        		// allow solar systems only if there is not one nearby
            	Boolean found = false;
                for(int i = -1; i <= 1; i++) {
                	for(int j = -1; j <= 1; j++) {
                		if(this.app.getUniverse().getChunk(chunkX + i, chunkZ + j).solarSystems.size() > 0) {
                			found = true;
                			break;
                		}
                	}
                }
                
                if(found)
                	continue;
        		
        		// NEW SOLARSYSTEM
                if(this.app.rnd.nextFloat() < 0.2) {
                	float posX = ((this.chunkX + x) * Universe.CHUNK_SIZE) + (this.app.rnd.nextFloat() - 0.5f) * Universe.CHUNK_SIZE;
                	float posZ = ((this.chunkZ + z) * Universe.CHUNK_SIZE) + (this.app.rnd.nextFloat() - 0.5f) * Universe.CHUNK_SIZE;
                	SolarSystem sys = new SolarSystem(app, new Vector3f(posX, Universe.Y_LAYER_UNIVERSE, posZ));
                	
                	sys.broadcastSpawn();
                	
                	NetMsg netmsg = new NetMsg(this.name + " discovered new SolarSystem " + sys.name + " in " + (this.chunkX + x) + "/" + (this.chunkZ + z));
                	netmsg.setReliable(true);
                    this.app.getServer().broadcast(netmsg);
                }
                
                // NEW SPACESTATION
                if(this.app.rnd.nextFloat() < 0.3) {
                	float posX = ((this.chunkX + x) * Universe.CHUNK_SIZE) + (this.app.rnd.nextFloat() - 0.5f) * Universe.CHUNK_SIZE;
                	float posZ = ((this.chunkZ + z) * Universe.CHUNK_SIZE) + (this.app.rnd.nextFloat() - 0.5f) * Universe.CHUNK_SIZE;
                	SpaceStation ss = new SpaceStation(app, new Vector3f(posX, Universe.Y_LAYER_STATIONS, posZ));
                	
                	SpawnSpaceStationMsg syncStation = new SpawnSpaceStationMsg(ss.getId(), ss.getLocalTranslation());
                    syncStation.setReliable(true);
                    app.getServer().broadcast(syncStation);
                    
                    NetMsg netmsg = new NetMsg(this.name + " discovered new SpaceStation " + ss.name + " in " + (this.chunkX + x) + "/" + (this.chunkZ + z));
                	netmsg.setReliable(true);
                    this.app.getServer().broadcast(netmsg);
                }
        	}
        }
    }
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Vector3f getPosCurChunk() {return this.posCurChunk;}
    public int getChunkX() {return this.chunkX;}
    public int getChunkZ() {return this.chunkZ;}
    public ChunkNodeType getType(){return this.type;}
}
