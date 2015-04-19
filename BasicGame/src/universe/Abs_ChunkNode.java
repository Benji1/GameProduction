package universe;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;

/**
 * ChunkNode extends Node and adds functionality to use the entities
 * within our universe system, which is divided into chunks.
 * @author 
 */
public abstract class Abs_ChunkNode extends Node {
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    private Main app;  // the main game class
    
    private int chunkX;            // the current chunkX
    private int chunkZ;            // the current chunkZ
    private Vector3f posCurChunk;   // the position within the current chunk
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Abs_ChunkNode(Main app) {
        // init node
        this.app = app;
        
        this.chunkX = Universe.UNIVERSE_SIZE / 2;
        this.chunkZ = Universe.UNIVERSE_SIZE / 2;
        this.posCurChunk = new Vector3f(0, 0, 0);
        
        // add to universe
        this.app.getUniverse().addNewGameEntity(this);
    }
    
    public void update(float tpf) {
        this.app.textShipPos.setText("PosChunk: " + this.chunkX + "/" + this.chunkZ + ". PosCurChunk: " + this.posCurChunk.toString() + ". PosAbs: " + this.getWorldTranslation().toString());
    }
    
    @Override
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
    
    private void recalcChunkPos() {
        if(this.posCurChunk.x > Universe.CHUNK_SIZE / 2) {
            this.chunkX++;
            this.posCurChunk.x = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.x < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkX--;
            this.posCurChunk.x = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        if(this.posCurChunk.z > Universe.CHUNK_SIZE / 2) {
            this.chunkZ++;
            this.posCurChunk.z = -(Universe.CHUNK_SIZE / 2);
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        } else if(this.posCurChunk.z < -(Universe.CHUNK_SIZE / 2)) {
            this.chunkZ--;
            this.posCurChunk.z = Universe.CHUNK_SIZE / 2;
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Entered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
        }
        
        // check universe if we discovered a new chunk
        if(this.app.getUniverse().enterNewChunk(this.chunkX, this.chunkZ))
            this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "Discovered Chunk: " + this.chunkX + "/" + this.chunkZ + "\n");
    }
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public Vector3f getPosCurChunk() {return this.posCurChunk;}
    public long getChunkX() {return this.chunkX;}
    public long getChunkZ() {return this.chunkZ;}
}
