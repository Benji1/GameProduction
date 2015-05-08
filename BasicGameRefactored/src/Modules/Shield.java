/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import static Modules.BasicModule.fillNotOverLimit;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
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
    protected float delta;

    public Shield(ArrayList<String> hotkeys) {
        super(hotkeys);
        moduleName = "Shield";
        energyConsumptionPerSecond = 50;
        color = ColorRGBA.Blue;
        colorActive = ColorRGBA.Cyan;
    }

    protected void onActive() {
        shieldPower = fillNotOverLimit(shieldPower, shieldRegenRate * delta, maxShieldPower);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        this.delta = tpf;
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
