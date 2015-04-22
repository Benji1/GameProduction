package universe;

import java.util.ArrayList;
import mygame.Main;

/**
 * The Main Universe Class
 * @author
 */
public class Universe {
    /**********************************
     ************ GLOBALS  ************
     **********************************/
    
    public static final float CHUNK_SIZE = 100;
    public static final int UNIVERSE_SIZE = 999;
    
    
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    
    private Main app;
    
    private ArrayList<Abs_ChunkNode> universeEntities;  // solar sys, planets,..
    private ArrayList<Abs_ChunkNode> gameEntities;      // asteroids, ships,...
    
    // PLACEHOLDERS FOR UNIVERSE STORAGE
    private boolean[][] universeChunks;                    
    private int universeCenter = UNIVERSE_SIZE / 2;
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Universe(Main app) {
        this.app = app;
        this.universeEntities = new ArrayList<Abs_ChunkNode>();
        this.gameEntities = new ArrayList<Abs_ChunkNode>();
        this.universeChunks = new boolean[UNIVERSE_SIZE][UNIVERSE_SIZE];
    }
    
    public boolean enterNewChunk(int chunkX, int chunkZ) {
        // Entered Chunk is not new
        if(this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter] == true)
            return false;
        
        // Entered Chunk is new
        this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter] = true;
        return true;
    }
    
    public void addNewGameEntity(Abs_ChunkNode n) {
        this.gameEntities.add(n);
    }
}
