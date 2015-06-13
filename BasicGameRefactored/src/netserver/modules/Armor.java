/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

/**
 *
 * @author 1337
 */
public class Armor extends BasicModule {

    public Armor() {
        super();
        moduleName = "Armor";
        color = ColorRGBA.Gray;
        
        maxHealth = cr.getFromMap(cr.getBaseMap("Armor"), "MaxHealth", int.class);
        health = maxHealth;
    }
    
    @Override
    protected void create3DBody() {
         AssetManager a = ship.getApp().getAssetManager();
         spatial = a.loadModel("3dmodels/armor.obj");
         ship.attachChild(spatial);
         material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t = a.loadTexture("3dmodels/armor_ao.png");
         material.setTexture("DiffuseMap", t);
         spatial.setMaterial(material);
    }
}