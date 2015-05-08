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
import universe.Abs_ChunkNode;

/**
 *
 * @author 1337
 */
public class BasicShip extends Abs_ChunkNode {

    public BasicModule[][] modules = new BasicModule[9][9];
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
         if(p !=null) {
            informOtherModulesOfRemovedModule(p);
            //System.out.println("Should remove myself: " + p);
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
                    ((InteractiveModule)modules[i][j]).disable();
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
    
    private Point offsetToActual (Point offset) {
        return new Point(modules.length / 2 - offset.y, modules.length / 2 + offset.x);
    }
    
    public ArrayList<InteractiveModule> getInteractiveModules() {
        ArrayList<InteractiveModule> ims = new ArrayList<InteractiveModule>();
         for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] instanceof InteractiveModule) {
                    ims.add((InteractiveModule) modules[i][j]);
                }
            }
        }
        return ims;
    }
    
    public ArrayList<InteractiveModule> getInteractiveModulesWithHotkey(String hotkey) {
        ArrayList<InteractiveModule> ims = new ArrayList<InteractiveModule>();
        for(InteractiveModule im: getInteractiveModules()) {
            for(String s: im.getHotkeys()) {
                if(s.equals(hotkey)) {
                    ims.add(im);
                }
            }
        }
        return ims;
    }
    
    public void activateModules(String hotkey) {
        for(InteractiveModule im: getInteractiveModulesWithHotkey(hotkey)) {
            im.activate();
        }
    }
    
    public void deactivateModules(String hotkey) {
        for(InteractiveModule im: getInteractiveModulesWithHotkey(hotkey)) {
            im.deactivate();
        }
    }
}
