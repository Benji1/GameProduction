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
public class GMArmor extends GraphicalModule {
    
    public GMArmor(OrientedModule orientedModule, Node shipRoot, ClientShip ship, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, ship, x, y, app);
        
        modelPath = "3dmodels/armor.obj";
        texturePath = "3dmodels/armor_ao.png";
        
        createMyGraphic(x, y);
    }
}
