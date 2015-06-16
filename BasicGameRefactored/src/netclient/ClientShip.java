package netclient;

import netclient.graphicalModules.GraphicalModule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import netclient.graphicalModules.GMArmor;
import netclient.graphicalModules.GMArmorDiagonal;
import netclient.graphicalModules.GMCockpit;
import netclient.graphicalModules.GMEnergyGenerator;
import netclient.graphicalModules.GMLaserGun;
import netclient.graphicalModules.GMShieldGenerator;
import netclient.graphicalModules.GMStorage;
import netclient.graphicalModules.GMThruster;
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
import netclient.gui.inventory.InventoryCategory;
import org.jbox2d.common.Vec2;

public class ClientShip {

    /**
     * ********************************
     ********** CLASS FIELDS ********* ********************************
     */
    // client stuff
    public String name;
    public int id;
    
    // ship stuff
    public Node shipRoot;
    
    private Vec2 velocity;
    private float angVelocity;
    
    public OrientedModule[][] modules;
    public GraphicalModule[][] gmodules;
    public ArrayList<ModuleType> itemsInBase;
    public WJSFClient app;
    private int activatedThrusterCount;

    /**
     * ********************************
     ********** CONSTRUCTORS ********* ********************************
     */    
    public ClientShip(String name, int id, OrientedModule[][] ship, ModuleType[] modulesInBase, WJSFClient app) {
        this.app = app;
        this.name = name;
        this.id = id;

        this.shipRoot = new Node("ShipNode " + name);
        this.modules = ship;
        this.itemsInBase = new ArrayList<ModuleType>();
        setItemsInBase(modulesInBase);
        activatedThrusterCount = 0;
        
        this.gmodules = new GraphicalModule[ship.length][ship[0].length];
        this.velocity = new Vec2();

        // build ship
        refreshGraphicsOfShip();
    }

    public OrientedModule[][] getModules() {
        return modules;
    }

    public void setModules(OrientedModule[][] modules) {
        this.modules = modules;
        refreshGraphicsOfShip();
    }
    public void setItemsInBase(ModuleType[] itemsInBase) {
        this.itemsInBase.clear();
        for (int i=0; i<itemsInBase.length; i++) {
            this.itemsInBase.add(itemsInBase[i]);
        }
    }

    public void refreshGraphicsOfShip() {
        removeOldGraphics();
        createNewGraphics();
    }
    
    private void createNewGraphics() {
        gmodules = new GraphicalModule[modules.length][modules[0].length];
        
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    gmodules[i][j] = createOrientedModuleGraphics(modules[i][j], i - modules.length / 2, j - modules[0].length / 2);
                }
            }
        }
    }

    private void removeOldGraphics() {
        for (int i = 0; i < gmodules.length; i++) {
            for (int j = 0; j < gmodules[0].length; j++) {
                if (gmodules[i][j] != null) {
                    gmodules[i][j].remove();
                }
            }
        }
    }

    private GraphicalModule createOrientedModuleGraphics(OrientedModule om, float x, float y) {
        switch (om.moduleType) {
            case ARMOR:
                return new GMArmor(om, shipRoot, this, x, y, app);
            case ARMOR_DIAGONAL:
                return new GMArmorDiagonal(om, shipRoot, this, x, y, app);
            case COCKPIT:
                return new GMCockpit(om, shipRoot, this, x, y, app);
            case ENERGY_GENERATOR:
                return new GMEnergyGenerator(om, shipRoot, this, x, y, app);
            case SHIELD:
                return new GMShieldGenerator(om, shipRoot, this, x, y, app);
            case STORAGE:
                return new GMStorage(om, shipRoot, this, x, y, app);
            case THRUSTER:
                return new GMThruster(om, shipRoot, this, x, y, app);
            case WEAPON:
                return new GMLaserGun(om, shipRoot, this, x, y, app);
            default:
                return new GMArmor(om, shipRoot, this, x, y, app);
        }
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
    public int[] getNumOfItemsPerCat(InventoryCategory cat) {
        int arraySize = cat.getTypes().length;
        int[] numOfEachItem = new int[arraySize];
        
        for (int i=0; i<arraySize; i++) {
            numOfEachItem[i] = this.getModuleCountInBase(cat.getTypes()[i]);
        }
        
        return numOfEachItem;
    }
    
    public void activateModule(int x, int y) {
        gmodules[x][y].activate();
    }
    public void deactivateModule(int x, int y) {
        gmodules[x][y].deactivate();
    }
    
    public void increaseActivatedThrusterCount() {
        this.activatedThrusterCount++;
    }
    public void decreaseActivatedThrusterCount() {
        this.activatedThrusterCount--;
    }
    public boolean hasActivatedThruster() {
        return activatedThrusterCount > 0;
    }
    
    public void setVelocity(Vec2 vel) {
        this.velocity = vel;
    }
    public void setAngVelocity(float angVel) {
        this.angVelocity = angVel;
    }
    public Vec2 getVelocity() {
        return velocity;
    }
    public float getAngVelocity() {
        return angVelocity;
    }
    
    public void update() {
        for (int i=0; i<gmodules.length; i++) {
            for (int j=0; j<gmodules[i].length; j++) {
                if (gmodules[i][j] != null) {
                    gmodules[i][j].update();
                }
            }
        }
    }
}
