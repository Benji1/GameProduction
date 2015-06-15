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
public class GMShieldGenerator extends GraphicalModule {
    
    public GMShieldGenerator(OrientedModule orientedModule, Node shipRoot, ClientShip ship, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, ship, x, y, app);
        
        colorActive = ColorRGBA.Cyan;
        modelPath = "3dmodels/shield_generator.obj";
        texturePath = "3dmodels/shield_generator_ao.png";
        
        createMyGraphic(x, y);
    }
    
    @Override
    protected void createMyGraphic(float x, float y) {
        createGraphicFromPath("3dmodels/armor.obj", "3dmodels/armor_ao.png", x, y);
        super.createMyGraphic(x, y);
    }
}
