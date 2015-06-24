package netserver.universe;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.services.ServiceManager;
import netserver.services.config.ConfigReader;
import netserver.universe.Abs_ChunkNode;
import netserver.universe.SolarSystem;
import netserver.universe.Universe;
import netserver.universe.UniverseChunk;
import netserver.universe.Abs_ChunkNode.ChunkNodeType;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * The Main Universe Class
 * @author
 */
public class Universe {
    /**********************************
     ************ GLOBALS  ************
     **********************************/

    public static final float CHUNK_SIZE = ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("UniverseConfig"), "ChunkSize", float.class);
    public static final int UNIVERSE_SIZE = ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("UniverseConfig"), "UniverseSize", int.class);
    public static final float Y_LAYER_SHIPS = ServiceManager.getConfigReader().getFromMap((Map) ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("UniverseConfig"), "YLayers", Map.class), "ShipLayer", float.class);
    public static final float Y_LAYER_UNIVERSE = ServiceManager.getConfigReader().getFromMap((Map) ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("UniverseConfig"), "YLayers", Map.class), "UniverseLayer", float.class);
    
    
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    
    private WJSFServer app;
    
    // PLACEHOLDERS FOR UNIVERSE STORAGE
    private UniverseChunk[][] universeChunks;
    //public List<SolarSystem> systems;
    private int universeCenter = UNIVERSE_SIZE / 2;
    
    // Debug Stuff
    private Node debugBoxes;
    private boolean isDebug = false;
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Universe(WJSFServer app) {
        this.app = app;
        
        this.universeChunks = new UniverseChunk[UNIVERSE_SIZE][UNIVERSE_SIZE];
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j] = new UniverseChunk(i - this.universeCenter, j - this.universeCenter);
            }
        }
        
        //this.initDebug();
        //this.systems = new ArrayList<SolarSystem>();
        //this.stations = new ArrayList<Spatial>();
    }
    
    
    
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    private void initDebug() {
    	this.debugBoxes = new Node("DebugBoxes");
        
        // testbox
        Box box1 = new Box(Universe.CHUNK_SIZE / 2, 0.1f, Universe.CHUNK_SIZE / 2);
        
        Material mat0 = new Material(this.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat0.setColor("Diffuse", ColorRGBA.Red);
        mat0.setColor("Specular", ColorRGBA.Red);
        mat0.setBoolean("UseMaterialColors", true);

        Material mat1 = new Material(this.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat1.setColor("Diffuse", ColorRGBA.Blue);
        mat1.setColor("Specular", ColorRGBA.Blue);
        mat1.setBoolean("UseMaterialColors", true);
        
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
            	Geometry blue = new Geometry("Box"+i+j, box1);
                if(i == 0 && j == 0)
                	blue.setMaterial(mat0);
                else
                	blue.setMaterial(mat1);
                blue.setLocalTranslation(Universe.CHUNK_SIZE * i, -10, Universe.CHUNK_SIZE * j);
                this.debugBoxes.attachChild(blue);
                
                BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
                BitmapText ch = new BitmapText(f, false);
                ch.setSize(3);
                //ch.setText("NumShips: " + this.universeChunks[this.universeCenter][this.universeCenter].getListOfType(ChunkNodeType.Ship).size());
                ch.setColor(ColorRGBA.White);
                ch.rotate(-1.57079633f,0,0);
                ch.setLocalTranslation(Universe.CHUNK_SIZE * i, 1, Universe.CHUNK_SIZE * j);
                this.debugBoxes.attachChild(ch);
            }
        }
    }
    
    public boolean nearStation(Vector3f shippos){
    	float mindis = Float.MAX_VALUE;
    	for (Abs_ChunkNode s: this.getChunk(shippos).spaceStations){
    		float dis = (s.getLocalTranslation().subtract(shippos)).length();

    		if (dis < mindis)
    			mindis = dis;
    	}
    	return mindis < 15;
    }
    
    
    public void update(float tpf) {
        
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j].update(tpf);
            }
        }
    	//for (SolarSystem s: systems)
    	//	s.update(tpf);
        
    	//if(this.isDebug)
    	//	this.app.gameRunState.textShipPos.setText("PosChunk: " + this.app.gameRunState.playersShip.getChunkX() + "/" + this.app.gameRunState.playersShip.getChunkX() + "\nPosCurChunk: " + this.app.gameRunState.playersShip.getPosCurChunk().toString() + "\nPosAbs: " + this.app.gameRunState.playersShip.getWorldTranslation().toString());
    }
    
    public void changedChunkForEntity(Abs_ChunkNode n, int movedX, int movedZ) {
		if(movedX != 0 || movedZ != 0) {
			this.universeChunks[n.getChunkX() + this.universeCenter - movedX][n.getChunkZ() + this.universeCenter - movedZ].getListOfType(n.getType()).remove(n);
			this.universeChunks[n.getChunkX() + this.universeCenter][n.getChunkZ() + this.universeCenter].getListOfType(n.getType()).add(n);
		}
    }
    
    
    
    /**********************************
     ******** GETTER & SETTER  ********
     **********************************/
    
    public UniverseChunk getChunk(int chunkX, int chunkZ) {
    	if(chunkX < -universeCenter || chunkX > universeCenter || chunkZ < -universeCenter || chunkZ > universeCenter)
    		return null;
    	
    	return this.universeChunks[chunkX + this.universeCenter][chunkZ + this.universeCenter];
    }
    
    public UniverseChunk getChunk(Vector3f pos) {
    	return this.universeChunks[(int)(((pos.x - (this.CHUNK_SIZE / 2)) / this.CHUNK_SIZE)) + this.universeCenter][(int)(((pos.z - (this.CHUNK_SIZE / 2)) / this.CHUNK_SIZE)) + this.universeCenter];
    }
    
    public ArrayList<UniverseChunk> getAllSurroundingChunks(int chunkX, int chunkZ) {
    	ArrayList<UniverseChunk> chunks = new ArrayList<UniverseChunk>();
    	
    	for(int i = -1; i < 3; i++) {
    		for (int j = -1; j < 3; j++) {
    			chunks.add(this.getChunk(chunkX + i, chunkZ + j));
    		}
    	}
    	
    	return chunks;
    }
    
    public ArrayList<SpaceStation> getAllNearbyStations(Vector3f pos) {
    	return this.getAllNearbyStations((int)(pos.x / Universe.CHUNK_SIZE), (int)(pos.z / Universe.CHUNK_SIZE));
    }
    
    public ArrayList<SpaceStation> getAllNearbyStations(int chunkX, int chunkZ) {
    	ArrayList<SpaceStation> stations = new ArrayList<SpaceStation>();
    	
    	for(UniverseChunk c : this.getAllSurroundingChunks(chunkX, chunkZ))
    		for(Abs_ChunkNode s : c.getListOfType(ChunkNodeType.SpaceStations))
    			stations.add((SpaceStation)s);
    	
    	return stations;
    }
    
    public void broadcastUniverseTo(HostedConnection player){
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j].broadcastChunkTo(player);
            }
        }
    }
    
    public void toggleUniverseDebug() {
    	this.isDebug = !this.isDebug;
    	
    	/*if(this.isDebug)
    		this.app.gameRunState.localRootNode.attachChild(this.debugBoxes);
    	else
    		this.app.gameRunState.localRootNode.detachChild(this.debugBoxes);*/
    }
}