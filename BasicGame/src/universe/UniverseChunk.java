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
    private ArrayList<Abs_ChunkNode> universeEntities;  // solar sys, planets,..
    private ArrayList<Abs_ChunkNode> gameEntities;      // asteroids, ships,...
    
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
}