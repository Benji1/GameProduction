/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.math.ColorRGBA;
import services.config.ConfigReader;
import java.util.ArrayList;
import mygame.Item;
import services.ServiceManager;

/**
 *
 * @author 1337
 */
public class Storage extends BasicModule {

    ConfigReader cr = ServiceManager.getConfigReader();
    protected ArrayList<Item> itemsInStorage = new ArrayList<Item>();
    protected int maxStoredItems = cr.getFromMap(cr.getBaseMap("Storage"), "MaxStorage", int.class);

    public Storage() {
        moduleName = "Storage";
        color = ColorRGBA.Magenta;
    }
    
    public boolean storeItem(Item item) {
        if (itemsInStorage.size() < maxStoredItems) {
            itemsInStorage.add(item);
            return true;
        }

        return false;
    }
    
    public Item removeItem(int index) {
        return itemsInStorage.remove(index);
    }
}
