package netclient;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;

import netclient.graphicalModules.GraphicalModule;

import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

import java.util.ArrayList;

import netclient.graphicalModules.GMArmor;
import netclient.graphicalModules.GMArmorDiagonal;
import netclient.graphicalModules.GMCockpit;
import netclient.graphicalModules.ModuleCreator;
import netclient.graphicalModules.GMEnergyGenerator;
import netclient.graphicalModules.GMLaserGun;
import netclient.graphicalModules.GMShieldGenerator;
import netclient.graphicalModules.GMStorage;
import netclient.graphicalModules.GMThruster;
import netclient.graphicalModules.ShipModule;
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
import netutil.NetMessages.PosAndRotMsg;

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
    public ShipModule[][] gmodules;
    public ArrayList<ModuleType> itemsInBase;
    public WJSFClient app;
    private int activatedThrusterCount;
    
    public BitmapText info;

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
        
        this.gmodules = new ShipModule[ship.length][ship[0].length];
        this.velocity = new Vec2();

        BitmapFont f = this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        info = new BitmapText(f, true);
        info.setColor(ColorRGBA.Green);
        info.rotate((float) -Math.PI/2f,0,0);
        info.scale(0.2f);
        info.setQueueBucket(RenderQueue.Bucket.Transparent);
        info.setText(name);
        info.setLocalTranslation(0, 1, 0);
        this.shipRoot.attachChild(info);
        
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
        gmodules = new ShipModule[modules.length][modules[0].length];
        
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[0].length; j++) {
                if (modules[i][j] != null) {
                    gmodules[i][j] = ModuleCreator.createOrientedShipModule(modules[i][j], shipRoot, this, i - modules.length / 2, j - modules[0].length / 2, app);
                }
            }
        }
    }

    private void removeOldGraphics() {
        for (int i = 0; i < gmodules.length; i++) {
            for (int j = 0; j < gmodules[0].length; j++) {
                if (gmodules[i][j] != null) {
                    gmodules[i][j].remove();
                    gmodules[i][j] = null;
                }
            }
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
    
    public void update(float delta) {
        for (int i=0; i<gmodules.length; i++) {
            for (int j=0; j<gmodules[i].length; j++) {
                if (gmodules[i][j] != null) {
                    gmodules[i][j].update();
                }
            }
        }
        deltaAccumulator += delta;
        updatePosAndRot();
    }
    
    private Vector3f lastPos = new Vector3f(), lastLastPos = new Vector3f();
    private Quaternion lastDir = new Quaternion(), lastLastDir = new Quaternion();
    private float deltaAccumulator, lastDeltaAcc, lastLastDeltaAcc;
    
    public void handlePosAndRotMsg(PosAndRotMsg msg) {
        lastLastPos = lastPos;
        lastLastDir = lastDir;
        lastLastDeltaAcc = lastDeltaAcc;
        lastPos = msg.getPos();
        lastDir = msg.getDir();
        lastDeltaAcc = deltaAccumulator;
    }
    
    private void updatePosAndRot() {
        float deltaTime = lastDeltaAcc - lastLastDeltaAcc;
        float newDeltaTime = deltaAccumulator - lastLastDeltaAcc;

        Vector3f predictedPathVector = lastPos.subtract(lastLastPos);
        Vector3f deltaVelocity = predictedPathVector.divide(deltaTime);
        Vector3f pos = lastPos.add(deltaVelocity.mult(newDeltaTime));
        shipRoot.setLocalTranslation(pos);
        
        Quaternion predictedDir = lastDir.subtract(lastLastDir);
        Quaternion deltaAngularVelocity = predictedDir.mult(1f / deltaTime);
        Quaternion rotation = lastDir.add(deltaAngularVelocity.mult(newDeltaTime));
        shipRoot.setLocalRotation(rotation);
    }
    
    /*private Vector3f[] lastKnownPos = new Vector3f[3];
    private Quaternion[] lastKnownDir = new Quaternion[3];
    private float[] lastKnownTime = new float[3];
    private float deltaAccumulator;
    
    public void handlePosAndRotMsg(PosAndRotMsg msg) {
        lastKnownPos[2] = lastKnownPos[1];
        lastKnownPos[1] = lastKnownPos[0];
        lastKnownPos[0] = msg.getPos();
        lastKnownDir[2] = lastKnownDir[1];
        lastKnownDir[1] = lastKnownDir[0];        
        lastKnownDir[0] = msg.getDir();
        lastKnownTime[2] = lastKnownTime[1];
        lastKnownTime[1] = lastKnownTime[0];
        lastKnownTime[0] = deltaAccumulator;
    }
    
    private void updatePosAndRot() {
        //PrevPredictedPathVector = Last_KnownPos[1] – Last_KnownPos[2]
        Vector3f prevPredictedPathVector = lastKnownPos[1].subtract(lastKnownPos[2]);
        //PrevDeltaTime = Last_KnownTime[1] – Last_KnownTime[2]
        float prevDeltaTime = lastKnownTime[1] - lastKnownTime[2];
        //StartDeltaTime = Last_KnownTime[0] – Last_KnownTime[2]
        float startDeltaTime = lastKnownTime[0] - lastKnownTime[2];
        //PrevVelocity = PrevPredictedPathVector / PrevDeltaTime
        Vector3f prevVelocity = prevPredictedPathVector.divide(prevDeltaTime);
        //StartPos = Last_KnownPos[2] + StartDeltaTime * PrevVelocity
        Vector3f startPos = lastKnownPos[2].add(prevVelocity.mult(startDeltaTime));
        //CurrentDeltaTime = Now - Last_KnownTime[0]
        float currentDeltaTime = deltaAccumulator - lastKnownTime[0];
        //PredictedPathVector = Last_KnownPos[0] – Last_KnownPos[1]
        Vector3f predictedPathVector = lastKnownPos[0].subtract(lastKnownPos[1]);
        //DeltaTime = Last_KnownTime[0] – Last_KnownTime[1]
        float deltaTime = lastKnownTime[0] - lastKnownTime[1];
        //EndPos = Last_KnownPos[0] + CurrentDeltaTime * PredictedPathVector / DeltaTime
        Vector3f endPos = lastKnownPos[0].add(predictedPathVector.divide(deltaTime).mult(currentDeltaTime));
        //LinearConvergenceVector = EndPos - StartPos
        Vector3f linearConvergenceVector = endPos.subtract(startPos);
        //Velocity = LinearConvergenceVector / DeltaTime
        Vector3f velocity = linearConvergenceVector.divide(deltaTime);
        //NewPos = StartPos + CurrentDeltaTime * Velocity
        Vector3f newPos = startPos.add(velocity.mult(currentDeltaTime));
        shipRoot.setLocalTranslation(newPos);
        
        Quaternion predictedDir = lastKnownDir[0].subtract(lastKnownDir[1]);
        Quaternion deltaAngularVelocity = predictedDir.mult(1f / deltaTime);
        Quaternion rotation = lastKnownDir[0].add(deltaAngularVelocity.mult(currentDeltaTime));
        shipRoot.setLocalRotation(rotation);
    }*/
}
