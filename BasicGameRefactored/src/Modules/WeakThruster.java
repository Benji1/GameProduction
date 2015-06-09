/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import java.util.ArrayList;

/**
 *
 * @author 1337
 */
public class WeakThruster extends Thruster {

    public WeakThruster(ArrayList<Integer> keyCodes, FacingDirection orientationDirection) {
        super(keyCodes, orientationDirection);
        moduleName = "WeakThruster";
    }
}
