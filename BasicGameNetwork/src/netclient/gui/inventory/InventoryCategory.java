/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.gui.inventory;

import netclient.gui.ModuleType;

public class InventoryCategory {
    
    public static final InventoryCategory ARMOR_CAT = new InventoryCategory(ModuleType.ARMOR, ModuleType.ARMOR_DIAGONAL);
    public static final InventoryCategory WEAPON_CAT = new InventoryCategory(ModuleType.WEAPON);
    public static final InventoryCategory THRUSTER_CAT = new InventoryCategory(ModuleType.THRUSTER);
    public static final InventoryCategory COCKPIT_CAT = new InventoryCategory(ModuleType.COCKPIT);
    public static final InventoryCategory ENERGY_CAT = new InventoryCategory(ModuleType.ENERGY_GENERATOR);
    
    private ModuleType[] types;
    private int[] typeIds;
    private int[] tileImgIds;
    
    public InventoryCategory(ModuleType... types) {
        this.types = types;
        
        int numOfDifferentItems = types.length;
        typeIds = new int[numOfDifferentItems];
        tileImgIds = new int[numOfDifferentItems];
        
        for (int i=0; i<numOfDifferentItems; i++) {
            typeIds[i] = types[i].getValue();
            tileImgIds[i] = types[i].getValue() * 4;
        }
    }
    
    public ModuleType[] getTypes() {
        return types;
    }
    public int[] getTileImgIds() {
        return tileImgIds;
    }
    public int[] getTypeIds() {
        return typeIds;
    }
    
}
