/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.collision;

import Modules.BasicModule;
import items.Item;
import mygame.PhysicsWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import weapons.LaserProjectile;
import weapons.ShieldCollider;

/**
 *
 * @author 1337
 */
public class GameContactListener implements ContactListener {

    public GameContactListener() {
        PhysicsWorld.world.setContactListener(this);
    }

    public void beginContact(Contact cntct) {
        checkLaserProjectileModuleCollision(cntct);
        checkLaserProjectileShieldCollision(cntct);
        checkItemModuleCollision(cntct);
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

        if (cntct.getFixtureA().getBody().getUserData() instanceof Item && cntct.getFixtureB().getBody().getUserData() instanceof BasicModule) {
            i = (Item) cntct.getFixtureA().getBody().getUserData();
            m = (BasicModule) cntct.getFixtureB().getBody().getUserData();
        } else if (cntct.getFixtureB().getBody().getUserData() instanceof Item && cntct.getFixtureA().getBody().getUserData() instanceof BasicModule) {
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
}
