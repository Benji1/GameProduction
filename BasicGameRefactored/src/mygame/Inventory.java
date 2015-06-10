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
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.ARMOR_DIAGONAL);
        itemsInBase.add(ModuleType.WEAPON);
        itemsInBase.add(ModuleType.WEAPON);
        itemsInBase.add(ModuleType.WEAPON);
        itemsInBase.add(ModuleType.THRUSTER);
        itemsInBase.add(ModuleType.THRUSTER);
        itemsInBase.add(ModuleType.THRUSTER);
        itemsInBase.add(ModuleType.THRUSTER);
        itemsInBase.add(ModuleType.COCKPIT);
        itemsInBase.add(ModuleType.ENERGY_GENERATOR);
        itemsInBase.add(ModuleType.ENERGY_GENERATOR);
        itemsInBase.add(ModuleType.ENERGY_GENERATOR);
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
