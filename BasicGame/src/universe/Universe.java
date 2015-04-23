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
    
    // PLACEHOLDERS FOR UNIVERSE STORAGE
    private UniverseChunk[][] universeChunks;
    private int universeCenter = UNIVERSE_SIZE / 2;
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Universe(Main app) {
        this.app = app;
        
        this.universeChunks = new UniverseChunk[UNIVERSE_SIZE][UNIVERSE_SIZE];
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j] = new UniverseChunk();
            }
        }
    }
    
    public boolean enterNewChunk(int chunkX, int chunkZ) {
        // Check if Chunk new
        if(this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter].visited == true)
            return false;
        
        this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter].visited = true;
        return true;
    }
    
    public void addNewGameEntity(Abs_ChunkNode n, int chunkX, int chunkZ) {
        this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter].addGameEntity(n);
    }
}