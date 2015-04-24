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

    protected float maxForce;
    protected float forceMagnitude = 100;    
    protected Vec2 forceVector;
   
    public Thruster(int orientation) {
        moduleName = "Thruster";
        energyConsumptionPerSecond = 10;
        color = ColorRGBA.LightGray;
        colorActive = ColorRGBA.Orange;
        
        maxForce = ConfigReader.get("ThrusterMaxForce", float.class);
        
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
