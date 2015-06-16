/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GMLaserGun extends GraphicalModule {
    
    public GMLaserGun(OrientedModule orientedModule, Node nodeToAttach,float x, float y, WJSFClient app) {
        super(orientedModule, nodeToAttach, x, y, app);
        
        colorActive = ColorRGBA.Red;
        modelPath = "3dmodels/gun.obj";
        texturePath = "3dmodels/gun_ao.png";
        
        createMyGraphic(x, y);
    }
}
