/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import java.util.Map;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import netclient.WJSFClient;
import netserver.services.ServiceManager;

import org.jbox2d.common.Vec2;

public class GSpaceStation extends GraphicObject {
    
    private Spatial station;
    private BitmapText info;
    private WJSFClient app;
    
    public GSpaceStation(Vec2 spawnPoint, WJSFClient app) {
        this.app = app;
        
        createStation(spawnPoint.x, spawnPoint.y);
        
        app.gameRunState.localRootNode.attachChild(this);        
    }
    
    private void createStation(float x, float z) {
        this.setLocalTranslation(x, ServiceManager.getConfigReader().getFromMap((Map) ServiceManager.getConfigReader().getFromMap(ServiceManager.getConfigReader().getBaseMap("UniverseConfig"), "YLayers", Map.class), "UniverseLayer", float.class), z);
        
    	station = app.getAssetManager().loadModel("3dmodels/station.obj");
        Material sphereMat = new Material(app.getAssetManager(), 
                        "Common/MatDefs/Light/Lighting.j3md");
        sphereMat.setBoolean("UseMaterialColors", true);

        ColorRGBA color = ColorRGBA.Gray;
        sphereMat.setColor("Diffuse", color);
        sphereMat.setColor("Ambient", color);
        sphereMat.setTexture("DiffuseMap", app.getAssetManager().loadTexture("textures/station_tex.jpg"));
        station.setMaterial(sphereMat);	
        station.setLocalScale(1.5f);       
                
        BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        info = new BitmapText(f, true);
        info.setColor(ColorRGBA.Green);
        info.rotate((float) -Math.PI/2f,0,0);
        info.scale(0.2f);
        info.setQueueBucket(RenderQueue.Bucket.Transparent);
        info.setText("Press 'E' to enter");
        info.setLocalTranslation(-13, 3, 0);
        
        this.attachChild(station);
        this.attachChild(info);
    }

    @Override
    public void delete() {
        station.removeFromParent();
        info.removeFromParent();
        this.removeFromParent();
    }
    
}
