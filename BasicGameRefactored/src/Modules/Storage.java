/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import gui.ModuleType;
import java.util.ArrayList;
import mygame.BasicShip;

/**
 *
 * @author 1337
 */
public class Storage extends BasicModule {

    protected ArrayList<ModuleType> itemsInStorage = new ArrayList<ModuleType>();
    protected int maxStoredItems = cr.getFromMap(cr.getBaseMap("Storage"), "MaxStorage", int.class);

    public Storage() {
        super();
        moduleName = "Storage";
        color = ColorRGBA.Magenta;
        type = ModuleType.STORAGE;
        orientation = FacingDirection.FORWARD;
    }
    
    public boolean storeItem(ModuleType item) {
        if (itemsInStorage.size() < maxStoredItems) {
            itemsInStorage.add(item);
            return true;
        }
        return false;
    }
    
    public ModuleType removeItem(int index) {
        return itemsInStorage.remove(index);
    }
    public boolean removeItemOfType(ModuleType type) {
        for (int i=0; i<itemsInStorage.size(); i++) {
            if (itemsInStorage.get(i).equals(type)) {
                itemsInStorage.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void onPlaced(BasicShip ship) {
        super.onPlaced(ship);
        if (ship.getPlayer() != null) {
            ship.getPlayer().getInventory().addStorage(this);
        }
    }
    
    @Override
    public void onRemove() {
        ship.getPlayer().getInventory().removeStorage(this);
        super.onRemove();
    }
    
    public void clearItems() {
        itemsInStorage.clear();
    }
    
    public ArrayList<ModuleType> getStoredItems() {
        return itemsInStorage;
    }
    
    @Override
    protected void create3DBody() {
         AssetManager a = ship.getApp().getAssetManager();
         spatial = a.loadModel("3dmodels/storage.obj");
         ship.attachChild(spatial);
         material = new Material(a, "Common/MatDefs/Light/Lighting.j3md");
         Texture t = a.loadTexture("3dmodels/storage_ao.png");
         material.setTexture("DiffuseMap", t);
         spatial.setMaterial(material);
    }
}
