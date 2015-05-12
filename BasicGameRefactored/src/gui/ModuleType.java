/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

public enum ModuleType {

    COCKPIT(0),
    THRUSTER(1),
    ENERGY_GENERATOR(2),
    ARMOR(3),
    WEAPON(4),
    ARMOR_DIAGONAL(5);
    
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
