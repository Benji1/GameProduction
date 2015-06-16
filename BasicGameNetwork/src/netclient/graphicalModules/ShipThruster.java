/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class ShipThruster extends ShipModule{
    
    public ParticleEmitter fire;
    
    public ShipThruster(OrientedModule orientedModule, float x, float y, Node shipRoot, ClientShip ship, WJSFClient app) {
        super(orientedModule, x, y, shipRoot, ship, app);
        
        AssetManager a = app.getAssetManager(); 
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 60);
        Material mat_red = new Material(a, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", a.loadTexture("textures/flame.jpg"));
        fire.setMaterial(mat_red);        
        //fire.setImagesX(2); fire.setImagesY(2); // 2x2 texture animation
        fire.setStartColor(  new ColorRGBA(0.6f, 0.9f, 1f, 0.6f));
        fire.setEndColor(new ColorRGBA(0.1f, 0f, 0f, 0f));
        //fire.setStartSize(1f + (float)Math.random());
        fire.setStartSize(0.5f);
        fire.setEndSize(5f);
        fire.setGravity(0,0,0);
        fire.setLowLife(0.5f);
        fire.setHighLife(1.2f);
        fire.setRandomAngle(true);
        fire.getParticleInfluencer().setVelocityVariation(0.1f);
        fire.setParticlesPerSec(0f);
        
        gm.attachChild(fire);
    }
    
     @Override
    public void activate() {
        super.activate();   
        ship.increaseActivatedThrusterCount();        
        fire.setParticlesPerSec(60f);
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        ship.decreaseActivatedThrusterCount();
        fire.setParticlesPerSec(0f);
    }
    
    @Override
    public void update() {
        super.update();
        fire.getParticleInfluencer().setInitialVelocity(getParticleSpawnDirection(5f));
    }
    
    public final Vector3f getParticleSpawnDirection(float initialVelocity) {           
        float angle = ship.shipRoot.getLocalRotation().toAngleAxis(new Vector3f(0, 1, 0));
        float x = (float)Math.cos(angle + 90f);        
        float y = (float)Math.sin(angle + 90f);

        Vector3f forward = new Vector3f(initialVelocity * x, 0 ,initialVelocity * y);
        return forward;
    }
}
