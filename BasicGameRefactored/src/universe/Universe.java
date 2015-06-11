package universe;


import java.util.ArrayList;
import java.util.List;

import universe.Abs_ChunkNode.ChunkNodeType;

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
import com.jme3.scene.shape.Sphere;

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
    public List<SolarSystem> systems;
    public List<Spatial> stations;
    private int universeCenter = UNIVERSE_SIZE / 2;
    
    // Debug Stuff
    private Node debugBoxes;
    private boolean isDebug = false;
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public Universe(Main app) {
        this.app = app;
        
        this.universeChunks = new UniverseChunk[UNIVERSE_SIZE][UNIVERSE_SIZE];
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j] = new UniverseChunk(i - this.universeCenter, j - this.universeCenter);
            }
        }
        
        this.initDebug();
        this.systems = new ArrayList<SolarSystem>();
        this.stations = new ArrayList<Spatial>();
    }
    
    public void addStation(float x, float z){
    	//Box shape = new Box(7, 2, 5);
    	Spatial station = app.getAssetManager().loadModel("3dmodels/station.obj");	
		Material sphereMat = new Material(app.getAssetManager(), 
				"Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		
		ColorRGBA color = ColorRGBA.DarkGray;
		sphereMat.setColor("Diffuse", color);
		sphereMat.setColor("Ambient", color);
		station.setMaterial(sphereMat);	
		app.getRootNode().attachChild(station);
		this.stations.add(station);
		station.setLocalTranslation(x, -5, z);
		station.setLocalScale(2f);
		
        BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText info = new BitmapText(f, true);
        info.setColor(ColorRGBA.Green);
        info.rotate((float) -Math.PI/2f,0,0);
        info.scale(0.2f);
        info.setQueueBucket(Bucket.Transparent);
        info.setText("Press 'E' to enter");
        info.setLocalTranslation(x-13, 3, z);
        app.getRootNode().attachChild(info);
    }
    
    public boolean nearStation(Vector3f shippos){
    	float mindis = Float.MAX_VALUE;
    	for (Spatial s: stations){
    		float dis = (s.getLocalTranslation().subtract(shippos)).length();
    		if (dis < mindis)
    			mindis = dis;
    	}
    	return mindis < 15;
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
    
    public void update(float tpf) {
        
        for(int i = 0; i < UNIVERSE_SIZE; i++) {
            for(int j = 0; j < UNIVERSE_SIZE; j++) {
                this.universeChunks[i][j].update(tpf);
            }
        }
    	for (SolarSystem s: systems)
    		s.update(tpf);
        
    	if(this.isDebug)
    		this.app.textShipPos.setText("PosChunk: " + this.app.player.getShip().getChunkX() + "/" + this.app.player.getShip().getChunkX() + "\nPosCurChunk: " + this.app.player.getShip().getPosCurChunk().toString() + "\nPosAbs: " + this.app.player.getShip().getWorldTranslation().toString());
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
    	
    	if(this.isDebug)
    		this.app.getRootNode().attachChild(this.debugBoxes);
    	else
    		this.app.getRootNode().detachChild(this.debugBoxes);
    }
}