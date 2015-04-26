/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import config.ConfigReader;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float forceMagnitude = ConfigReader.getFromMap(ConfigReader.getBaseMap("Thruster"), "ForceMagnitude", float.class); 
    protected Vec2 forceVector;
   
    public Thruster(int orientation) {
        moduleName = "Thruster";
        energyConsumptionPerSecond = ConfigReader.getFromMap(ConfigReader.getBaseMap("Thruster"), "EnergyConsumptionPerSecond", float.class);
        
        // Nested Config Example
        //System.out.println(ConfigReader.getFromMap((Map) ConfigReader.getFromMap(ConfigReader.getBaseMap("Thruster"), "TestLayeredConfig", Map.class), "EvenDeeperLayer", String.class));
        
        
        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;
        
        switch(orientation) {
            case 0: forceVector = new Vec2(0, 1); break;
            case 1: forceVector = new Vec2(-1, 0); break;
            case 2: forceVector = new Vec2(0, -1); break;
            case 3: forceVector = new Vec2(1, 0); break;
            default: forceVector = new Vec2(0, 1);
        }
    }        

    protected void onActive() {
        Vec2 forceDirection = body.getWorldVector(forceVector).mul(forceMagnitude);
        body.applyForce(forceDirection, body.getPosition());
    }
}
