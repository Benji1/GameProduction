package mygame;

import services.ServiceManager;
import ShipDesigns.TestShipDesigns;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;
import gui.GUI;
import items.EncapsulatingItem;
import items.Item;
import java.util.ArrayList;
import org.jbox2d.dynamics.Body;
import services.updater.UpdateableManager;
import universe.Background;
import universe.Universe;
import universe.UniverseGenerator;
import weapons.Projectile;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    public CameraNode camNode;
    private Universe u;
    private Background background;
    public BitmapText textShipPos;
    public BitmapText textNewChunk;
    protected float shipSpeed = 0;
    protected float shipRotation = 1.5f;
    protected int rotDir = 0;
    protected float maxSpeed = 5f;
    private GUI gui;
    public ArrayList<BasicShip> ships = new ArrayList<BasicShip>();
    public BasicShip playersShip;
    public BasicShip targetShip;
    
    UpdateableManager updateableManager = ServiceManager.getUpdateableManager();
    
    public ArrayList<Body> bodiesToRemove = new ArrayList<Body>();
    public ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public ArrayList<EncapsulatingItem> itemsToCreate = new ArrayList<EncapsulatingItem>();
    public ArrayList<Item> itemsToRemove = new ArrayList<Item>();
    
    private float cameraHeight = 0f;
    float camXOffset = -20f; // Camera X
    float camZOffset = 20f;  // Camera Y, should at least be 0.1f so that the camera isn't inside the ship
    float camYOffset = 20f;  // Camera height
    boolean universeDebug = false;

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("GameProduction Project");
        settings.setResolution(1600, 900);
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
        this.initWorld();
        this.initShip();
        this.initLight();
        this.initKeys();
        this.initHUD();
        this.initCamera();
        this.background = new Background(this);
        this.background.initBackground();
        this.gui = new GUI(this);
    }

    private void initShip() {
        TestShipDesigns tsd = new TestShipDesigns(this);
        playersShip = tsd.createTestShip1();
        //playersShip = tsd.createStickShip();
        //playersShip = tsd.createBasicShip();
        targetShip = tsd.createTestTargetShip2();
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        camNode = new CameraNode("Camera Node", viewPort.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.rootNode.attachChild(camNode);

        cameraHeight = camYOffset * (this.viewPort.getCamera().getWidth() / 1600f);
        if (this.playersShip.cockpit != null) {
            UpdateCamPos();
        }
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN), new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Weapon", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shield", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("ToggleEditor", new KeyTrigger(KeyInput.KEY_E));

        inputManager.addListener(this, "Up", "Left", "Right", "Down", "Weapon", "Shield", "ToggleUniverseDebug", "ToggleEditor");
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
        ambient.setColor(ColorRGBA.White.mult(1f));
        rootNode.addLight(ambient);
    }
    boolean up = false, down = false;

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Up")) {
            playersShip.activateModules("Up");
            up = true;
            if (!keyPressed) {
                playersShip.deactivateModules("Up");
                up = false;
            }
        }

        if (name.equals("Left")) {
            playersShip.activateModules("Left");
            if (!keyPressed) {
                playersShip.deactivateModules("Left");
                if (up) {
                    playersShip.activateModules("Up");
                } else if (down) {
                    playersShip.activateModules("Down");
                }
            }
        }

        if (name.equals("Right")) {
            playersShip.activateModules("Right");
            if (!keyPressed) {
                playersShip.deactivateModules("Right");
                if (up) {
                    playersShip.activateModules("Up");
                } else if (down) {
                    playersShip.activateModules("Down");
                }
            }
        }

        if (name.equals("Down")) {
            playersShip.activateModules("Down");
            down = true;
            if (!keyPressed) {
                playersShip.deactivateModules("Down");
                down = false;
            }
        }

        if (name.equals("Weapon")) {
            playersShip.activateModules("Weapon");
            if (!keyPressed) {
                playersShip.deactivateModules("Weapon");
            }
        }

        if (name.equals("Shield") && !keyPressed) {
            // TODO: improve bool test
            if (playersShip.getInteractiveModulesWithHotkey("Shield").size() > 0 && playersShip.getInteractiveModulesWithHotkey("Shield").get(0) != null) {
                if (playersShip.getInteractiveModulesWithHotkey("Shield").get(0).isActive()) {
                    playersShip.deactivateModules("Shield");
                    targetShip.deactivateModules("Shield");
                } else {
                    playersShip.activateModules("Shield");
                    targetShip.activateModules("Shield");
                }
            }



        }

        if (name.equals("ToggleUniverseDebug")) {
            if (!keyPressed) {
                //System.out.println(camNode.getLocalTranslation().y + "/ " + 70 * (this.viewPort.getCamera().getWidth() / 1280f));
                // if (camNode.getLocalTranslation().y == 70 * (this.viewPort.getCamera().getWidth() / 1600f)) {
                if (universeDebug) {
                    universeDebug = false;
                    //camNode.setLocalTranslation(new Vector3f(0, 200 * (this.viewPort.getCamera().getWidth() / 1600f), 0.1f));
                    //this.u.toggleUniverseDebug();
                    guiNode.attachChild(this.textShipPos);
                    guiNode.attachChild(this.textNewChunk);
                } else {
                    camNode.setLocalTranslation(new Vector3f(0, 70 * (this.viewPort.getCamera().getWidth() / 1600f), 0.1f));
                    //this.u.toggleUniverseDebug();
                    guiNode.detachChild(this.textShipPos);
                    guiNode.detachChild(this.textNewChunk);
                    universeDebug = true;
                }
            }
        }

        if (name.equals("ToggleEditor") && !keyPressed) {
            if (!gui.getCurrentScreenId().equals("editor")) {
                gui.goToEditorScreen();
            } else {
                gui.goToStartScreen();
            }
        }
    }

    @Override
    public void simpleUpdate(float delta) {
        phyicsUpdate(delta);

        for (BasicShip s : ships) {
            s.update(delta);
        }
        
        //System.out.println(ships.size());
        updateableManager.update(delta);

        for (Body b : bodiesToRemove) {
            //System.out.println(b);
            PhysicsWorld.world.destroyBody(b);
        }
        bodiesToRemove.clear();

        while (!projectilesToRemove.isEmpty()) {
            Projectile p = projectilesToRemove.get(0);
            projectilesToRemove.remove(0);
            p.delete();
        }
        
        while (!itemsToCreate.isEmpty()) {
            EncapsulatingItem encItem =  itemsToCreate.remove(0);
            encItem.init();
        }
        
        while (!itemsToRemove.isEmpty()) {
            Item p = itemsToRemove.get(0);
            itemsToRemove.remove(0);
            p.delete();
        }
        
        
        
        
        this.u.update(delta);
        this.background.updateBackground();
        // update camera position

        if (this.playersShip != null && this.playersShip.cockpit != null && !universeDebug) {
            UpdateCamPos();
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Universe getUniverse() {
        return this.u;
    }

    public void phyicsUpdate(float delta) {
        PhysicsWorld.world.step(delta, 8, 8);
    }
    Vector3f previousCamPos;
    Vector3f currentCamPos = new Vector3f();
    float camPosChangeLerpValue = 0.03f;

    public void UpdateCamPos() {
        previousCamPos = currentCamPos;
        currentCamPos = new Vector3f();

        float min = 0.1f;
        float max = 100f;
        float speedFactor = this.playersShip.cockpit.getBody().getLinearVelocity().lengthSquared() * 0.1f;

        speedFactor = Math.max(min, speedFactor);
        speedFactor = Math.min(speedFactor, max);
        float t = inverseLerp(0f, max + min, speedFactor);
        float offsetFactor = 1f - t;

        currentCamPos.x = this.playersShip.cockpit.getLocalTranslation().x + camXOffset * offsetFactor;
        currentCamPos.z = this.playersShip.cockpit.getLocalTranslation().z + camZOffset * offsetFactor;

        float newY =
                lerp(
                previousCamPos.y,
                this.cameraHeight + speedFactor,
                camPosChangeLerpValue);

        if (!up || newY > previousCamPos.y) {
            currentCamPos.y = newY;
        } else {
            currentCamPos.y = previousCamPos.y;
        }

        camNode.setLocalTranslation(currentCamPos);
        camNode.lookAt(this.playersShip.cockpit.getLocalTranslation(), Vector3f.UNIT_Y);

    }

    float lerp(float v0, float v1, float t) {
        return (1 - t) * v0 + t * v1;
    }

    float inverseLerp(float a, float b, float x) {
        return (x - a) / (b - a);
    }
}