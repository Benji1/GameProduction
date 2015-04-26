/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import static Modules.BasicModule.fillNotOverLimit;
import com.jme3.math.ColorRGBA;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author 1337
 */
public class Shield extends InteractiveModule {

    protected float maxShieldPower = 700;
    protected float shieldPower = maxShieldPower;
    protected float shieldRegenRate = 40;

    public Shield() {
        moduleName = "Shield";
        energyConsumptionPerSecond = 50;
        color = ColorRGBA.Blue;
        colorActive = ColorRGBA.Cyan;
    }

    protected void onActive() {
        if (energyAvailableInPercent >= 100) {
            shieldPower = fillNotOverLimit(shieldPower, shieldRegenRate, maxShieldPower);
        }
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    private class ShieldCollider implements ContactListener {

        public void beginContact(Contact cntct) {            
        }

        public void endContact(Contact cntct) {
        }

        public void preSolve(Contact cntct, Manifold mnfld) {
        }

        public void postSolve(Contact cntct, ContactImpulse ci) {
        }
        
    }
}
