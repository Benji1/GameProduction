/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver;

import java.awt.Point;
import java.util.ArrayList;
import netserver.modules.BasicModule;
import netserver.modules.InteractiveModule;
import netserver.modules.Storage;
import netserver.services.updater.IUpdateable;
import netserver.universe.Abs_ChunkNode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Map;
import netclient.gui.ModuleType;
import netclient.gui.OrientedModule;
import netserver.modules.Armor;
import netserver.modules.Cockpit;
import netserver.modules.EnergyGenerator;
import netserver.modules.LaserGun;
import netserver.modules.Shield;
import netserver.modules.Thruster;

/**
 *
 * @author 1337
 */
public class BasicShip extends Abs_ChunkNode implements IUpdateable {

    public int shipHeight = 22;
    public int shipWidth = 22;
    public BasicModule[][] modules = new BasicModule[shipHeight][shipWidth];
    public ArrayList<InteractiveModule> interactiveModules = new ArrayList<InteractiveModule>();
    public Cockpit cockpit;
    public Vector3f cockpitPos;
    private NetPlayer player;
    private WJSFServer app;
    
    private BasicShip(WJSFServer app, String name) {
        //this(app, name, null);
    }
    
    public BasicShip(WJSFServer app, String name, NetPlayer player) {
        super(app, name, Abs_ChunkNode.ChunkNodeType.PlayerShips, false, new Vector3f(0,0,0));
    	//super(name);
    	this.app = app;
        app.getRootNode().attachChild(this);
        //app.gameRunState.ships.add(this);
        this.player = player;
    }
    
    public void setPlayer(NetPlayer player) {
        this.player = player;
    }
    public NetPlayer getPlayer() {
        return player;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf, this.cockpit.getLocalTranslation());

        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].update(tpf);
                }
            }
        }
    }
    
    public BasicModule[][] getModules() {
        return modules;
    }

    public WJSFServer getApp() {
        return this.app;
    }

    public void addModuleAt(BasicModule module, Point p) {
        modules[p.x][p.y] = module;

        module.onPlaced(this);
        informOtherModulesOfAddedModule(module, p);
    }

    public void addModuleAtFromOffset(BasicModule module, Point offset) {
        addModuleAt(module, offsetToActual(offset));
    }

    private void informOtherModulesOfAddedModule(BasicModule module, Point p) {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null && !(p.x == i && p.y == j)) {
                    modules[i][j].otherModulePlaced(module, p);
                }
            }
        }
    }

    public void removeModuleAt(Point p) {
        if (p != null) {
            informOtherModulesOfRemovedModule(p);
            modules[p.x][p.y] = null;
        }
    }

    public void removeModuleAtFromOffset(Point offset) {
        removeModuleAt(offsetToActual(offset));
    }

    private void informOtherModulesOfRemovedModule(Point p) {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null && !(p.x == i && p.y == j)) {
                    modules[i][j].otherModuleRemoved(getModule(p), p);
                }
            }
        }
    }

    public BasicModule getModule(Point p) {
        if (!(p.x < 0 || p.x > modules.length - 1 || p.y < 0 || p.y > modules[0].length - 1)) {
            return modules[p.x][p.y];
        }
        return null;
    }

    public BasicModule getModuleFromOffset(Point offset) {
        return getModule(offsetToActual(offset));
    }

    public InteractiveModule getInteractiveModule(Point p) {
        if (getModule(p) instanceof InteractiveModule) {
            return (InteractiveModule) getModule(p);
        }
        return null;
    }

    public InteractiveModule getInteractiveModuleFromOffset(Point offset) {
        return getInteractiveModule(offsetToActual(offset));
    }

    public Point getActualPositionInGrid(BasicModule m) {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] == m) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void print() {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    System.out.print(modules[i][j].getModuleName() + "\t\t");
                } else {
                    System.out.print("-\t\t");
                }
            }
            System.out.println();
        }
    }

    public void disable() {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] instanceof InteractiveModule) {
                    ((InteractiveModule) modules[i][j]).disable();
                }
            }
        }

        // TODO: player returns to base
    }

    public boolean collectItem(ModuleType item) {
        // check for available item storage
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] instanceof Storage) {
                    if (((Storage) modules[i][j]).storeItem(item)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean canCollectItem() {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] instanceof Storage) {
                    if (!((Storage) modules[i][j]).isFull()) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private Point offsetToActual(Point offset) {
        return new Point(modules.length / 2 - offset.x, modules.length / 2 + offset.y);
    }

    public ArrayList<InteractiveModule> getInteractiveModules() {
        return interactiveModules;
    }

    public ArrayList<InteractiveModule> getInteractiveModulesWithKeyCode(Integer keyCode) {
        ArrayList<InteractiveModule> ims = new ArrayList<InteractiveModule>();
        for (InteractiveModule im : getInteractiveModules()) {
            for (Integer i : im.getKeyCodes()) {
                if (i.equals(keyCode)) {
                    ims.add(im);
                }
            }
        }
        return ims;
    }

    public void activateModules(Integer keyCode) {
        for (InteractiveModule im : getInteractiveModulesWithKeyCode(keyCode)) {
            im.activate();
        }
    }

    public void deactivateModules(Integer keyCode) {
        for (InteractiveModule im : getInteractiveModulesWithKeyCode(keyCode)) {
            im.deactivate();
        }
    }
    
    public void handleKeyPressed(Integer keyCode) {
        for (InteractiveModule im : interactiveModules) {
            im.handleKeyPressed(keyCode);
        }
    }
    public void handleKeyReleased(Integer keyCode) {
        for (InteractiveModule im : interactiveModules) {
            im.handleKeyReleased(keyCode);
        }
    }

    public void seperateInNewShips() {
        int[][] alreadyAddedModules = new int[shipHeight][shipWidth];
        BasicModule[][] ms = new BasicModule[shipHeight][shipWidth];

        for (int i = 0; i < modules.length; i++) {
            System.arraycopy(modules[i], 0, ms[i], 0, modules[i].length);
        }

        int shipNumber = 1; // where 0 is is null

        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
                if (modules[i][j] != null && alreadyAddedModules[i][j] == 0) {
                    floodFill(shipNumber, alreadyAddedModules, ms, new Point(i, j));
                    shipNumber++;
                }
            }
        }
        //printInt(alreadyAddedModules);
        //System.out.println();

        // if seperated in more than one ship
        if (shipNumber > 2) { // more than one ship (cause it starts with one and gets a ++ at the end of the loop)
            for (int k = 3; k <= shipNumber; k++) {
                // XXX
                BasicShip newShip = new BasicShip(app, name);
                for (int i = 0; i < modules.length; i++) {
                    for (int j = 0; j < modules[i].length; j++) {
                        if (alreadyAddedModules[i][j] == k - 1) {
                            BasicModule b = modules[i][j];
                            removeModuleAt(new Point(i, j));
                            newShip.getModuleFromOtherShip(b, new Point(i, j));
                        } else {
                            newShip.removeModuleAt(new Point(i, j));
                        }
                    }
                }
//                System.out.println("New Ship:");
//                newShip.print();
            }
//            System.out.println("Old Ship:");
//            this.print();
        }
    }

    public void getModuleFromOtherShip(BasicModule module, Point p) {
        modules[p.x][p.y] = module;

        module.onMovedToOtherShip(this);
        informOtherModulesOfAddedModule(module, p);
    }

    public void floodFill(int shipNumber, int[][] alreadyAdded, BasicModule[][] ms, Point start) {
        if (ms[start.x][start.y] != null && alreadyAdded[start.x][start.y] < shipNumber) {

            alreadyAdded[start.x][start.y] = shipNumber;
            if (start.x - 1 > 0) {
                floodFill(shipNumber, alreadyAdded, ms, new Point(start.x - 1, start.y));
            }
            if (start.x + 1 < ms[start.y].length) {
                floodFill(shipNumber, alreadyAdded, ms, new Point(start.x + 1, start.y));
            }
            if (start.y - 1 > 0) {
                floodFill(shipNumber, alreadyAdded, ms, new Point(start.x, start.y - 1));
            }
            if (start.y + 1 < ms.length) {
                floodFill(shipNumber, alreadyAdded, ms, new Point(start.x, start.y + 1));
            }
        }
    }

    public void printInt(int[][] b) {
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                String s;
                if (b[i][j] < 1) {
                    s = "-";
                } else {
                    s = b[i][j] + "";
                }
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public void onShipChanged(OrientedModule[][] modulesNewShip) {
        BasicModule[][] newModules = new BasicModule[modulesNewShip.length][modulesNewShip[0].length];
        
        for (int i=0; i<modulesNewShip.length; i++) {
            for (int j=0; j<modulesNewShip[0].length; j++) {
                if (modulesNewShip[i][j] != null) {
                    switch(modulesNewShip[i][j].moduleType) {
                        case COCKPIT:
                            newModules[i][j] = new Cockpit();
                            break;
                        case THRUSTER:
                            newModules[i][j] = new Thruster(modulesNewShip[i][j].keyCodes, modulesNewShip[i][j].facingDirection);
                            break;
                        case ENERGY_GENERATOR:
                            newModules[i][j] = new EnergyGenerator();
                            break;
                        case ARMOR:
                            newModules[i][j] = new Armor();
                            break;
                        case WEAPON:
                            newModules[i][j] = new LaserGun(modulesNewShip[i][j].keyCodes, modulesNewShip[i][j].facingDirection);
                            break;
                        case ARMOR_DIAGONAL:
                            newModules[i][j] = new Armor();
                            break;
                        case SHIELD:
                            newModules[i][j] = new Shield(modulesNewShip[i][j].keyCodes);
                            break;
                        case STORAGE:
                            newModules[i][j] = new Storage();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        
        this.cockpitPos = this.cockpit.getBodyPos().clone();
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].destroyWithoutSeperation();
                }
            }
        }
        modules = new BasicModule[newModules.length][newModules[0].length];

        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
            	if(modulesNewShip[i][j] != null && modulesNewShip[i][j].moduleType.equals(ModuleType.COCKPIT)) {
            		this.cockpitPos = new Vector3f(cockpitPos.x - 2*i, 0, cockpitPos.z - 2*j);
            	}
            }
        }
        
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
                if(newModules[i][j] != null) {
                    addModuleAt(newModules[i][j], new Point(i, j));
                }
            }
        }
    }
    
    public boolean hasStillModules() {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void delete() {
        // TODO
    }
    
    public OrientedModule[][] getOrientedModuleArray() {
       OrientedModule[][] oModules = new OrientedModule[modules.length][modules[0].length];
       
       for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    if (modules[i][j] instanceof InteractiveModule) {
                        oModules[i][j] = new OrientedModule(modules[i][j].getType(), modules[i][j].getOrientation(), ((InteractiveModule)modules[i][j]).getKeyCodes());
                    } else {
                        oModules[i][j] = new OrientedModule(modules[i][j].getType(), modules[i][j].getOrientation());
                    }                    
                }
            }
        }
       
       return oModules;
    }
    
    public void ToggleDamping(){
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].toggleDamping();
                }
            }
        }
    }
}
