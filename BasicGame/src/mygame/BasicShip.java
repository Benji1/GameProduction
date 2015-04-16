/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.awt.Point;

/**
 *
 * @author 1337
 */
public class BasicShip {

    public BasicModule[][] modules = new BasicModule[5][5];

    public void addModule(BasicModule module, int x, int y) {
        modules[x][y] = module;
        module.onPlaced(x, y);
    }

    public void removeModule(BasicModule m) {
    }

    public BasicModule getModule(int x, int y) {
        if(!(x < 0 || x > modules.length-1 || y < 0 || y > modules[0].length-1)) {
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
                    System.out.print(modules[i][j].name + "\t\t");
                } else {
                    System.out.print("-\t\t");
                }
            }
            System.out.println();
        }
    }
}
