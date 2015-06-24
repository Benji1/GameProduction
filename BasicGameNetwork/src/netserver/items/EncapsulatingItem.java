/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.items;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import netclient.gui.OrientedModule;
import netserver.WJSFServer;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class EncapsulatingItem {
    
    protected ArrayList<Spatial> spatials;
    protected Material material;
    protected OrientedModule om;
    protected WJSFServer app;
    protected Vec2 spawnPoint;
    protected Quaternion rotation;
    
    public EncapsulatingItem(OrientedModule type, ArrayList<Spatial> spatials, Vec2 spawnPoint, Quaternion rotation, WJSFServer app) {
        this.app = app;
        this.om = type;
        this.spatials = spatials;
        this.spawnPoint = spawnPoint;
        this.rotation = rotation;
    }
    
    public void init() {
        Item i = new Item(om, spatials, spawnPoint, rotation, app);
        app.floatingItems.add(i);
    }
}