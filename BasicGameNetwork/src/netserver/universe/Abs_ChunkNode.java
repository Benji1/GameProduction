package netserver.universe;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.universe.Universe;

import com.jme3.math.Vector3f;
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
    			(int)(posAbsolute.x / Universe.CHUNK_SIZE), (int)(posAbsolute.z / Universe.CHUNK_SIZE),
    			new Vector3f(posAbsolute.x + ((int)(posAbsolute.x / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE * -1), posAbsolute.y, posAbsolute.x + ((int)(posAbsolute.z / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE * -1)));
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
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.x < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkX--;
            this.chunkXLast--;
            this.posCurChunk.x = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        if(this.posCurChunk.z > Universe.CHUNK_SIZE / 2) {
            this.chunkZ++;
            this.chunkZLast++;
            this.posCurChunk.z = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.z < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkZ--;
            this.chunkZLast--;
            this.posCurChunk.z = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + this.name + " entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        this.app.getUniverse().changedChunkForEntity(this, this.chunkXLast, this.chunkZLast);
        
        // check universe if we discovered a new chunk
        if(this.discoveredNewChunk(this.chunkX, this.chunkZ))
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + this.name + " discovered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
    }
    
    private boolean discoveredNewChunk(int chunkX, int chunkZ) {
        if(this.app.getUniverse().getChunk(chunkX, chunkZ).visited == true)
            return false;
        
        this.app.getUniverse().getChunk(chunkX, chunkZ).visited = true;
        return true;
    }

    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Vector3f getPosCurChunk() {return this.posCurChunk;}
    public int getChunkX() {return this.chunkX;}
    public int getChunkZ() {return this.chunkZ;}
    public ChunkNodeType getType(){return this.type;}
}
