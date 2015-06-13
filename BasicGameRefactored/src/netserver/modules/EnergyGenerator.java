/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import static netserver.modules.BasicModule.fillNotOverLimit;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import netclient.gui.ModuleType;

/**
 *
 * @author 1337
 */
public class EnergyGenerator extends BasicModule {
    
    private float energyGeneratedPerSecond = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "EnergyGeneratedPerSecond", float.class);
    private float energyStorageLimit = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "EnergyStorageLimit", float.class);
    private float energyStorage = energyStorageLimit;
    private int radius = cr.getFromMap(cr.getBaseMap("EnergyGenerator"), "Radius", int.class);
    
    public EnergyGenerator() {
        super();
        moduleName = "E-Gen";
        color = ColorRGBA.Yellow;
        type = ModuleType.ENERGY_GENERATOR;
        orientation = FacingDirection.FORWARD;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        regenerateEnergy(delta);
    }

    public void regenerateEnergy(float delta) {
        energyStorage = fillNotOverLimit(energyStorage, energyGeneratedPerSecond * delta, energyStorageLimit);

        ColorRGBA c = new ColorRGBA();
        c.interpolate(ColorRGBA.DarkGray, color, energyStorage / energyStorageLimit);
    
        material.setColor("Ambient", c);
        material.setColor("Diffuse", c);
    }
    
    public float getEnergy() {
        return energyStorage;
    }
    
    public void reduceEnergy(float amount) {
        energyStorage -= amount;
    }
    
    public int getRadius() {
        return radius;
    }
    
     @Override
    protected void create3DBody() {
         AssetManager a = ship.getApp().getAssetManager();
         spatial = a.loadModel("3dmodels/generator.obj");
         material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t = a.loadTexture("3dmodels/generator_ao.png");
         material.setBoolean("UseMaterialColors", true);
         material.setTexture("DiffuseMap", t);
         spatial.setMaterial(material);

         Spatial spatial2 = a.loadModel("3dmodels/armor.obj");
         this.attachChild(spatial2);
         Material material2 = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t2 = a.loadTexture("3dmodels/armor_ao.png");
         material2.setTexture("DiffuseMap", t2);
         spatial2.setMaterial(material2);
    }
}
