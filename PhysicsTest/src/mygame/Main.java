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
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import java.awt.Point;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import universe.Abs_ChunkNode;
import universe.Universe;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {

    private CameraNode camNode;
    private BasicShip s;
    private Universe u;
    
    private List<Body> bodies = new ArrayList<Body>();
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
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        this.u = new Universe(this);
        this.initShip();
        this.initWorld();
        this.initLight();
        this.initCamera();
        this.initKeys();
        this.initHUD();
        this.initPhysics();
    }

    private void initShip() {
        s = new BasicShip(assetManager, this);
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
        
        /*
        Weapon weapon = new Weapon();
        s.addModule(weapon, new Point(s.modules.length / 2 - 2, s.modules.length / 2));
        weapon.lockToShip();
        
        
        Thruster thruster = new Thruster();
        s.addModule(thruster, new Point(s.modules.length / 2 + 2, s.modules.length / 2));
        thruster.lockToShip();
        */
        Shield shield = new Shield();
        s.addModule(shield, new Point(s.modules.length / 2 - 1, s.modules.length / 2));
        shield.lockToShip();

        EnergyGenerator eg = new EnergyGenerator();
        s.addModule(eg, new Point(s.modules.length / 2 + 1, s.modules.length / 2));
        eg.lockToShip();

        /*
        EnergyGenerator eg2 = new EnergyGenerator();
        s.addModule(eg2, new Point(s.modules.length / 2, s.modules.length / 2 + 2));
        eg2.lockToShip();
        */
        s.print();

        eg.printModules();
        //eg2.printModules();
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        camNode = new CameraNode("Camera Node", viewPort.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.s.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 40 * (this.viewPort.getCamera().getWidth() / 1280f), -0.1f));
        camNode.lookAt(this.s.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(this, "Up", "Left", "Right");
    }

    private void initHUD() {
        this.textShipPos = new BitmapText(guiFont, false);
        this.textShipPos.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        this.textShipPos.setColor(ColorRGBA.Green);                             // font color
        this.textShipPos.setText("POS");             // the text
        this.textShipPos.setLocalTranslation(0, this.settings.getHeight(), 0); // position
        guiNode.attachChild(this.textShipPos);

        this.textNewChunk = new BitmapText(guiFont, false);
        this.textNewChunk.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        this.textNewChunk.setColor(ColorRGBA.Green);                             // font color
        this.textNewChunk.setText("CHUNK UPDATES\n");             // the text
        this.textNewChunk.setLocalTranslation(this.settings.getWidth() - 250, this.settings.getHeight(), 0); // position
        guiNode.attachChild(this.textNewChunk);
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
    }

    @Override
    public void simpleUpdate(float delta) {
        s.update(delta);

        phyicsUpdate(delta);
        // update movement        
        //Vector3f lookDir = this.s.getLocalRotation().mult(Vector3f.UNIT_Z).mult(shipSpeed).clone();
        //this.s.move(lookDir);
        
        // update rotation
        //this.s.rotate(0, tpf * speed * shipRotation * rotDir, 0);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public Universe getUniverse() {
        return this.u;
    }

    Body testBody;
    Spatial testBox;
    public void initPhysics() {
        //PhysicsWorld.world.setGravity(new Vector2(0.0, -9.7));
        testBody = generateBody();
        testBox = generateCrapBox();
        
    }
    
    public Body generateBody(){
        //Rectangle rect = new Rectangle(1, 1);                    
        CircleShape circle = new CircleShape();
        circle.m_radius = 1.0f;
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = circle;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        //fDef.restitution = 0.5f;
        
        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(0, -1);
        bDef.type = BodyType.DYNAMIC;
        
        Body body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        //body.setLinearDamping(0.3);
        //body.setMass(Mass.Type.NORMAL);
        
        
        //PhysicsWorld.world.addBody(body);
        
        //System.out.println("dynamic: " + body.isDynamic());
        
        return body;
    }
    
    public Spatial generateCrapBox()
    {
        Box box1 = new Box(1,1,1);
        Geometry blue = new Geometry("Box", box1);
        blue.setLocalTranslation(new Vector3f(0,-0,0));
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        blue.setMaterial(mat1);
        rootNode.attachChild(blue);
        
        return blue;
    }
    
    public Vector3f Vector2ToVector3f(Vec2 v2)
    {
        Vector3f returnVec = new Vector3f();
        returnVec.x = (float)v2.x;
        returnVec.y = 0;
        returnVec.z = (float)v2.y;
        return returnVec;
    }
    
    public Vec2 Vector3fToVector2(Vector3f v3)
    {
        Vec2 returnVec = new Vec2();
        returnVec.x = v3.x;
        returnVec.y = v3.z;
        return returnVec;
    }
    public Vec2 getBodyPos(Body b)
    {
        return b.getWorldPoint(b.getLocalCenter());
    }
    
    public void phyicsUpdate(float delta) {
        testBody.applyForce(new Vec2(-51f, 0f), testBody.getLocalCenter());
        
        Vector3f crapV2 = Vector2ToVector3f(getBodyPos(testBody));
        testBox.setLocalTranslation(crapV2);
        
        PhysicsWorld.world.step(delta, 8, 8);
    }
}