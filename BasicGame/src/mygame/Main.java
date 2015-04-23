package mygame;

import Modules.Weapon;
import Modules.EnergyGenerator;
import Modules.Armor;
import Modules.Cockpit;
import Modules.Shield;
import Modules.Thruster;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import config.ConfigReader;
import java.awt.Point;
import universe.Universe;


/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    private CameraNode camNode;
    public BasicShip s;
    private Universe u;
    
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
        settings.setSamples(32);
        
        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        ConfigReader.init();
        this.u = new Universe(this);
        this.initShip();
        this.initWorld();
        this.initLight();
        this.initCamera();
        this.initKeys();
        this.initHUD();
    }
    
    private void initShip() {
        s = new BasicShip(assetManager, this);
        rootNode.attachChild(s);

        s.addModule(new Cockpit(), new Point(s.modules.length / 2, s.modules.length / 2));
        s.addModule(new Armor(), new Point(s.modules.length / 2, s.modules.length / 2 - 1));
        s.addModule(new Armor(), new Point(s.modules.length / 2, s.modules.length / 2 + 1));

        s.addModule(new Weapon(), new Point(s.modules.length / 2 - 2, s.modules.length / 2));
        s.addModule(new Thruster(), new Point(s.modules.length / 2 + 2, s.modules.length / 2));
        s.addModule(new Shield(), new Point(s.modules.length / 2 - 1, s.modules.length / 2));

        EnergyGenerator eg = new EnergyGenerator();
        s.addModule(eg, new Point(s.modules.length / 2 + 1, s.modules.length / 2));

        EnergyGenerator eg2 = new EnergyGenerator();
        s.addModule(eg2, new Point(s.modules.length / 2, s.modules.length / 2 + 2));

        //s.print();

        //eg.printModules();
        //eg2.printModules();
    }
    
    private void initCamera() {
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        camNode = new CameraNode("Camera Node", viewPort.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.s.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 70 * (this.viewPort.getCamera().getWidth() / 1280f), 0.1f));
        camNode.lookAt(this.s.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));
        
        inputManager.addListener(this, "Up", "Left", "Right", "ToggleUniverseDebug");
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
        this.textNewChunk.setLocalTranslation(this.settings.getWidth() -250, this.settings.getHeight(), 0); // position
    }
    
    private void initWorld() {
        // testbox
        Box box1 = new Box(1, 1, 1);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setColor("Diffuse", ColorRGBA.Blue);
        mat1.setColor("Specular", ColorRGBA.Blue);

        for (int i = 0; i < 1000; i++) {
            Geometry blue = new Geometry("Box", box1);
            blue.setLocalTranslation(new Vector3f(((float) Math.random() - 0.5f) * 1000, -15, ((float) Math.random() - 0.5f) * 1000));
            blue.setMaterial(mat1);
            rootNode.attachChild(blue);

            Geometry blue2 = new Geometry("Box", box1);
            blue2.setLocalTranslation(new Vector3f(((float) Math.random() - 0.5f) * 1000, 10, ((float) Math.random() - 0.5f) * 1000));
            blue2.setMaterial(mat1);
            rootNode.attachChild(blue2);
        }
    }
    
    private void initLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
    }
    
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Up")) {
            shipSpeed = 0.25f;
            if (!keyPressed) {
                shipSpeed = 0f;
            }
        }
        if (name.equals("Left")) {
            this.rotDir = 1;
            if (!keyPressed) {
                this.rotDir = 0;
            }
        }
        if (name.equals("Right")) {
            this.rotDir = -1;
            if (!keyPressed) {
                this.rotDir = 0;
            }
        }
        if (name.equals("ToggleUniverseDebug")) {
        	if (!keyPressed) {
	        	if(camNode.getLocalTranslation().y == 70 * (this.viewPort.getCamera().getWidth() / 1280f)) {
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

    
    @Override
    public void simpleUpdate(float tpf) {
    	this.u.update(tpf);
        s.update(tpf);
        
        // update movement
        Vector3f lookDir = this.s.getLocalRotation().mult(Vector3f.UNIT_Z).mult(-1).mult(shipSpeed).clone();
        this.s.move(lookDir);

        // update rotation
        this.s.rotate(0, tpf * speed * shipRotation * rotDir, 0);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    public Universe getUniverse() {
        return this.u;
    }
}
