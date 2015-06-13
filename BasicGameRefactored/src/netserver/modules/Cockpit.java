/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import netserver.BasicShip;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

/**
 *
 * @author 1337
 */
public class Cockpit extends BasicModule {

    public Cockpit() {
        super();
        moduleName = "Cockpit";
        color = ColorRGBA.White;
    }
    
    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
         
        ship.cockpit = this;
    }
    
    @Override
    protected void create3DBody() {
         AssetManager a = ship.getApp().getAssetManager();
         spatial = a.loadModel("3dmodels/cockpit.obj");
         material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t = a.loadTexture("3dmodels/cockpit_ao.png");
         material.setTexture("DiffuseMap", t);
         spatial.setMaterial(material);
         spatial.rotate(0, (float) Math.PI / 2f, 0);
    }
    
    @Override
    public void onRemove() {
        super.onRemove();      
        ship.cockpit = null;
        // TODO SET NEW PHYSICS CENTER OR FIND BETTER SOLUTION
    }

    @Override
    public void destroy() {
        super.destroy();
        ship.disable();
    }
}