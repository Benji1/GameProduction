/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.BasicModule;
import Modules.Cockpit;
import Modules.InteractiveModule;
import com.jme3.asset.AssetManager;
import java.awt.Point;
import universe.Abs_ChunkNode;

/**
 *
 * @author 1337
 */
public class BasicShip extends Abs_ChunkNode {

    public BasicModule[][] modules = new BasicModule[5][5];
    public AssetManager assetManager;
    public Cockpit cockpit;

    public BasicShip(AssetManager assetManager, Main app) {
        super(app, "BasicShip", Abs_ChunkNode.ChunkNodeType.Ship);
        this.assetManager = assetManager;          
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

    public void addModule(BasicModule module, Point p) {
        modules[p.x][p.y] = module;
        module.onPlaced(this);
    }

    public void removeModule(BasicModule m) {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] == m) {
                    modules[i][j] = null;
                }
            }
        }
    }

    public void removeModule(Point p) {
        modules[p.x][p.y] = null;
    }

    public BasicModule getModule(Point p) {
        if (!(p.x < 0 || p.x > modules.length - 1 || p.y < 0 || p.y > modules[0].length - 1)) {
            return modules[p.x][p.y];
        }
        return null;
    }
    
    public InteractiveModule getInteractiveModule(Point p) {
        if (getModule(p) instanceof InteractiveModule) {
            return (InteractiveModule) getModule(p);
        }
        return null;
    }

    public Point getPositionInGrid(BasicModule m) {
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
}
