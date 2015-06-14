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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import netserver.physics.JBox2dNode;
/**
 *
 * @author Chris
 */
public class Explosion extends JBox2dNode {
    ParticleEmitter fire;
    float duration = 0.6f;
    float timeAlive = 0f;
    public Explosion(AssetManager a, Vector3f pos, Node root)
    {
        super();
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 60);
        Material mat_red = new Material(a, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", a.loadTexture("textures/flame.jpg"));
        fire.setMaterial(mat_red);        
        //fire.setImagesX(2); fire.setImagesY(2); // 2x2 texture animation
        fire.setStartColor(  new ColorRGBA(1f, 0.8f, 0.3f, 1f));
        fire.setEndColor(new ColorRGBA(1f, 0.1f, 0f, 0f));
        fire.setStartSize(1f + (float)Math.random());
        fire.setEndSize(3f);
        fire.setGravity(0,0,0);
        fire.setLowLife(0.3f);
        fire.setHighLife(duration);
        fire.setRandomAngle(true);
        fire.getParticleInfluencer().setVelocityVariation(0.1f);
        
        fire.setLocalTranslation(pos);
        fire.emitAllParticles();
        fire.setParticlesPerSec(0f);
        root.attachChild(fire);
    }
    
    // TODO dont know why this isn't beeing called. do i have to register somewhere?
    // FIXME this ways the object will never be garbage-collected...
    @Override
    public void update(float delta) {
        super.update(delta);
        timeAlive += delta;
        if (timeAlive > duration)
            fire.removeFromParent();
        System.out.println("ParticleSystem alive!");
    }
} 