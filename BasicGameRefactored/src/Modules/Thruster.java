/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class Thruster extends InteractiveModule {

    protected float forceMagnitude = cr.getFromMap(cr.getBaseMap("Thruster"), "ForceMagnitude", float.class);
    protected float linearDampingFactor = cr.getFromMap(cr.getBaseMap("Thruster"), "LinearDamping", float.class);
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
   
    @Override
    public void update(float delta) {
        super.update(delta);
        if (!active) {
            this.body.setLinearDamping(linearDampingFactor);
        }
    }
    
    @Override
    protected void create3DBody() {
        super.create3DBody();
        AssetManager a = ship.getApp().getAssetManager();
        spatial = a.loadModel("3dmodels/thruster.obj");
        material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = a.loadTexture("3dmodels/thruster_ao.png");
        material.setTexture("DiffuseMap", t);
        spatial.setMaterial(material);
        
        materialActive.setTexture("DiffuseMap", t);
    }
}
