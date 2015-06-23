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
	
	public enum ChunkNodeType { SolarSystems, SpaceStations }
	
	
	
	
	
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
    protected WJSFServer app;  				// the main game class
    private ChunkNodeType type;		// the type of the Node
    private int chunkX;            	// the current chunkX
    private int chunkZ;            	// the current chunkZ
    private int chunkXLast;			// the last chunkX
    private int chunkZLast;			// the last chunkZ
    private Vector3f posCurChunk;   // the position within the current chunk
    private Vector3f lastAbsPos;
    
    private boolean isStatic;
    
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Abs_ChunkNode(WJSFServer app, String name, ChunkNodeType t, boolean isStatic, int chunkX, int chunkZ, Vector3f posInChunk) {
    	super(name);
    	System.out.println(name + ": " + chunkX + "/" + chunkZ + ", " + posInChunk.toString());
        this.app = app;
        this.type = t;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkXLast = chunkX;
        this.chunkZLast = chunkZ;
        this.posCurChunk = posInChunk;
        this.lastAbsPos = new Vector3f(posInChunk.x + chunkX * Universe.CHUNK_SIZE, posInChunk.y, posInChunk.z + chunkZ * Universe.CHUNK_SIZE);
        this.setLocalTranslation(this.lastAbsPos);
        this.app.getUniverse().getChunk(chunkX, chunkZ).getListOfType(this.type).add(this);
        this.app.getRootNode().attachChild(this);
    }
    
    public Abs_ChunkNode(WJSFServer app, String name, ChunkNodeType t, boolean isStatic, Vector3f posAbsolute) {
    	this(app, name, t, isStatic,
    			(int)(posAbsolute.x / Universe.CHUNK_SIZE), (int)(posAbsolute.z / Universe.CHUNK_SIZE),
    			new Vector3f(posAbsolute.x - (int)(posAbsolute.x / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE, posAbsolute.y, posAbsolute.z - (int)(posAbsolute.z / Universe.CHUNK_SIZE) * Universe.CHUNK_SIZE));
    }
    
    protected Abs_ChunkNode() {}
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public void update(float tpf) {
    	if(!this.isStatic) {
	    	this.posCurChunk = new Vector3f((Universe.CHUNK_SIZE / 2) - this.getLocalTranslation().x - this.chunkX * Universe.CHUNK_SIZE, this.posCurChunk.y, (Universe.CHUNK_SIZE / 2) - this.getLocalTranslation().z - this.chunkZ * Universe.CHUNK_SIZE);
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
            //this.app.gameRunState.textNewChunk.setText(this.app.gameRunState.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.x < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkX--;
            this.chunkXLast--;
            this.posCurChunk.x = Universe.CHUNK_SIZE / 2;
            //this.app.gameRunState.textNewChunk.setText(this.app.gameRunState.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        if(this.posCurChunk.z > Universe.CHUNK_SIZE / 2) {
            this.chunkZ++;
            this.chunkZLast++;
            this.posCurChunk.z = -(Universe.CHUNK_SIZE / 2);
            //this.app.gameRunState.textNewChunk.setText(this.app.gameRunState.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.z < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkZ--;
            this.chunkZLast--;
            this.posCurChunk.z = Universe.CHUNK_SIZE / 2;
            //this.app.gameRunState.textNewChunk.setText(this.app.gameRunState.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        this.app.getUniverse().changedChunkForEntity(this, this.chunkXLast, this.chunkZLast);
        
        // check universe if we discovered a new chunk
        this.discoveredNewChunk(this.chunkX, this.chunkZ);
        //    this.app.gameRunState.textNewChunk.setText(this.app.gameRunState.textNewChunk.getText() + "Discovered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
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
