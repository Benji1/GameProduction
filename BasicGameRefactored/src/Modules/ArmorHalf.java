/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import gui.ModuleType;

/**
 *
 * @author 1337
 */
public class ArmorHalf extends BasicModule {

    public ArmorHalf() {
        super();
        moduleName = "Armor";
        color = ColorRGBA.Gray;
        
        maxHealth = cr.getFromMap(cr.getBaseMap("Armor"), "MaxHealth", int.class);
        health = maxHealth;
        type = ModuleType.ARMOR;
        orientation = FacingDirection.FORWARD;
    }
    
    @Override
    protected void create3DBody() {
         AssetManager a = ship.getApp().getAssetManager();
         spatial = a.loadModel("3dmodels/armor_half.obj");
         ship.attachChild(spatial);
         material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t = a.loadTexture("3dmodels/armor_half_ao.png");
         material.setTexture("DiffuseMap", t);
         spatial.setMaterial(material);
    }
}
