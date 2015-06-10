/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import mygame.JBox2dNode;
import mygame.Main;
import services.updater.IUpdateable;
import org.jbox2d.common.Vec2;
import services.ServiceManager;
import services.config.ConfigReader;

public abstract class Projectile extends JBox2dNode implements IUpdateable {
    
    protected float startForce;
    protected float lifetime;       // in seconds
    protected float lifetimeCounter;
    
    protected Main app;
    protected Vec2 direction;
    
    ConfigReader cr = ServiceManager.getConfigReader();
    
     protected boolean beDead;
    
    public Projectile(Vec2 spawnPoint, Vec2 fireDirection, Main app) {
        super();
        this.app = app;
        this.direction = fireDirection;
        
        lifetimeCounter = 0;      
        ServiceManager.getUpdateableManager().addUpdateable(this);
    }
        
    @Override
    public void update(float tpf) {
        super.update(tpf);
        updateLifetime(tpf);
    }
    
    protected void updateLifetime(float delta) {
        lifetimeCounter += delta;
        if (lifetimeCounter >= lifetime) {
            markForDeletion();
        }
    }
    
    protected void markForDeletion() {
        app.projectilesToRemove.add(this);
    }
    
    public void delete() {
        ServiceManager.getUpdateableManager().removeUpdateable(this);
    }
}
