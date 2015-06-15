/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver;

import java.util.ArrayList;
import netclient.gui.ModuleType;
import netclient.gui.inventory.InventoryCategory;
import netserver.modules.Storage;

public class Inventory {
    
    private BasicShip ship;
    private ArrayList<Storage> storages;
    private ArrayList<ModuleType> itemsInBase;
    
    public Inventory(BasicShip ship) {
        this.ship = ship;
        storages = new ArrayList<Storage>();
        itemsInBase = new ArrayList<ModuleType>();
        
        fillWithStartItems();
    }
    
    private void fillWithStartItems() {
        addMultipleItems(ModuleType.ARMOR, 50);//12
        addMultipleItems(ModuleType.ARMOR_DIAGONAL, 50);//7
        addMultipleItems(ModuleType.WEAPON, 50);//3
        addMultipleItems(ModuleType.THRUSTER, 50);//4
        addMultipleItems(ModuleType.COCKPIT, 50);//1
        addMultipleItems(ModuleType.ENERGY_GENERATOR, 50);//3
    }
    
    private void addMultipleItems(ModuleType moduleType, int count) {
        for (int i = 0; i < count; ++i) {
            itemsInBase.add(moduleType);
        }
     }
    
    public void addStorage(Storage storage) {
        storages.add(storage);
    }
    
    public void removeStorage(Storage storage) {
        storages.remove(storage);
    }
    
    public int getModuleCountInStorage(ModuleType type) {
        int counter = 0;
        
        for (int i=0; i<storages.size(); i++) {
            for (int j=0; j<storages.get(i).getStoredItems().size(); j++) {
                if (storages.get(i).getStoredItems().get(j).equals(type)) {
                    counter++;
                }
            }
        }
        
        return counter;
    }
    public int getModuleCountInBase(ModuleType type) {
        int counter = 0;
        
        for (int i=0; i<itemsInBase.size(); i++) {
            if (itemsInBase.get(i).equals(type)) {
                counter++;
            }
        }
        
        return counter;
    }
    
    public ModuleType[] getModulesInBase() {
        return this.itemsInBase.toArray(new ModuleType[itemsInBase.size()]);
    }
    
    public int[] getNumOfItemsPerCat(InventoryCategory cat) {
        int arraySize = cat.getTypes().length;
        int[] numOfEachItem = new int[arraySize];
        
        for (int i=0; i<arraySize; i++) {
            numOfEachItem[i] = this.getModuleCountInBase(cat.getTypes()[i]);
        }
        
        return numOfEachItem;
    }
    
    public boolean removeItemFromBase(ModuleType type) {
        for (int i=0; i<itemsInBase.size(); i++) {
            if (itemsInBase.get(i).equals(type)) {
                itemsInBase.remove(i);
                return true;
            }
        }
        
        return false;
    }
    public void addItemToBase(ModuleType type) {
        itemsInBase.add(type);
    }
    
    public void moveItemsFromShipToBaseStorage() {
        for (Storage storage : storages) {
            ArrayList<ModuleType> items = storage.getStoredItems();
            
            for (ModuleType item : items) {
                addItemToBase(item);
            }
            
            storage.clearItems();
        }
    }
    
    public void setItemsInBase(ModuleType[] itemsInBase) {
        this.itemsInBase.clear();
        for (int i=0; i<itemsInBase.length; i++) {
            this.itemsInBase.add(itemsInBase[i]);
        }
    }
    
    public void clear() {
        this.itemsInBase.clear();
    }
    
}
