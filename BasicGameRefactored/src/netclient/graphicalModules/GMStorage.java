/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.scene.Node;
import netclient.WJSFClient;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class GMStorage extends GraphicalModule {
    
    public GMStorage(OrientedModule orientedModule, Node shipRoot, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, x, y, app);
        
        modelPath = "3dmodels/storage.obj";
        texturePath = "3dmodels/storage_ao.png";
        
        createMyGraphic(x, y);
    }
}
