/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.universe;

import java.util.ArrayList;

import netserver.universe.Abs_ChunkNode;
import netserver.universe.Abs_ChunkNode.ChunkNodeType;

/**
 *
 * @author Benjamin
 */
public class UniverseChunk {
	
	/**********************************
     ********** CLASS FIELDS  *********
     **********************************/
	
    public ArrayList<Abs_ChunkNode> universeEntities;   // solar sys, planets,..
    public ArrayList<Abs_ChunkNode> shipEntities;       // all "working" ships
    public ArrayList<Abs_ChunkNode> debrisEntities;     // destroyed ship parts
    public ArrayList<Abs_ChunkNode> asteroidEntities;   // asteroids
    
    public final int coordsX;
    public final int coordsZ;

    public Boolean visited = false;
    
    
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public UniverseChunk(int x, int z) {
        this.universeEntities = new ArrayList<Abs_ChunkNode>();
        this.shipEntities = new ArrayList<Abs_ChunkNode>();
        this.debrisEntities = new ArrayList<Abs_ChunkNode>();
        this.asteroidEntities = new ArrayList<Abs_ChunkNode>();
        
        this.coordsX = x;
        this.coordsZ = z;
    }
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
     public void update(float tpf) {
    	for(Abs_ChunkNode n : this.shipEntities)
    		n.update(tpf);
    	for(Abs_ChunkNode n : this.debrisEntities)
    		n.update(tpf);
    	for(Abs_ChunkNode n : this.asteroidEntities)
    		n.update(tpf);
    	//for(Abs_ChunkNode n : this.universeEntities)
    	//	n.update(tpf);
    }
    
    public void addShipEntity(Abs_ChunkNode n) {
        this.shipEntities.add(n);
    }
    
    public void removeShipEntity(Abs_ChunkNode n) {
        this.shipEntities.remove(n);
    }
    
    public void addUniverseEntity(Abs_ChunkNode n) {
        this.universeEntities.add(n);
    }
    
    public void removeUniverseEntity(Abs_ChunkNode n) {
        this.universeEntities.remove(n);
    }
    
    public void addDebrisEntitiy(Abs_ChunkNode n) {
        this.debrisEntities.add(n);
    }
    
    public void removeDebrisEntitiy(Abs_ChunkNode n) {
        this.debrisEntities.remove(n);
    }
    
    public void addAsteroidEntitiy(Abs_ChunkNode n) {
        this.asteroidEntities.add(n);
    }
    
    public void removeAsteroidEntitiy(Abs_ChunkNode n) {
        this.asteroidEntities.remove(n);
    }
    
    
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public ArrayList<Abs_ChunkNode> getListOfType(Abs_ChunkNode.ChunkNodeType type) {
    	if(type == ChunkNodeType.Ship) {return this.shipEntities;}
        else if(type == ChunkNodeType.Debris) {return this.debrisEntities;}
        else if(type == ChunkNodeType.Asteroid) {return this.asteroidEntities;}
        else if(type == ChunkNodeType.Universe) {return this.universeEntities;}
        else {System.out.println("ChunkNode was not assigned an EntityType.");return null;}
    }
}