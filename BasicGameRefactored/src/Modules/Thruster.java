/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import gui.ModuleType;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);
    ParticleEmitter fire;
    private int keyPressedCounter = 0;

    public Thruster(ArrayList<Integer> keyCodes, FacingDirection orientationDirection) {
        super(keyCodes);
        moduleName = "Thruster";
        energyConsumptionPerSecond = cr.getFromMap(cr.getBaseMap("Thruster"), "EnergyConsumptionPerSecond", float.class);

        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;

        orientation = orientationDirection;
        type = ModuleType.THRUSTER;
    }

    protected void onActive() {
        Vec2 forceDirection = ship.getBody().getWorldVector(FacingDirection.getDirectionVector(orientation)).mul(forceMagnitude);        
        Vec2 pos = new Vec2(ship.getActualPositionInGrid(this).x, ship.getActualPositionInGrid(this).y);
        ship.getBody().applyForce(forceDirection, pos);// ship.getBody().getPosition());
    }
   
    @Override
    public void update(float delta) {
        super.update(delta);
        //fire.setLocalTranslation(this.body.getPosition().x, 0, this.body.getPosition().y);
        fire.setLocalTranslation(ship.getActualPositionInGrid(this).x*2, 0, ship.getActualPositionInGrid(this).y*2);
        fire.getParticleInfluencer().setInitialVelocity(getParticleSpawnDirection(5f));
    }
    
    @Override
    public void activate() {
        super.activate();
        fire.setParticlesPerSec(60f);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        fire.setParticlesPerSec(0f);
    }


    
    @Override
    protected void create3DBody() {
        super.create3DBody();        
        AssetManager a = ship.getApp().getAssetManager();
        spatial = a.loadModel("3dmodels/thruster.obj");
        spatial.setLocalTranslation(ship.getActualPositionInGrid(this).x*2, 0, ship.getActualPositionInGrid(this).y*2);
        material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = a.loadTexture("3dmodels/thruster_ao.png");
        material.setTexture("DiffuseMap", t);
        spatial.setMaterial(material);
        materialActive.setTexture("DiffuseMap", t);
        
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-(float) Math.atan2(FacingDirection.getDirectionVector(orientation).x, -FacingDirection.getDirectionVector(orientation).y), new Vector3f(0, 1, 0));
        spatial.setLocalRotation(q);
        
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
        fire.setLocalTranslation(ship.getActualPositionInGrid(this).x*2, 0, ship.getActualPositionInGrid(this).y*2);

        ship.getApp().getRootNode().attachChild(fire);
        //System.out.println(this.);
    }
    
    public final Vector3f getParticleSpawnDirection(float initialVelocity)
    {   
        if (ship.getBody() == null)
            return new Vector3f(0f, 0f, 0f);
        
        float angle = this.ship.getBody().getAngle();
        float x = (float)Math.cos(angle + 90f);        
        float y = (float)Math.sin(angle + 90f);

        Vector3f forward = new Vector3f(initialVelocity * x, 0 ,initialVelocity * y);
        return forward;
    }

    @Override
    public void handleKeyPressed(Integer keyCode) {        
        if (keyCodes.contains(keyCode)) {
            keyPressedCounter++;    
            
            if (keyPressedCounter > 0 && !isActive()) {
                activate();
                ship.increaseActivatedThrusterCount();
            }
        }
    }

    @Override
    public void handleKeyReleased(Integer keyCode) {
        if (keyCodes.contains(keyCode)) {
            keyPressedCounter--;
            
            if (keyPressedCounter <= 0) {
                deactivate();
                ship.decreaseActivatedThrusterCount();
            }
        }
    }
}

