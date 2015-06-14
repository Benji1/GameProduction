/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import netclient.gui.ModuleType;

import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);
    ParticleEmitter fire;
    
    public Thruster(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys);
        moduleName = "Thruster";
        energyConsumptionPerSecond = cr.getFromMap(cr.getBaseMap("Thruster"), "EnergyConsumptionPerSecond", float.class);

        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;

        orientation = orientationDirection;
        type = ModuleType.THRUSTER;
    }

    protected void onActive() {
        Vec2 forceDirection = body.getWorldVector(FacingDirection.getDirectionVector(orientation)).mul(forceMagnitude);
        body.applyForce(forceDirection, body.getPosition());
    }
   
    @Override
    public void update(float delta) {
        super.update(delta);
        fire.setLocalTranslation(this.body.getPosition().x, 0, this.body.getPosition().y);
    }
    
    @Override
    public void activate() {
        super.activate();
        fire.emitAllParticles();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        fire.killAllParticles();
    }


    
    @Override
    protected void create3DBody() {
        super.create3DBody();
        AssetManager a = ship.getApp().getAssetManager();
        spatial = a.loadModel("3dmodels/thruster.obj");
        material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = a.loadTexture("3dmodels/thruster_ao.png");
        material.setTexture("DiffuseMap", t);
        
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-(float) Math.atan2(FacingDirection.getDirectionVector(orientation).x, -FacingDirection.getDirectionVector(orientation).y), new Vector3f(0, 1, 0));
        spatial.setLocalRotation(q);
        spatial.setMaterial(material);
        materialActive.setTexture("DiffuseMap", t);
        
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(a, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", a.loadTexture("textures/flame.jpg"));
        fire.setMaterial(mat_red);        
        fire.setImagesX(2); fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0,2,0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0,0,0);
        fire.setLowLife(0.5f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        ship.getApp().getRootNode().attachChild(fire);
    }
}
