/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.graphicalModules;

import com.jme3.scene.Node;
import netclient.ClientShip;
import netclient.WJSFClient;
import static netclient.gui.ModuleType.ARMOR;
import static netclient.gui.ModuleType.ARMOR_DIAGONAL;
import static netclient.gui.ModuleType.COCKPIT;
import static netclient.gui.ModuleType.ENERGY_GENERATOR;
import static netclient.gui.ModuleType.SHIELD;
import static netclient.gui.ModuleType.STORAGE;
import static netclient.gui.ModuleType.THRUSTER;
import static netclient.gui.ModuleType.WEAPON;
import netclient.gui.OrientedModule;

/**
 *
 * @author 1337
 */
public class ModuleCreator {
    
    public static GraphicalModule createOrientedGraphicalModule(OrientedModule om, Node nodeToAttach, float x, float y, WJSFClient app, float scale) {
        switch (om.moduleType) {
            case ARMOR:
                return new GMArmor(om, nodeToAttach, x, y, app, scale);
            case ARMOR_DIAGONAL:
                return new GMArmorDiagonal(om, nodeToAttach, x, y, app, scale);
            case COCKPIT:
                return new GMCockpit(om, nodeToAttach, x, y, app, scale);
            case ENERGY_GENERATOR:
                return new GMEnergyGenerator(om, nodeToAttach, x, y, app, scale);
            case SHIELD:
                return new GMShieldGenerator(om, nodeToAttach, x, y, app, scale);
            case STORAGE:
                return new GMStorage(om, nodeToAttach, x, y, app, scale);
            case THRUSTER:
                return new GMThruster(om, nodeToAttach, x, y, app, scale);
            case WEAPON:
                return new GMLaserGun(om, nodeToAttach, x, y, app, scale);
            default:
                return new GMArmor(om, nodeToAttach, x, y, app, scale);
        }
    }
    
    public static ShipModule createOrientedShipModule(OrientedModule om, Node nodeToAttach, ClientShip ship, float x, float y, WJSFClient app) {
        switch (om.moduleType) {
            case ARMOR:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case ARMOR_DIAGONAL:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case COCKPIT:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case ENERGY_GENERATOR:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case SHIELD:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case STORAGE:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            case THRUSTER:
                return new ShipThruster(om, x, y, nodeToAttach, ship, app);
            case WEAPON:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
            default:
                return new ShipModule(om, x, y, nodeToAttach, ship, app);
        }
    }
    
    
}
