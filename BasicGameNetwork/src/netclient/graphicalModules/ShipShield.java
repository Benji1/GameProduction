/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

public class ShipShield extends ShipModule {
    
    private ColorRGBA color = new ColorRGBA(0f, 0.5f, 0.5f, 0.3f);
    private Spatial spatial;
    private Material material;
    private float shieldRadius = 5;
    private WJSFClient app;
    
    public ShipShield(OrientedModule orientedModule, float x, float y, Node shipRoot, ClientShip ship, WJSFClient app) {
        super(orientedModule, x, y, shipRoot, ship, app);
        
        this.app = app;
        
        createShield();
    }
    
    private void createShield() {
        Sphere sphere = new Sphere(32, 32, shieldRadius);
        spatial = new Geometry("Sphere", sphere);
        material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        spatial.setQueueBucket(RenderQueue.Bucket.Transparent);

        spatial.setMaterial(material);        
    }
    
    @Override
    public void activate() {
        super.activate();   
        gm.attachChild(spatial);
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        spatial.removeFromParent();
    }
    
}
