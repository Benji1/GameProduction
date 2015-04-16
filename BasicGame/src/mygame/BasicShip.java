/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.BasicModule;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.awt.Point;

/**
 *
 * @author 1337
 */
public class BasicShip extends Node {

    public BasicModule[][] modules = new BasicModule[5][5];
    public AssetManager assetManager;

    public BasicShip(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void update() {
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].update();
                }
            }
        }
    }

    public void addModule(BasicModule module, int x, int y) {
        modules[x][y] = module;
        module.onPlaced(x, y, this);
    }

    public void removeModule(BasicModule m) {
    }

    public BasicModule getModule(int x, int y) {
        if (!(x < 0 || x > modules.length - 1 || y < 0 || y > modules[0].length - 1)) {
            return modules[x][y];
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
                    System.out.print(modules[i][j].getName() + "\t\t");
                } else {
                    System.out.print("-\t\t");
                }
            }
            System.out.println();
        }
    }
}
