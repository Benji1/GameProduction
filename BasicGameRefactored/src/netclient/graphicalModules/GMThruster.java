/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GMThruster extends GraphicalModule {
    
    ParticleEmitter fire;
    
    
    public GMThruster(OrientedModule orientedModule, Node shipRoot, ClientShip ship, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, ship, x, y, app);
        
        colorActive = ColorRGBA.Orange;
        modelPath = "3dmodels/thruster.obj";
        texturePath = "3dmodels/thruster_ao.png";
        
        createMyGraphic(x, y);
    }
    
    @Override
    protected void createMyGraphic(float x, float y) {
        super.createMyGraphic(x, y);
        
        /*
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
        
        app.getRootNode().attachChild(fire);
        * */
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
    /*
    public void update() {
        fire.setLocalTranslation(this.body.getPosition().x, 0, this.body.getPosition().y);
        fire.getParticleInfluencer().setInitialVelocity(getParticleSpawnDirection(5f));
    }
    
    
    
    public final Vector3f getParticleSpawnDirection(float initialVelocity)
    {   
        if (getBody() == null)
            return new Vector3f(0f, 0f, 0f);
        
        float angle = this.getBody().getAngle();
        float x = (float)Math.cos(angle + 90f);        
        float y = (float)Math.sin(angle + 90f);

        Vector3f forward = new Vector3f(initialVelocity * x, 0 ,initialVelocity * y);
        return forward;
    }*/
    
}
