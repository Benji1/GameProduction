/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import com.jme3.scene.Node;
import mygame.Main;
import mygame.Updateable;
import org.jbox2d.common.Vec2;

public abstract class Projectile extends Node implements Updateable {
    
    protected float speed;
    protected float lifetime;       // in seconds
    protected float lifetimeCounter;
    
    protected Main app;
    protected Vec2 direction;
    
    public Projectile(Vec2 spawnPoint, Vec2 fireDirection, Main app) {
        this.app = app;
        this.direction = fireDirection;
        
        lifetimeCounter = 0;      
        app.addUpdateable(this);
    }
        
    public void update(float tpf) {
        updateLifetime(tpf);
    }
    
    protected void updateLifetime(float delta) {
        lifetimeCounter += delta;
        if (lifetimeCounter >= lifetime) {
            die();
        }
    }
    
    protected void die() {
        app.removeUpdateable(this);
    }
    
}
