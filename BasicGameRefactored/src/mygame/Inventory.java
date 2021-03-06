/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.Storage;
import gui.ModuleType;
import gui.inventory.InventoryCategory;
import java.util.ArrayList;

public class Inventory {
    
    private BasicShip ship;
    private ArrayList<Storage> storages;
    protected ArrayList<ModuleType> itemsInBase;
    
    public Inventory(BasicShip ship) {
        this.ship = ship;
        storages = new ArrayList<Storage>();
        itemsInBase = new ArrayList<ModuleType>();
        
        fillWithStartItems();
    }
    
    private void fillWithStartItems() {
        addMultipleItems(ModuleType.ARMOR, 99);//12
        addMultipleItems(ModuleType.ARMOR_DIAGONAL, 99);//7
        addMultipleItems(ModuleType.WEAPON, 99);//3
        addMultipleItems(ModuleType.THRUSTER, 99);//4
        addMultipleItems(ModuleType.COCKPIT, 99);//1
        addMultipleItems(ModuleType.ENERGY_GENERATOR, 99);//3
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
    
    public void MoveItemsFromShipToBaseStorage() {
        for (Storage storage : storages) {
            ArrayList<ModuleType> items = storage.getStoredItems();
            
            for (ModuleType item : items) {
                addItemToBase(item);
            }
            
            storage.clearItems();
        }
    }
    
}
