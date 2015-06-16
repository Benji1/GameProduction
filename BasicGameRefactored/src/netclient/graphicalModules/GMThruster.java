/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GMThruster extends GraphicalModule {
    
    public GMThruster(OrientedModule orientedModule, Node nodeToAttach, float x, float y, WJSFClient app) {
        super(orientedModule, nodeToAttach, x, y, app);
        
        colorActive = ColorRGBA.Orange;
        modelPath = "3dmodels/thruster.obj";
        texturePath = "3dmodels/thruster_ao.png";
        
        createMyGraphic(x, y);
    }
    
    @Override
    protected void createMyGraphic(float x, float y) {
        super.createMyGraphic(x, y);        
    }
}
