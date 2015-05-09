/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.BasicModule;
import Modules.Cockpit;
import Modules.InteractiveModule;
import Modules.Storage;
import java.awt.Point;
import java.util.ArrayList;
import services.updater.IUpdateable;
import universe.Abs_ChunkNode;

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

    public BasicShip(Main app) {
        super(app, "BasicShip", Abs_ChunkNode.ChunkNodeType.Ship);
        app.getRootNode().attachChild(this);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].update(tpf);
                }
            }
        }
    }

    public Main getApp() {
        return this.app;
    }

    public void addModuleAtFromOffset(BasicModule module, Point offset) {
        Point p = offsetToActual(offset);
        modules[p.x][p.y] = module;

        module.onPlaced(this);
        informOtherModulesOfAddedModule(module, p);
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
        //sperateInNewShips();
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

    public void collectItem(Item item) {
        // check for available item storage
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] instanceof Storage) {
                    if (((Storage) modules[i][j]).storeItem(item)) {
                        break;
                    }
                }
            }
        }
    }

    private Point offsetToActual(Point offset) {
        return new Point(modules.length / 2 - offset.y, modules.length / 2 + offset.x);
    }

    public ArrayList<InteractiveModule> getInteractiveModules() {
        return interactiveModules;
    }

    public ArrayList<InteractiveModule> getInteractiveModulesWithHotkey(String hotkey) {
        ArrayList<InteractiveModule> ims = new ArrayList<InteractiveModule>();
        for (InteractiveModule im : getInteractiveModules()) {
            for (String s : im.getHotkeys()) {
                if (s.equals(hotkey)) {
                    ims.add(im);
                }
            }
        }
        return ims;
    }

    public void activateModules(String hotkey) {
        for (InteractiveModule im : getInteractiveModulesWithHotkey(hotkey)) {
            im.activate();
        }
    }

    public void deactivateModules(String hotkey) {
        for (InteractiveModule im : getInteractiveModulesWithHotkey(hotkey)) {
            im.deactivate();
        }
    }

    public void sperateInNewShips() {
        ArrayList<BasicShip> ships = new ArrayList<BasicShip>();

        boolean[][] alreadyAddedModules = new boolean[shipHeight][shipWidth];
        BasicModule[][] ms = new BasicModule[shipHeight][shipWidth];

        for (int i = 0; i < modules.length; i++) {
            System.arraycopy(modules[i], 0, ms[i], 0, modules[i].length);
        }

        if (cockpit != null) {
            floodFill(alreadyAddedModules, ms, getActualPositionInGrid(cockpit));
        }
        printBool(alreadyAddedModules);
        System.out.println();
    }

    public void floodFill(boolean[][] alreadyAdded, BasicModule[][] ms, Point start) {
        if(ms[start.x][start.y] != null && !alreadyAdded[start.x][start.y]) {
            
            ms[start.x][start.y] = modules[start.x][start.y];
            alreadyAdded[start.x][start.y] = true;
            if (start.x - 1 > 0) {
                floodFill(alreadyAdded, ms, new Point(start.x - 1, start.y));
            }
            if (start.x + 1 < ms[start.y].length) {
                floodFill(alreadyAdded, ms, new Point(start.x + 1, start.y));
            }
            if (start.y - 1 > 0) {
                floodFill(alreadyAdded, ms, new Point(start.x, start.y - 1));
            }
            if (start.y + 1 < ms.length) {
                floodFill(alreadyAdded, ms, new Point(start.x, start.y + 1));
            }
        }
    }
    
    public void printBool(boolean[][] b) {
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                String s;
                if(!b[i][j]) {
                    s = "-";
                } else {
                    s = "x";
                }
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }
}
