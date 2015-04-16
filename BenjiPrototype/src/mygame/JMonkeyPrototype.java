package mygame;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class JMonkeyPrototype extends SimpleApplication implements ActionListener {

    private static final int _framerate = 60;
    protected Node playerNode;
    private CameraNode camNode;
    protected float shipSpeed = 0;
    protected float shipRotation = 1.5f;
    protected int rotDir = 0;
    protected float maxSpeed = 5f;

    /**
     * Start the jMonkeyEngine application
     */
    public static void main(String[] args) {

        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(_framerate);
        settings.setTitle("JME PROTOTYPE");
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        settings.setBitsPerPixel(24);
        settings.setSamples(32);

        JMonkeyPrototype app = new JMonkeyPrototype();
        app.setSettings(settings);

        Logger.getLogger("").setLevel(Level.SEVERE);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        this.initKeys();
        this.initPlayer();
        this.initCamera();
        this.initWorld();
        this.initLight();
    }

    @Override
    public void simpleUpdate(float tpf) {
        // update movement
        Vector3f v = playerNode.getLocalTranslation().clone();
        Vector3f lookDir = playerNode.getLocalRotation().mult(Vector3f.UNIT_Z).mult(shipSpeed).clone();

        v = new Vector3f(v.x + lookDir.x, 0, v.z + lookDir.z);

        playerNode.setLocalTranslation(v);

        // update rotation
        playerNode.rotate(0, tpf * speed * shipRotation * rotDir, 0);
    }

    private void initKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP), new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT), new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT), new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(this, "Up", "Left", "Right");
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

    private void initPlayer() {
        this.playerNode = new Node("Player");

        Spatial player = assetManager.loadModel("Models/shuttle/shuttle.j3o");
        player.rotate(0, -1.5707963268f, 0); // rotate model 90degree
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/shuttle/texturemap.tga"));
        mat.setTexture("NormalMap", assetManager.loadTexture("Textures/shuttle/normalmap.tga"));
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.White);  // minimum material color
        mat.setColor("Specular", ColorRGBA.White); // for shininess
        mat.setFloat("Shininess", 64f); // [1,128] for shininess
        player.setMaterial(mat);

        this.playerNode.attachChild(player);
        rootNode.attachChild(this.playerNode);
    }

    private void initCamera() {
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        camNode = new CameraNode("Camera Node", viewPort.getCamera());
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        this.playerNode.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 40 * (this.viewPort.getCamera().getWidth() / 1280f), -0.1f));
        camNode.lookAt(this.playerNode.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    private void initWorld() {
        // testbox
        Box box1 = new Box(1, 1, 1);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat1.setColor("Diffuse", ColorRGBA.Blue);
        mat1.setColor("Specular", ColorRGBA.Blue);

        for (int i = 0; i < 20; i++) {
            Geometry blue = new Geometry("Box", box1);
            blue.setLocalTranslation(new Vector3f(((float) Math.random() - 0.5f) * 100, -15, ((float) Math.random() - 0.5f) * 100));
            blue.setMaterial(mat1);
            rootNode.attachChild(blue);

            Geometry blue2 = new Geometry("Box", box1);
            blue2.setLocalTranslation(new Vector3f(((float) Math.random() - 0.5f) * 100, 10, ((float) Math.random() - 0.5f) * 100));
            blue2.setMaterial(mat1);
            rootNode.attachChild(blue2);
        }

        // skybox
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/skybox/skybox.dds", false));
    }

    private void initLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
    }
}