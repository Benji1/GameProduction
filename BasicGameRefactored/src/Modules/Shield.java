/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import static Modules.BasicModule.fillNotOverLimit;
import com.jme3.math.ColorRGBA;
import gui.ModuleType;
import java.util.ArrayList;
import weapons.ShieldCollider;

/**
 *
 * @author 1337
 */
public class Shield extends InteractiveModule {

    protected float maxShieldPower = cr.getFromMap(cr.getBaseMap("Shield"), "MaxShieldPower", float.class);
    protected float shieldPower = maxShieldPower;
    protected float shieldRegenRate = cr.getFromMap(cr.getBaseMap("Shield"), "RegenRate", float.class);
    protected float delta;
    protected ShieldCollider sc;

    public Shield(ArrayList<String> hotkeys) {
        super(hotkeys);
        moduleName = "Shield";
        energyConsumptionPerSecond = cr.getFromMap(cr.getBaseMap("Shield"), "EnergyConsumptionPerSecond", float.class);
        color = ColorRGBA.Blue;
        colorActive = ColorRGBA.Cyan;
        type = ModuleType.SHIELD;
        orientation = FacingDirection.FORWARD;
    }
    
    public ShieldCollider getShieldCollider  () {
        return sc;
    }

    protected void onActive() {
        shieldPower = fillNotOverLimit(shieldPower, shieldRegenRate * delta, maxShieldPower);

        ColorRGBA c = new ColorRGBA();
        c.interpolate(ColorRGBA.DarkGray, colorActive, shieldPower / maxShieldPower);

        materialActive.setColor("Ambient", c);
        materialActive.setColor("Diffuse", c);

        sc.update(delta);

        if (shieldPower <= 0) {
            this.deactivate();
        }
    }

    @Override
    public void activate() {
        super.activate();
        sc = new ShieldCollider(this);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        if (sc != null) {
            sc.die();
            sc = null;
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if(sc != null) {
            sc.die();
            sc = null;
        }
        ship.interactiveModules.remove(this);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        this.delta = tpf;
    }

    public void takeDamageOnShield(float amount) {
        this.shieldPower -= amount;
    }
}
