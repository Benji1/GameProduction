/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Main;
import org.jbox2d.common.Vec2;

/**
 *
 * @author 1337
 */
public class EncapsulatingItem {
    
    protected ArrayList<Spatial> spatials;
    protected Material material;
    protected Main app;
    protected Vec2 spawnPoint;
    protected Quaternion rotation;
    
    public EncapsulatingItem(ArrayList<Spatial> spatials, Vec2 spawnPoint, Quaternion rotation, Main app) {
        this.app = app;
        this.spatials = spatials;
        this.spawnPoint = spawnPoint;
        this.rotation = rotation;
    }
    
    public void init() {
        Item i = new Item(spatials, spawnPoint, rotation, app);
    }
}
