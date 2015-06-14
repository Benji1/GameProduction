package netclient;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import netclient.gui.inventory.InventoryCategory;

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
    public Vector3f velocity;
    public OrientedModule[][] modules;
    public GraphicalModule[][] gmodules;
    public ArrayList<ModuleType> itemsInBase;
    public WJSFClient app;

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
        
        this.gmodules = new GraphicalModule[ship.length][ship[0].length];
        // TODO: get real velocity, atm needed for camera movement
        this.velocity = new Vector3f(0, 0, 0);

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
                    gmodules[i][j].removeFromParent();
                }
            }
        }
    }

    private GraphicalModule createOrientedModuleGraphics(OrientedModule om, float x, float y) {
        return new GraphicalModule(om, shipRoot, x, y, app);
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
}
