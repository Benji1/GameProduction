/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import gui.ModuleType;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);

    public Thruster(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys);
        moduleName = "Thruster";
        energyConsumptionPerSecond = cr.getFromMap(cr.getBaseMap("Thruster"), "EnergyConsumptionPerSecond", float.class);

        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;

        orientation = orientationDirection;
        type = ModuleType.THRUSTER;
    }

    protected void onActive() {
        Vec2 forceDirection = body.getWorldVector(FacingDirection.getDirectionVector(orientation)).mul(forceMagnitude);
        body.applyForce(forceDirection, body.getPosition());
    }
}
