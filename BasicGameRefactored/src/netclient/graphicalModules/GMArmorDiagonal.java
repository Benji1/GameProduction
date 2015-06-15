/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GMArmorDiagonal  extends GraphicalModule {
    
    public GMArmorDiagonal(OrientedModule orientedModule, Node shipRoot, ClientShip ship, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, ship, x, y, app);
        
        modelPath = "3dmodels/armor_half.obj";
        texturePath = "3dmodels/armor_half_ao.png";
        
        createMyGraphic(x, y);
    }
}