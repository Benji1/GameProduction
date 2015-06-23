package netserver.universe;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;

import netserver.WJSFServer;

public class SpaceStation extends Abs_ChunkNode {
	public SpaceStation(WJSFServer app, Vector3f pos) {
		super(app, "Station " + CBNameGenerator.getName(), ChunkNodeType.SpaceStations, true, pos);
		
		this.app.textNewChunk.setText(this.app.textNewChunk.getText() + "\nStation: " + this.getChunkX() + "/" + this.getChunkZ() + " - " + this.getLocalTranslation().toString() + " - " + this.getWorldTranslation().toString());
		
		Spatial station = app.getAssetManager().loadModel("3dmodels/station.obj");
		Material sphereMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		sphereMat.setBoolean("UseMaterialColors", true);
		
		ColorRGBA color = ColorRGBA.DarkGray;
		sphereMat.setColor("Diffuse", color);
		sphereMat.setColor("Ambient", color);
		station.setMaterial(sphereMat);	
		this.attachChild(station);
        station.setLocalScale(2f);
        station.setLocalTranslation(this.getLocalTranslation().x, this.getLocalTranslation().y, this.getLocalTranslation().z);
        
        BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText info = new BitmapText(f, true);
        info.setColor(ColorRGBA.Green);
        info.rotate((float) -Math.PI/2f,0,0);
        info.scale(0.2f);
        info.setQueueBucket(Bucket.Transparent);
        info.setText("Press 'E' to enter");
        info.setLocalTranslation(this.getLocalTranslation().x - 13, this.getLocalTranslation().y + 3, this.getLocalTranslation().z);
        this.attachChild(info);
	}
}
