package universe;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import mygame.Main;

/**
 * ChunkNode extends Node and adds functionality to use the entities
 * within our universe system, which is divided into chunks.
 * @author 
 */
public abstract class Abs_ChunkNode extends Node {
	/**********************************
     ************ GLOBALS  ************
     **********************************/
	
	public enum ChunkNodeType { Ship, Debris, Asteroid, Universe }
	
	
	
	
	
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
    protected Main app;  				// the main game class
    private ChunkNodeType type;		// the type of the Node
    private int chunkX;            	// the current chunkX
    private int chunkZ;            	// the current chunkZ
    private int chunkXLast;			// the last chunkX
    private int chunkZLast;			// the last chunkZ
    private Vector3f posCurChunk;   // the position within the current chunk
    private Vector3f lastAbsPos;
    
    
    
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Abs_ChunkNode(Main app, String name, ChunkNodeType t, int chunkX, int chunkZ, Vector3f posInChunk) {
    	super(name);
    	
        this.app = app;
        this.type = t;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkXLast = chunkX;
        this.chunkZLast = chunkZ;
        this.posCurChunk = new Vector3f(posInChunk.x % (Universe.CHUNK_SIZE / 2), posInChunk.y, posInChunk.z % (Universe.CHUNK_SIZE / 2));
        this.lastAbsPos = this.getLocalTranslation();
        
        this.app.getUniverse().getChunk(chunkX, chunkZ).getListOfType(this.type).add(this);
    }
    
    public Abs_ChunkNode(Main app, String name, ChunkNodeType t, int chunkX, int chunkZ) {
        this(app, name, t, chunkX, chunkZ, Vector3f.ZERO);
    }
    
    public Abs_ChunkNode(Main app, String name, ChunkNodeType t) {
        this(app, name, t, 0, 0, Vector3f.ZERO);
    }
    
    protected Abs_ChunkNode() {}
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    public void update(float tpf) {
    	this.posCurChunk.set(this.getLocalTranslation().x % (this.chunkX * Universe.CHUNK_SIZE / 2f), this.posCurChunk.y, this.getLocalTranslation().z % (this.chunkZ * Universe.CHUNK_SIZE / 2f));
    	this.recalcChunkPos();
    }
    
    private void recalcChunkPos() {
    	/// TODO remove redundant code
    	this.chunkXLast = 0;
    	this.chunkZLast = 0;
        if(this.posCurChunk.x > Universe.CHUNK_SIZE / 2) {
            this.chunkX++;
            this.chunkXLast++;
            this.posCurChunk.x = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.x < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkX--;
            this.chunkXLast--;
            this.posCurChunk.x = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        if(this.posCurChunk.z > Universe.CHUNK_SIZE / 2) {
            this.chunkZ++;
            this.chunkZLast++;
            this.posCurChunk.z = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.z < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkZ--;
            this.chunkZLast--;
            this.posCurChunk.z = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        this.app.getUniverse().changedChunkForEntity(this, this.chunkXLast, this.chunkZLast);
        
        // check universe if we discovered a new chunk
        if(this.discoveredNewChunk(this.chunkX, this.chunkZ))
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Discovered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
    }
    
    private boolean discoveredNewChunk(int chunkX, int chunkZ) {
        if(this.app.getUniverse().getChunk(chunkX, chunkZ).visited == true)
            return false;
        
        this.app.getUniverse().getChunk(chunkX, chunkZ).visited = true;
        return true;
    }
    
    
    
    
    
    
    /**********************************
     **** OVERRIDE NODE & PHYSICS  ****
     **********************************/
    
    
    /*@Override
    public Spatial move(Vector3f offset) {
        this.posCurChunk.addLocal(offset);
        
        this.recalcChunkPos();
        super.move(offset);
        return this;
    }
    
    @Override
    public Spatial move(float x, float y, float z) {
        this.posCurChunk.x += x;
        this.posCurChunk.z += z;
        
        this.recalcChunkPos();
        super.move(x, y, z);
        return this;
    }
    */
    
    
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Vector3f getPosCurChunk() {return this.posCurChunk;}
    public int getChunkX() {return this.chunkX;}
    public int getChunkZ() {return this.chunkZ;}
    public ChunkNodeType getType(){return this.type;}
}
