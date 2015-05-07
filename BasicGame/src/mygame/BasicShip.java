/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Modules.BasicModule;
import Modules.Cockpit;
import Modules.InteractiveModule;
import Modules.Storage;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.awt.Point;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import universe.Abs_ChunkNode;

/**
 *
 * @author 1337
 */
public class BasicShip extends JBox2dNode {

    public BasicModule[][] modules = new BasicModule[5][5];
    public AssetManager assetManager;
    //public Cockpit cockpit;

    public BasicShip(AssetManager assetManager, Main app, String name) {
        super(app, name, Abs_ChunkNode.ChunkNodeType.Ship);
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
        
        //if(this.name.equals("PlayerShip"))
        //	System.out.println(this.getLocalTranslation().toString() + " / " + this.physicsCenter.getPosition().toString());
    }
    
    public Main getApp() {
        return this.app;
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
}
