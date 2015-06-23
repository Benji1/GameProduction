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
public class GMCockpit extends GraphicalModule {
    
    public GMCockpit(OrientedModule orientedModule, Node nodeToAttach, float x, float y, WJSFClient app, float scale) {
        super(orientedModule, nodeToAttach, x, y, app);
        
        modelPath = "3dmodels/cockpit.obj";
        texturePath = "3dmodels/cockpit_ao.png";
        
        createMyGraphic(x, y, scale);
    }
}