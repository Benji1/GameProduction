/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import com.jme3.math.ColorRGBA;

import java.util.ArrayList;
import netclient.gui.ModuleType;

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
}
