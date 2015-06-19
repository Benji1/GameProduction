package netserver.universe;


import java.util.ArrayList;
import java.util.List;

import netclient.WJSFClient;
import netserver.WJSFServer;
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
    
    public static final float CHUNK_SIZE = 100;
    public static final int UNIVERSE_SIZE = 999;
    
    
    
    
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    
    private WJSFServer app;
    
    // PLACEHOLDERS FOR UNIVERSE STORAGE
    private UniverseChunk[][] universeChunks;
    public List<SolarSystem> systems;
    private int universeCenter = UNIVERSE_SIZE / 2;
    public List<SpaceStation> stations;
    
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
        
        this.initDebug();
        this.systems = new ArrayList<SolarSystem>();
        this.stations = new ArrayList<SpaceStation>();
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
                ch.setText("NumShips: " + this.universeChunks[this.universeCenter][this.universeCenter].getListOfType(ChunkNodeType.Ship).size());
                ch.setColor(ColorRGBA.White);
                ch.rotate(-1.57079633f,0,0);
                ch.setLocalTranslation(Universe.CHUNK_SIZE * i, 1, Universe.CHUNK_SIZE * j);
                this.debugBoxes.attachChild(ch);
            }
        }
    }
    
    public void addStation(float x, float z){
        SpaceStation station = new SpaceStation(x, z, app);
        this.stations.add(station);
    }
    
    public boolean nearStation(Vector3f shippos){
    	float mindis = Float.MAX_VALUE;
    	for (SpaceStation s: stations){
    		float dis = (s.getPosition().subtract(shippos)).length();
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
    	for (SolarSystem s: systems)
    		s.update(tpf);
        
    	//if(this.isDebug)
    	//	this.app.gameRunState.textShipPos.setText("PosChunk: " + this.app.gameRunState.playersShip.getChunkX() + "/" + this.app.gameRunState.playersShip.getChunkX() + "\nPosCurChunk: " + this.app.gameRunState.playersShip.getPosCurChunk().toString() + "\nPosAbs: " + this.app.gameRunState.playersShip.getWorldTranslation().toString());
    }
    
    public void changedChunkForEntity(Abs_ChunkNode n, int movedX, int movedZ) {
		if(movedX != 0 || movedZ != 0) {
			this.universeChunks[n.getChunkX() + this.universeCenter - movedX][n.getChunkZ() + this.universeCenter - movedZ].getListOfType(n.getType()).remove(n);
			this.universeChunks[n.getChunkX() + this.universeCenter][n.getChunkZ() + this.universeCenter].getListOfType(n.getType()).add(n);
			
			if(this.isDebug && n.getName().equals("BasicShip")) {
				this.debugBoxes.setLocalTranslation(n.getChunkX() * CHUNK_SIZE, 0, n.getChunkZ() * CHUNK_SIZE);
			}
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
    
    public void toggleUniverseDebug() {
    	this.isDebug = !this.isDebug;
    	
    	/*if(this.isDebug)
    		this.app.gameRunState.localRootNode.attachChild(this.debugBoxes);
    	else
    		this.app.gameRunState.localRootNode.detachChild(this.debugBoxes);*/
    }
}