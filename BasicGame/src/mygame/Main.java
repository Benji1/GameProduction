package mygame;

import Modules.Weapon;
import Modules.EnergyGenerator;
import Modules.Armor;
import Modules.Cockpit;
import Modules.LaserGun;
import Modules.Shield;
import Modules.Storage;
import Modules.Thruster;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

import config.ConfigReader;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import universe.Universe;
import universe.UniverseGenerator;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    private CameraNode camNode;
    public BasicShip s;
    public BasicShip targetS;
    private Universe u;
    private List<Body> bodies = new ArrayList<Body>();
    private List<Updateable> updateables = new ArrayList<Updateable>();
    public BitmapText textShipPos;
    public BitmapText textNewChunk;
    protected float shipSpeed = 0;
    protected float shipRotation = 1.5f;
    protected int rotDir = 0;
    protected float maxSpeed = 5f;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("GameProduction Project");
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        settings.setBitsPerPixel(24);
        settings.setSamples(16);

        Main app = new Main();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        ConfigReader.init();
        
        this.initWorld();
        this.initShip();
        this.initLight();
        this.initKeys();
        this.initHUD();
        this.initCamera();
    }

    private void initShip() {
        s = new BasicShip(assetManager, this, "PlayerShip");
        rootNode.attachChild(s);

        Cockpit cockpit = new Cockpit();
        s.addModule(cockpit, new Point(s.modules.length / 2, s.modules.length / 2));
        cockpit.addToShip();

        Armor armor1 = new Armor();
        s.addModule(armor1, new Point(s.modules.length / 2, s.modules.length / 2 - 1));
        armor1.lockToShip();

        Armor armor2 = new Armor();
        s.addModule(armor2, new Point(s.modules.length / 2, s.modules.length / 2 + 1));
        armor2.lockToShip();

        Weapon weapon = new LaserGun(0);
        s.addModule(weapon, new Point(s.modules.length / 2 - 2, s.modules.length / 2));
        weapon.lockToShip();

        Thruster thruster = new Thruster(2);
        s.addModule(thruster, new Point(s.modules.length / 2 + 2, s.modules.length / 2));
        thruster.lockToShip();

        Thruster thruster2 = new Thruster(1);
        s.addModule(thruster2, new Point(s.modules.length / 2 + 1, s.modules.length / 2 + 1));
        thruster2.lockToShip();

        Thruster thruster3 = new Thruster(3);
        s.addModule(thruster3, new Point(s.modules.length / 2 + 1, s.modules.length / 2 - 1));
        thruster3.lockToShip();

        Shield shield = new Shield();
        s.addModule(shield, new Point(s.modules.length / 2 - 1, s.modules.length / 2));
        shield.lockToShip();

        EnergyGenerator eg = new EnergyGenerator();
        s.addModule(eg, new Point(s.modules.length / 2 + 1, s.modules.length / 2));
        eg.lockToShip();

        Storage storage1 = new Storage();
        s.addModule(storage1, new Point(s.modules.length / 2 - 1, s.modules.length / 2 - 1));
        storage1.lockToShip();

        Storage storage2 = new Storage();
        s.addModule(storage2, new Point(s.modules.length / 2 - 1, s.modules.length / 2 + 1));
        storage2.lockToShip();


        //EnergyGenerator eg2 = new EnergyGenerator();
        //s.addModule(eg2, new Point(s.modules.length / 2, s.modules.length / 2 + 2));
        //eg2.lockToShip();

        //s.print();

        //eg.printModules();
        //eg2.printModules();


        targetS = new BasicShip(assetManager, this, "TargetShip");
        rootNode.attachChild(targetS);

        Cockpit cockpitTs = new Cockpit();
        targetS.addModule(cockpitTs, new Point(targetS.modules.length / 2 + 2, targetS.modules.length / 2 + 2));
        cockpitTs.addToShip();

        Armor armorTs1 = new Armor();
        targetS.addModule(armorTs1, new Point(targetS.modules.length / 2 + 1, targetS.modules.length / 2 + 2));
        armorTs1.lockToShip();

        Armor armorTs2 = new Armor();
        targetS.addModule(armorTs2, new Point(targetS.modules.length / 2, targetS.modules.length / 2 + 2));
        armorTs2.lockToShip();
        
        //targetS.print();
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        camNode = new CameraNode("Camera Node", viewPort.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.rootNode.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(this.s.getLocalTranslation().x, 70 * (this.viewPort.getCamera().getWidth() / 1280f), this.s.getLocalTranslation().z + 0.1f));
        camNode.lookAt(this.s.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Weapon", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shield", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));

        inputManager.addListener(this, "Up", "Left", "Right", "Weapon", "Shield", "ToggleUniverseDebug");
    }

    private void initHUD() {
        this.textShipPos = new BitmapText(guiFont, false);
        this.textShipPos.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        this.textShipPos.setColor(ColorRGBA.Green);                             // font color
        this.textShipPos.setText("POS");             // the text
        this.textShipPos.setLocalTranslation(0, this.settings.getHeight(), 0); // position

        this.textNewChunk = new BitmapText(guiFont, false);
        this.textNewChunk.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        this.textNewChunk.setColor(ColorRGBA.Green);                             // font color
        this.textNewChunk.setText("CHUNK UPDATES\n");             // the text
        this.textNewChunk.setLocalTranslation(this.settings.getWidth() - 250, this.settings.getHeight(), 0); // position
    }

    private void initWorld() {
    	this.u = new Universe(this);
    	
        UniverseGenerator.debugSystem(this, u);
    }

    private void initLight() {
    	AmbientLight ambient = new AmbientLight();
    	ambient.setColor(ColorRGBA.White.mult(0.2f));
    	rootNode.addLight(ambient);
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Up")) {
            s.getInteractiveModule(new Point(s.modules.length / 2 + 2, s.modules.length / 2)).activate();
            if (!keyPressed) {
                s.getInteractiveModule(new Point(s.modules.length / 2 + 2, s.modules.length / 2)).deactivate();
            }
        }
        if (name.equals("Left")) {
            s.getInteractiveModule(new Point(s.modules.length / 2 + 1, s.modules.length / 2 + -1)).activate();
            if (!keyPressed) {
                s.getInteractiveModule(new Point(s.modules.length / 2 + 1, s.modules.length / 2 + -1)).deactivate();
            }
        }
        if (name.equals("Right")) {
            s.getInteractiveModule(new Point(s.modules.length / 2 + 1, s.modules.length / 2 + 1)).activate();
            if (!keyPressed) {
                s.getInteractiveModule(new Point(s.modules.length / 2 + 1, s.modules.length / 2 + 1)).deactivate();
            }
        }

        if (name.equals("Weapon")) {
            s.getInteractiveModule(new Point(s.modules.length / 2 - 2, s.modules.length / 2)).activate();
            if (!keyPressed) {
                s.getInteractiveModule(new Point(s.modules.length / 2 - 2, s.modules.length / 2)).deactivate();
            }
        }

        if (name.equals("Shield") && !keyPressed) {
            if (s.getInteractiveModule(new Point(s.modules.length / 2 - 1, s.modules.length / 2)).isActive()) {
                s.getInteractiveModule(new Point(s.modules.length / 2 - 1, s.modules.length / 2)).deactivate();
            } else {
                s.getInteractiveModule(new Point(s.modules.length / 2 - 1, s.modules.length / 2)).activate();
            }
        }

        if (name.equals("ToggleUniverseDebug")) {
            if (!keyPressed) {
            	System.out.println(camNode.getLocalTranslation().y + "/ " + 70 * (this.viewPort.getCamera().getWidth() / 1280f));
                if (camNode.getLocalTranslation().y == 70 * (this.viewPort.getCamera().getWidth() / 1280f)) {
                    camNode.setLocalTranslation(new Vector3f(0, 200 * (this.viewPort.getCamera().getWidth() / 1280f), 0.1f));
                    this.u.toggleUniverseDebug();
                    guiNode.attachChild(this.textShipPos);
                    guiNode.attachChild(this.textNewChunk);
                } else {
                    camNode.setLocalTranslation(new Vector3f(0, 70 * (this.viewPort.getCamera().getWidth() / 1280f), 0.1f));
                    this.u.toggleUniverseDebug();
                    guiNode.detachChild(this.textShipPos);
                    guiNode.detachChild(this.textNewChunk);
                }
            }
        }
    }

    public void addUpdateable(Updateable u) {
        updateables.add(u);
    }

    public void removeUpdateable(Updateable u) {
        updateables.remove(u);
    }

    @Override
    public void simpleUpdate(float delta) {
    	phyicsUpdate(delta);
        this.u.update(delta);
        s.update(delta);
        targetS.update(delta);

        for (int i = 0; i < updateables.size(); i++) {
            if (updateables.get(i) != null) {
                updateables.get(i).update(delta);
            }
        }
        
        // update camera position
        camNode.setLocalTranslation(new Vector3f(this.s.getLocalTranslation().x, this.camNode.getLocalTranslation().y, this.s.getLocalTranslation().z + 0.1f));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Universe getUniverse() {
        return this.u;
    }

    public Spatial generateCrapBox() {
        Box box1 = new Box(1, 1, 1);
        Geometry blue = new Geometry("Box", box1);
        blue.setLocalTranslation(new Vector3f(0, -0, 0));
        Material mat1 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        blue.setMaterial(mat1);
        rootNode.attachChild(blue);

        return blue;
    }

    public Vector3f Vector2ToVector3f(Vec2 v2) {
        Vector3f returnVec = new Vector3f();
        returnVec.x = (float) v2.x;
        returnVec.y = 0;
        returnVec.z = (float) v2.y;
        return returnVec;
    }

    public Vec2 Vector3fToVector2(Vector3f v3) {
        Vec2 returnVec = new Vec2();
        returnVec.x = v3.x;
        returnVec.y = v3.z;
        return returnVec;
    }

    public Vec2 getBodyPos(Body b) {
        return b.getWorldPoint(b.getLocalCenter());
    }

    public void phyicsUpdate(float delta) {
        //testBody.applyForce(new Vec2(-51f, 0f), testBody.getLocalCenter());

        // Vector3f crapV2 = Vector2ToVector3f(getBodyPos(testBody));
        //testBox.setLocalTranslation(crapV2);

        PhysicsWorld.world.step(delta, 8, 8);
    }
}