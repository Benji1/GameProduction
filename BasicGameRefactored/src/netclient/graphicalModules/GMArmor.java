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
public class GMArmor extends GraphicalModule {
    
    public GMArmor(OrientedModule orientedModule, Node shipRoot, float x, float y, WJSFClient app) {
        super(orientedModule, shipRoot, x, y, app);
    }
    
    @Override
    protected void createMyGraphic(float x, float y) {
        createGraphicFromPath("3dmodels/armor.obj", "3dmodels/armor_ao.png", x, y);
    }
}
