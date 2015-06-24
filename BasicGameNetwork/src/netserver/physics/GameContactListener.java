/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.physics;

import netclient.gui.ModuleType;
import netserver.BasicShip;
import netserver.NetPlayer;
import netserver.WJSFServer;
import netserver.items.Item;
import netserver.modules.BasicModule;
import netserver.weapons.LaserProjectile;
import netserver.weapons.ShieldCollider;
import netutil.NetMessages;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author 1337
 */
public class GameContactListener implements ContactListener {

    private WJSFServer app;
    
    public GameContactListener(WJSFServer app) {
        this.app = app;
        PhysicsWorld.world.setContactListener(this);
    }

    public void beginContact(Contact cntct) {
        checkLaserProjectileModuleCollision(cntct);
        checkLaserProjectileShieldCollision(cntct);
        checkItemModuleCollision(cntct);
        checkItemSensor(cntct);
    }
    
    public void checkItemSensor(Contact cntct) {
        Item i = null;
        BasicModule m = null;

        if ((cntct.getFixtureA().isSensor() && cntct.getFixtureA().getBody().getUserData() instanceof Item) && cntct.getFixtureB().getBody().getUserData() instanceof BasicModule) {
            i = (Item) cntct.getFixtureA().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureB().getBody().getUserData();
        } else if ((cntct.getFixtureB().isSensor() && cntct.getFixtureB().getBody().getUserData() instanceof Item) && cntct.getFixtureA().getBody().getUserData() instanceof BasicModule) {
            i = (Item) cntct.getFixtureB().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureA().getBody().getUserData();
        }
        
        if(i != null && m != null) {
            if (m.getShip().canCollectItem()) {
                i.handleModuleSensor(m.getBody().getPosition());
            }
        }
    }

    public void checkLaserProjectileModuleCollision(Contact cntct) {
        LaserProjectile p = null;
        BasicModule m = null;

        if (cntct.getFixtureA().getBody().getUserData() instanceof LaserProjectile && cntct.getFixtureB().getBody().getUserData() instanceof BasicModule) {
            p = (LaserProjectile) cntct.getFixtureA().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureB().getBody().getUserData();
        } else if (cntct.getFixtureB().getBody().getUserData() instanceof LaserProjectile && cntct.getFixtureA().getBody().getUserData() instanceof BasicModule) {
            p = (LaserProjectile) cntct.getFixtureB().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureA().getBody().getUserData();
        }
        
        if(p != null && m != null) {
            p.handleBasicModuleCollision(m);
            sendRefreshGraphicOfShipMsg(m.getShip());
        }
    }

    public void checkLaserProjectileShieldCollision(Contact cntct) {
        LaserProjectile p = null;
        ShieldCollider s = null;

        if (cntct.getFixtureA().getBody().getUserData() instanceof LaserProjectile && cntct.getFixtureB().getBody().getUserData() instanceof ShieldCollider) {
            p = (LaserProjectile) cntct.getFixtureA().getBody().getUserData();
            s = (ShieldCollider) cntct.getFixtureB().getBody().getUserData();
        } else if (cntct.getFixtureB().getBody().getUserData() instanceof LaserProjectile && cntct.getFixtureA().getBody().getUserData() instanceof ShieldCollider) {
            p = (LaserProjectile) cntct.getFixtureB().getBody().getUserData();
            s = (ShieldCollider) cntct.getFixtureA().getBody().getUserData();
        }
        
        if(p != null && s != null) {
            p.handleShieldColliderCollision(s);
        }
    }
    
    public void checkItemModuleCollision (Contact cntct) {
        Item i = null;
        BasicModule m = null;

        if ((!cntct.getFixtureA().isSensor() && cntct.getFixtureA().getBody().getUserData() instanceof Item) && cntct.getFixtureB().getBody().getUserData() instanceof BasicModule) {
            i = (Item) cntct.getFixtureA().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureB().getBody().getUserData();
        } else if ((!cntct.getFixtureB().isSensor() && cntct.getFixtureB().getBody().getUserData() instanceof Item) && cntct.getFixtureA().getBody().getUserData() instanceof BasicModule) {
            i = (Item) cntct.getFixtureB().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureA().getBody().getUserData();
        }
        
        if(i != null && m != null) {
            i.handleShipCollision(m);
        }
    }

    public void endContact(Contact cntct) {
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
    
    public void sendRefreshGraphicOfShipMsg(BasicShip ship) {
        if (ship.getPlayer() != null) {
            NetMessages.ModuleDestroyedMsg msg = new NetMessages.ModuleDestroyedMsg(ship.getPlayer().con.getId(), ship.getOrientedModuleArray());
            msg.setReliable(true);
            app.getServer().broadcast(msg);
        }
    }
}