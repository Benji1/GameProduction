/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.modules;

import java.util.ArrayList;


/**
 *
 * @author 1337
 */
public class WeakThruster extends Thruster {

    public WeakThruster(ArrayList<String> hotkeys, FacingDirection orientationDirection) {
        super(hotkeys, orientationDirection);
        linearDampingFactor = cr.getFromMap(cr.getBaseMap("Thruster"), "LinearDampingWeak", float.class);
        moduleName = "WeakThruster";
    }
}
