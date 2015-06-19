/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.weapons;

import com.jme3.scene.Node;

import netclient.WJSFClient;
import netserver.WJSFServer;
import netserver.physics.JBox2dNode;
import netserver.services.ServiceManager;
import netserver.services.config.ConfigReader;
import netserver.services.updater.INetworkPosAndRotUpdateable;
import netserver.services.updater.IUpdateable;
import netutil.NetMessages;
import netutil.NetMessages.DeleteGraphicObjectMsg;
import netutil.NetMessages.SpawnLaserProjectileMsg;

import org.jbox2d.common.Vec2;

public abstract class Projectile extends JBox2dNode implements IUpdateable, INetworkPosAndRotUpdateable {
    
    protected float startForce;
    protected float lifetime;       // in seconds
    protected float lifetimeCounter;
    
    protected WJSFServer app;
    protected Vec2 direction;
    
    protected int id;
    
    ConfigReader cr = ServiceManager.getConfigReader();
    
    public Projectile(Vec2 spawnPoint, Vec2 fireDirection, WJSFServer app) {
        super();
        this.app = app;
        this.direction = fireDirection;
        
        lifetimeCounter = 0;      
        ServiceManager.getUpdateableManager().addUpdateable(this);        
        
        id = ServiceManager.getIdProvider().getFreeId();
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
        DeleteGraphicObjectMsg msg = new NetMessages.DeleteGraphicObjectMsg(id);
        msg.setReliable(true);
        app.getServer().broadcast(msg);
        ServiceManager.getUpdateableManager().removeUpdateable(this);
        ServiceManager.getUpdateableManager().removeNetworkUpdateable(this);
    }
}
