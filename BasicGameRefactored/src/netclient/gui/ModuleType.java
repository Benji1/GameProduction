/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.gui;

public enum ModuleType {

    COCKPIT(0),
    THRUSTER(1),
    ENERGY_GENERATOR(2),
    ARMOR(3),
    WEAPON(4),
    ARMOR_DIAGONAL(5),
    SHIELD(6),
    STORAGE(7);
    
    private final int value;
    
    private ModuleType(final int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static ModuleType getType(int val) {
    	for (ModuleType t : ModuleType.values()) {
            if (val == t.value) {
                return t;
            }
        }
    	return null;
    }    
    
    public static boolean isInteractiveModule(ModuleType moduleType) {
         return moduleType.equals(ModuleType.SHIELD) ||
                moduleType.equals(ModuleType.THRUSTER) ||
                moduleType.equals(ModuleType.WEAPON);
    }
}
