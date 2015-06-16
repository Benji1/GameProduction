/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.otherGraphics;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import netclient.WJSFClient;
import netclient.graphicalModules.GMArmor;
import netclient.graphicalModules.GMArmorDiagonal;
import netclient.graphicalModules.GMCockpit;
import netclient.graphicalModules.ModuleCreator;
import netclient.graphicalModules.GMEnergyGenerator;
import netclient.graphicalModules.GMLaserGun;
import netclient.graphicalModules.GMShieldGenerator;
import netclient.graphicalModules.GMStorage;
import netclient.graphicalModules.GMThruster;
import netclient.graphicalModules.GraphicalModule;
import netclient.gui.ModuleType;
import static netclient.gui.ModuleType.ARMOR;
import static netclient.gui.ModuleType.ARMOR_DIAGONAL;
import static netclient.gui.ModuleType.COCKPIT;
import static netclient.gui.ModuleType.ENERGY_GENERATOR;
import static netclient.gui.ModuleType.SHIELD;
import static netclient.gui.ModuleType.STORAGE;
import static netclient.gui.ModuleType.THRUSTER;
import static netclient.gui.ModuleType.WEAPON;
import netclient.gui.OrientedModule;
import netserver.modules.BasicModule;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.shipdesigns.TestShipDesigns;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author 1337
 */

public class GItem extends Node {
    
    protected int id;
    protected Node shipRoot;
    protected Body body;
    protected Material material;
    protected WJSFClient app;
    protected boolean collected;
    GraphicalModule gm;
    
    public GItem (int id, OrientedModule orientedModule, Vec2 spawnPoint, Quaternion rotation, WJSFClient app) {
        this.id = id;
        this.shipRoot = new Node("Item " + orientedModule.moduleType.toString() + " " + id);
        this.app = app;
      
        this.gm = ModuleCreator.createOrientedGraphicalModule(orientedModule, shipRoot, spawnPoint.x, spawnPoint.y, app);
        app.getRootNode().attachChild(this);
    }
    
    public void update(float tpf) {
    }
    
    public void remove() {
        this.removeFromParent();
    }
}