/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import netclient.WJSFClient;
import org.jbox2d.common.Vec2;

public class GLaserProjectile extends GraphicObject {   
    
    private AudioNode fire_sound;
    private Spatial spatial;
    private Material material;
    private WJSFClient app;
    
    public GLaserProjectile(Vec2 spawnPoint, Vec2 fireDirection, WJSFClient app) {
        this.app = app;    
        
        playFireSound();
        createBox(spawnPoint, fireDirection);   
        
        app.gameRunState.localRootNode.attachChild(this);
    }
    
    private void createBox(Vec2 spawnPoint, Vec2 fireDirection) {
        Box box = new Box(0.9f, 0.1f, 0.1f);
        spatial = new Geometry("Box", box);
        material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", ColorRGBA.Red);
        material.setColor("Diffuse", ColorRGBA.Red);

        spatial.setMaterial(material);       
        
        //spatial.setLocalTranslation(new Vector3f(spawnPoint.x, 0, spawnPoint.y));
        this.setLocalTranslation(new Vector3f(spawnPoint.x, 0, spawnPoint.y));        
        
        Quaternion q = new Quaternion();        
        q.fromAngleAxis(-(float) Math.atan2(fireDirection.y, fireDirection.x), new Vector3f(0f, 1f, 0f));
        //spatial.setLocalRotation(q);
        this.setLocalRotation(q);    
        
        this.attachChild(spatial);
    }
    
    private void playFireSound() {
        fire_sound = new AudioNode(app.getAssetManager(), "Sound/Effects/lasershot2.ogg", false);
        fire_sound.setPositional(false);
        fire_sound.setLooping(false);
        fire_sound.setVolume(1);
        this.attachChild(fire_sound);
        
        fire_sound.setPitch((float) Math.random() * 0.1f + 0.95f);
        fire_sound.playInstance();
    }

    @Override
    public void delete() {
        spatial.removeFromParent();        
        fire_sound.removeFromParent();
        this.removeFromParent();
    }
    
}
