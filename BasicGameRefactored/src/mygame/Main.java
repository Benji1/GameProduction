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
import services.collision.GameContactListener;
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
    //public BasicShip playersShip;
    public Player player;
    public BasicShip targetShip;
    
    UpdateableManager updateableManager = ServiceManager.getUpdateableManager();
    
    public ArrayList<Body> bodiesToRemove = new ArrayList<Body>();
    public ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public ArrayList<EncapsulatingItem> itemsToCreate = new ArrayList<EncapsulatingItem>();
    public ArrayList<Item> itemsToRemove = new ArrayList<Item>();
    
    private int cameraType = 2; // 0, 1 or 2
    private float cameraMinHeight = 0f;
    private float cameraMaxHeight = 0f;
    float camXOffset = -20f; // Camera X
    float camZOffset = 20f;  // Camera Y, should at least be 0.1f so that the camera isn't inside the ship
    float camYOffset = 20f;  // Camera height
    boolean universeDebug = false;
    
    GameContactListener gameCollisionListener;

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
        this.gameCollisionListener = new GameContactListener();
    }

    private void initShip() {
        TestShipDesigns tsd = new TestShipDesigns(this);        
        player = new Player(this);
        BasicShip ship = tsd.createPlayerShip(player);
        //BasicShip ship = tsd.createStickShip(player);
        player.setShip(ship);
        //playersShip = tsd.createTestShip1();
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

        cameraMinHeight = camYOffset * (this.viewPort.getCamera().getWidth() / 1600f);
        cameraMaxHeight = cameraMinHeight * 5f;
        
        Vector3f tmpPos = camNode.getLocalTranslation();
        tmpPos.y = cameraMinHeight - 2f;
        currentCamPos = tmpPos;
        
        if (this.player.getShip().cockpit != null) {
            UpdateCamPos(0f);
        }
    }

    private void initKeys() {
        inputManager.addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("ToggleEditor", new KeyTrigger(KeyInput.KEY_E));

        inputManager.addListener(this, "ToggleUniverseDebug", "ToggleEditor");
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

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {

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
            if (!gui.getCurrentScreenId().equals(GUI.EDITOR_SCREEN)) {
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
            Item i = itemsToRemove.get(0);
            itemsToRemove.remove(0);
            i.delete();
        }
        
        this.u.update(delta);
        this.background.updateBackground();
        // update camera position

        if (this.player.getShip() != null && this.player.getShip().cockpit != null && !universeDebug) {
            UpdateCamPos(delta);
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

    final float cooldown = 2f;
    float cameraMoveBackCooldown = 0f;
    float camYspeed = 10f;
    float crap = 0f;
    
    public void UpdateCamPos(float delta) {
        if (cameraType == 0)
        {
            currentCamPos.x = this.player.getShip().cockpit.getLocalTranslation().x;
            currentCamPos.z = this.player.getShip().cockpit.getLocalTranslation().z + 0.1f;
            currentCamPos.y = this.cameraMinHeight * 5f;

            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(this.player.getShip().cockpit.getLocalTranslation(), Vector3f.UNIT_Y);
        }
        else if (cameraType == 1){
            previousCamPos = currentCamPos;
            currentCamPos = new Vector3f();

            float min = 0.1f;
            float max = 100f;
            float speedFactor = this.player.getShip().cockpit.getBody().getLinearVelocity().lengthSquared() * 0.1f;

            speedFactor = Math.max(min, speedFactor);
            speedFactor = Math.min(speedFactor, max);
            float t = inverseLerp(0f, max + min, speedFactor);
            float offsetFactor = 1f - t;

            currentCamPos.x = this.player.getShip().cockpit.getLocalTranslation().x + camXOffset * offsetFactor;
            currentCamPos.z = this.player.getShip().cockpit.getLocalTranslation().z + camZOffset * offsetFactor;

            float newY =
                    lerp(
                    previousCamPos.y,
                    this.cameraMinHeight + speedFactor,
                    camPosChangeLerpValue);

            if (!player.getShip().hasActivatedThruster() || newY > previousCamPos.y) {
                currentCamPos.y = newY;
            } else {
                currentCamPos.y = previousCamPos.y;
            }

            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(this.player.getShip().cockpit.getLocalTranslation(), Vector3f.UNIT_Y);
        }
        else if (cameraType == 2)
        {
            previousCamPos = currentCamPos;
            currentCamPos = new Vector3f();
                        
            float newY =
                    lerp(
                    previousCamPos.y,
                    this.cameraMinHeight + crap,
                    camPosChangeLerpValue);

            
            if (player.getShip().hasActivatedThruster()) 
                cameraMoveBackCooldown = cooldown;
            
            
            if(cameraMoveBackCooldown < 0f || 
                    (player.getShip().hasActivatedThruster() && 
                    newY > previousCamPos.y)) 
                currentCamPos.y = newY;
            else
                currentCamPos.y = previousCamPos.y;
            
            
            if (cameraMoveBackCooldown > 1.9f)
                crap += delta * camYspeed;
            else
                crap -= delta * camYspeed;
            
            crap = Math.max(crap, cameraMinHeight);
            crap = Math.min(crap, cameraMaxHeight);
            
            currentCamPos.x = this.player.getShip().cockpit.getLocalTranslation().x + camXOffset;
            currentCamPos.z = this.player.getShip().cockpit.getLocalTranslation().z + camZOffset;

            
                
                
                
            //*/
            
            cameraMoveBackCooldown -= delta;
            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(this.player.getShip().cockpit.getLocalTranslation(), Vector3f.UNIT_Y);
        }

    }

    float lerp(float v0, float v1, float t) {
        return (1 - t) * v0 + t * v1;
    }

    float inverseLerp(float a, float b, float x) {
        return (x - a) / (b - a);
    }
}