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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import gui.ModuleType;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
    private Body body;
    public Cockpit cockpit;    
    public Vector3f cockpitPos;
    public int colliderType, collidingWith;
    private Player player;
    private int activatedThrusterCount;    

    public BasicShip(Main app, String name) {
        this(app, name, null);
    }
    public BasicShip(Main app, String name, Player player) {
        super(app, name, Abs_ChunkNode.ChunkNodeType.Ship);
        app.getRootNode().attachChild(this);
        app.ships.add(this); 
        this.player = player;
        activatedThrusterCount = 0;
    }
    
    public Body getBody() {
        return body;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
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
    
    public void ToggleDamping(){
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].toggleDamping();
                }
            }
        }
    }
    
    public BasicModule[][] getModules() {
        return modules;
    }

    public Main getApp() {
        return this.app;
    }

    public void setColliderTypeAndWith(int type, int with) {
        colliderType = type;
        collidingWith = with;
    }

    public void addModuleAt(BasicModule module, Point p) {
        modules[p.x][p.y] = module;

        module.onPlaced(this);
        informOtherModulesOfAddedModule(module, p);
    }
    
    public void addModuleAtBody(BasicModule module, Point p) {
        modules[p.x][p.y] = module;
        generatePhysicsBody(p.x, p.y, this.colliderType, this.collidingWith);
        module.onPlaced(this);
        informOtherModulesOfAddedModule(module, p);
    }

    public void addModuleAtFromOffset(BasicModule module, Point offset) {
        addModuleAt(module, offsetToActual(offset));
    }
    
    public void addModuleAtFromOffsetBody(BasicModule module, Point offset) {
        addModuleAtBody(module, offsetToActual(offset));
    }
    
    private void generatePhysicsBody(int x, int y, int colliderType, int collidingWith) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(1, 1);//, new Vec2(x, y), 0);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        fDef.filter.categoryBits = colliderType;
        fDef.filter.maskBits = collidingWith;

        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyType.DYNAMIC;

        this.body = PhysicsWorld.world.createBody(bDef);
        this.body.createFixture(fDef);
        this.body.setUserData(this);
        //body.setLinearDamping(linearDampingFactor);
        PhysicsWorld.world.setContactListener(cockpit);
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

    public void sperateInNewShips() {
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
                newShip.setColliderTypeAndWith(colliderType, collidingWith);
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

    public void onShipChanged(BasicModule[][] modulesNewShip) {
        this.cockpitPos = this.cockpit.getBodyPos().clone();
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
                if (modules[i][j] != null) {
                    modules[i][j].destroyWithoutSeperation();
                }
            }
        }
        modules = new BasicModule[modulesNewShip.length][modulesNewShip[0].length];
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
            	if(modulesNewShip[i][j] != null && modulesNewShip[i][j] instanceof Cockpit) {
            		System.out.println("cp pos: "+i+" "+j);
            		this.cockpitPos = new Vector3f(cockpitPos.x - 2*i, 0, cockpitPos.z - 2*j);
            	}
            }
        }

        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].length; j++) {
                if(modulesNewShip[i][j] != null) {
                    addModuleAt(modulesNewShip[i][j], new Point(i, j));
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
}
