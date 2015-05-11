/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

public enum ModuleType {

    COCKPIT(2),
    THRUSTER(3),
    ENERGY_GENERATOR(4),
    ARMOR(5),
    WEAPON(6),
    ARMOR_DIAGONAL(7);
    
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
    
}
