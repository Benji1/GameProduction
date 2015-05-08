/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import services.config.ConfigReader;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import services.ServiceManager;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    ConfigReader cr = ServiceManager.getConfigReader();
    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);
    protected Vec2 orientation;

    public Thruster(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
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
}
