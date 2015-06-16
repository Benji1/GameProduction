/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import netclient.WJSFClient;
import netclient.graphicalModules.ModuleCreator;
import netclient.graphicalModules.GraphicalModule;
import netclient.gui.OrientedModule;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */

public class GItem extends GraphicObject {
    
    private GraphicalModule gm;
    private Material material;
    private WJSFClient app;
    
    public GItem (OrientedModule orientedModule, Vec2 spawnPoint, Quaternion rotation, WJSFClient app) {
        this.app = app;
        
        this.gm = ModuleCreator.createOrientedGraphicalModule(orientedModule, this, spawnPoint.x, spawnPoint.y, app);
        app.gameRunState.localRootNode.attachChild(this);
    }
    
    public void update(float tpf) {
    }

    @Override
    public void delete() {
        gm.removeFromParent();
        this.removeFromParent();
    }
}