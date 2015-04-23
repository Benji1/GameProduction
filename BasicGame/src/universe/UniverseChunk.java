/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universe;

import java.util.ArrayList;

/**
 *
 * @author Benjamin
 */
public class UniverseChunk {
    public ArrayList<Abs_ChunkNode> universeEntities;  // solar sys, planets,..
    public ArrayList<Abs_ChunkNode> gameEntities;      // asteroids, ships,...
    
    public Boolean visited = false;
    
    public UniverseChunk() {
        this.universeEntities = new ArrayList<Abs_ChunkNode>();
        this.gameEntities = new ArrayList<Abs_ChunkNode>();
    }
    
    public void addGameEntity(Abs_ChunkNode n) {
        this.gameEntities.add(n);
    }
    
    public void removeGameEntity(Abs_ChunkNode n) {
        this.gameEntities.remove(n);
    }
    
    public void addUniverseEntity(Abs_ChunkNode n) {
        this.universeEntities.add(n);
    }
    
    public void removeUniverseEntity(Abs_ChunkNode n) {
        this.universeEntities.remove(n);
    }
}