/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.universe;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import netserver.WJSFServer;
import netserver.services.ServiceManager;
import netutil.NetMessages;
import netutil.NetMessages.SpawnSpaceStationMsg;
import org.jbox2d.common.Vec2;

public class SpaceStation {
    
    private Spatial station;
    private int id;
    private float x;
    private float z;
    private WJSFServer app;
    
    public SpaceStation(float x, float z, WJSFServer app) {
        this.app = app;
        this.x = x;
        this.z = z;
        
        id = ServiceManager.getIdProvider().getFreeId();
        
        createStation();
        
        // network spawn msg
        SpawnSpaceStationMsg msg = new SpawnSpaceStationMsg(id, new Vec2(x, z));
        msg.setReliable(true);
        app.getServer().broadcast(msg);
    }
    
    private void createStation() {
        Box shape = new Box(7, 2, 5);
    	station = app.getAssetManager().loadModel("3dmodels/station.obj");
        Material sphereMat = new Material(app.getAssetManager(), 
                        "Common/MatDefs/Light/Lighting.j3md");
        sphereMat.setBoolean("UseMaterialColors", true);

        ColorRGBA color = ColorRGBA.DarkGray;
        sphereMat.setColor("Diffuse", color);
        sphereMat.setColor("Ambient", color);
        station.setMaterial(sphereMat);	
        app.getRootNode().attachChild(station);
        station.setLocalTranslation(x, -5, z);
        station.setLocalScale(2f);
                
        BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText info = new BitmapText(f, true);
        info.setColor(ColorRGBA.Green);
        info.rotate((float) -Math.PI/2f,0,0);
        info.scale(0.2f);
        info.setQueueBucket(RenderQueue.Bucket.Transparent);
        info.setText("Press 'E' to enter");
        info.setLocalTranslation(x-13, 3, z);
        app.getRootNode().attachChild(info);
    }
    
    public Vector3f getPosition() {
        return station.getLocalTranslation();
    }
    
    public int getId() {
        return id;
    }
    
    public Vec2 getSpawnPoint() {
        return new Vec2(x, z);
    }
}
