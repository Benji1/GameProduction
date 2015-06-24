/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.universe;

import java.util.ArrayList;

import com.jme3.network.HostedConnection;
import com.jme3.scene.Node;

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
	
    public ArrayList<Abs_ChunkNode> solarSystems;
    public ArrayList<Abs_ChunkNode> spaceStations;
    //public ArrayList<Abs_ChunkNode> shipEntities;       // all "working" ships
    //public ArrayList<Abs_ChunkNode> debrisEntities;     // destroyed ship parts
    //public ArrayList<Abs_ChunkNode> asteroidEntities;   // asteroids
    
    public final int coordsX;
    public final int coordsZ;

    public Boolean visited = false;
    
    
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public UniverseChunk(int x, int z) {
        this.solarSystems = new ArrayList<Abs_ChunkNode>();
        this.spaceStations = new ArrayList<Abs_ChunkNode>();
        //this.shipEntities = new ArrayList<Abs_ChunkNode>();
        //this.debrisEntities = new ArrayList<Abs_ChunkNode>();
        //this.asteroidEntities = new ArrayList<Abs_ChunkNode>();
        
        this.coordsX = x;
        this.coordsZ = z;
    }
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
     public void update(float tpf) {
    	/*for(Abs_ChunkNode n : this.shipEntities)
    		n.update(tpf);
    	for(Abs_ChunkNode n : this.debrisEntities)
    		n.update(tpf);
    	for(Abs_ChunkNode n : this.asteroidEntities)
    		n.update(tpf);*/
    	for(Abs_ChunkNode n : this.solarSystems)
    		n.update(tpf);
    	for(int i = 0; i < this.spaceStations.size(); i++)
    		this.spaceStations.get(i).update(tpf);
    }
    
    public void addUniverseEntity(Abs_ChunkNode n) {
        this.solarSystems.add(n);
    }
    
    public void removeUniverseEntity(Abs_ChunkNode n) {
        this.solarSystems.remove(n);
    }
    
    public void broadcastChunkTo(HostedConnection player){
    	for(Abs_ChunkNode n : this.solarSystems){
    		((SolarSystem)n).broadcastSpawnTo(player);
    	}
    }
    
    
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public ArrayList<Abs_ChunkNode> getListOfType(Abs_ChunkNode.ChunkNodeType type) {
    	//if(type == ChunkNodeType.Ship) {return this.shipEntities;}
        //if(type == ChunkNodeType.Debris) {return this.debrisEntities;}
        //else if(type == ChunkNodeType.Asteroid) {return this.asteroidEntities;}
        if(type == ChunkNodeType.SolarSystems) {return this.solarSystems;}
        else if(type == ChunkNodeType.SpaceStations) {return this.spaceStations;}
        else {System.out.println("ChunkNode was not assigned an EntityType.");return null;}
    }
}