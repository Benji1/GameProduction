/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class WeakThruster extends InteractiveModule {

    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);
    protected float linearDampingFactor = cr.getFromMap(cr.getBaseMap("Thruster"), "LinearDampingWeak", float.class);
    protected Vec2 orientation;

    public WeakThruster(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys);
        moduleName = "Thruster";
        energyConsumptionPerSecond = cr.getFromMap(cr.getBaseMap("Thruster"), "EnergyConsumptionPerSecond", float.class);

        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;

        orientation = FacingDirection.getDirectionVector(orientationDirection);
    }

    protected void onActive() {
        Vec2 forceDirection = body.getWorldVector(orientation).mul(forceMagnitude);
        body.applyForce(forceDirection, body.getPosition());
    }
   
    @Override
    public void update(float delta) {
        super.update(delta);
        if (!active) {
            this.body.setLinearDamping(linearDampingFactor);
        }
    }
}
