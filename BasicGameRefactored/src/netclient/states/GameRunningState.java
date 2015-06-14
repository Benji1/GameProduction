package netclient.states;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import netclient.ClientNetMsgListener;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.GUI;
import netclient.universe.Background;
import netserver.BasicShip;
import netserver.WJSFServer;
import netserver.physics.PhysicsWorld;
import netserver.services.ServiceManager;
import netserver.services.updater.UpdateableManager;
import netserver.shipdesigns.TestShipDesigns;
import netserver.universe.Universe;
import netserver.universe.UniverseGenerator;
import netutil.NetMessages.KeyPressedMsg;

import org.jbox2d.dynamics.Body;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GameRunningState extends AbstractAppState implements ActionListener, ScreenController, RawInputListener {

    private WJSFClient app;
    public Node localRootNode;
    public CameraNode camNode;
    private Background background;
    public BitmapText textShipPos;
    public BitmapText textNewChunk;
    public ClientShip playerShip;
    public ArrayList<ClientShip> clientShips = new ArrayList<ClientShip>();
    public ClientNetMsgListener msgManager;
    private float cameraHeight = 0f;
    float camXOffset = -20f; // Camera X
    float camZOffset = 20f;  // Camera Y, should at least be 0.1f so that the camera isn't inside the ship
    float camYOffset = 20f;  // Camera height
    boolean universeDebug = false;
    
    public boolean nearStation = false;

    /**
     * for camera working in client
     */
    private boolean up;
    private boolean useAdjustingCamera = false;

    public GameRunningState() {
    }

    public GameRunningState(WJSFClient app) {
        this.app = app;
        this.msgManager = new ClientNetMsgListener(app);
        this.localRootNode = new Node("GameRunningNode");       
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.initLight();
        this.initHUD();
        //this.initKeys();
        this.initCamera();
        this.background = new Background(this.app);
        this.background.initBackground();

        this.app.getRootNode().attachChild(this.localRootNode);

        this.app.gui.goToEmptyScreen();
        this.app.getInputManager().setCursorVisible(false);
        this.app.getInputManager().addRawInputListener(this);
    }

    public void initCamera() {
        camNode = new CameraNode("Camera Node", this.app.getViewPort().getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.localRootNode.attachChild(camNode);

        cameraHeight = camYOffset * (this.app.getViewPort().getCamera().getWidth() / 1600f);
        if (this.playerShip != null) {
            updateCamera();
        }
    }

    public void initKeys() {
        this.app.getInputManager().addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));
        this.app.getInputManager().addMapping("ToggleEditor", new KeyTrigger(KeyInput.KEY_E));
        this.app.getInputManager().addMapping("ExitOverlay", new KeyTrigger(KeyInput.KEY_ESCAPE));

        this.app.getInputManager().addListener(this, "ToggleUniverseDebug", "ToggleEditor", "ExitOverlay");
    }

    private void initHUD() {
        this.textShipPos = new BitmapText(this.app.defaultFont, false);
        this.textShipPos.setSize(this.app.defaultFont.getCharSet().getRenderedSize());      // font size
        this.textShipPos.setColor(ColorRGBA.Green);                             // font color
        this.textShipPos.setText("POS");             // the text
        this.textShipPos.setLocalTranslation(0, this.app.settings.getHeight(), 0); // position

        this.textNewChunk = new BitmapText(this.app.defaultFont, false);
        this.textNewChunk.setSize(this.app.defaultFont.getCharSet().getRenderedSize());      // font size
        this.textNewChunk.setColor(ColorRGBA.Green);                             // font color
        this.textNewChunk.setText("CHUNK UPDATES\n");             // the text
        this.textNewChunk.setLocalTranslation(this.app.settings.getWidth() - 250, this.app.settings.getHeight(), 0); // position
    }

    private void initLight() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(1f));
        this.localRootNode.addLight(ambient);
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        //
        // OFFLINE INPUT
        //

        if (name.equals("ToggleUniverseDebug")) {
            if (!keyPressed) {
                //System.out.println(camNode.getLocalTranslation().y + "/ " + 70 * (this.viewPort.getCamera().getWidth() / 1280f));
// if (camNode.getLocalTranslation().y == 70 * (this.viewPort.getCamera().getWidth() / 1600f)) {
                if (universeDebug) {
                    universeDebug = false;
                    //camNode.setLocalTranslation(new Vector3f(0, 200 * (this.viewPort.getCamera().getWidth() / 1600f), 0.1f));
                    //this.u.toggleUniverseDebug();
                    this.app.getGuiNode().attachChild(this.textShipPos);
                    this.app.getGuiNode().attachChild(this.textNewChunk);
                } else {
                    camNode.setLocalTranslation(new Vector3f(0, 70 * (this.app.getViewPort().getCamera().getWidth() / 1600f), 0.1f));
                    //this.u.toggleUniverseDebug();
                    this.app.getGuiNode().detachChild(this.textShipPos);
                    this.app.getGuiNode().detachChild(this.textNewChunk);
                    universeDebug = true;
                }
            }
        } else if (name.equals("ToggleEditor") && !keyPressed && nearStation) {
            if (!this.app.gui.getCurrentScreenId().equals(GUI.EDITOR_SCREEN)) {
                this.app.gui.goToEditorScreen();
                this.app.getInputManager().setCursorVisible(true);
                this.app.getInputManager().removeRawInputListener(this);
            } else {
                this.app.gui.goToEmptyScreen();
                this.app.getInputManager().setCursorVisible(false);
                this.app.getInputManager().addRawInputListener(this);
            }
        } else if (name.equals("ExitOverlay") && !keyPressed) {
            if (!this.app.gui.getCurrentScreenId().equals(GUI.EXIT_OVERLAY_SCREEN)) {
                this.app.gui.goToExitOverlayScreen();
                this.app.getInputManager().setCursorVisible(true);
            } else {
                this.app.gui.goToEmptyScreen();
                this.app.getInputManager().setCursorVisible(false);
            }
        }
    }

    @Override
    public void update(float tpf) {
        this.msgManager.update(tpf);

        //this.background.updateBackground();
        this.updateCamera();
    }

    @Override
    public void cleanup() {
        super.cleanup();

        this.app.getInputManager().clearMappings();
        this.app.getRootNode().detachChild(this.localRootNode);
    }

    /**
     * Nifty Stuff
     */
    @Override
    public void bind(Nifty arg0, Screen arg1) {
    }

    @Override
    public void onEndScreen() {
    }

    @Override
    public void onStartScreen() {
    }

    public void pressLogOut() {
        this.app.getStateManager().detach(this.app.gameRunState);
        this.app.getStateManager().attach(this.app.mainMenuState);
    }
    /*
     * Camera
     */
    Vector3f previousCamPos;
    Vector3f currentCamPos = new Vector3f();
    float camPosChangeLerpValue = 0.03f;

    public void updateCamera() {
        if (this.playerShip != null) {
            if (useAdjustingCamera) {
                previousCamPos = currentCamPos;
                currentCamPos = new Vector3f();

                float min = 0.1f;
                float max = 100f;
                float speedFactor = this.playerShip.velocity.lengthSquared() * 0.1f;

                speedFactor = Math.max(min, speedFactor);
                speedFactor = Math.min(speedFactor, max);
                float t = inverseLerp(0f, max + min, speedFactor);
                float offsetFactor = 1f - t;

                currentCamPos.x = this.playerShip.shipRoot.getLocalTranslation().x + camXOffset * offsetFactor;
                currentCamPos.z = this.playerShip.shipRoot.getLocalTranslation().z + camZOffset * offsetFactor;

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
                camNode.lookAt(this.playerShip.shipRoot.getLocalTranslation(), Vector3f.UNIT_Y);
            } else {
                currentCamPos.x = this.playerShip.shipRoot.getLocalTranslation().x;
                currentCamPos.z = this.playerShip.shipRoot.getLocalTranslation().z + 0.1f;
                currentCamPos.y = this.cameraHeight * 5f;

                camNode.setLocalTranslation(currentCamPos);
                camNode.lookAt(this.playerShip.shipRoot.getLocalTranslation(), Vector3f.UNIT_Y);
            }
        }
    }

    float lerp(float v0, float v1, float t) {
        return (1 - t) * v0 + t * v1;
    }

    float inverseLerp(float a, float b, float x) {
        return (x - a) / (b - a);
    }
    
    public void beginInput() {
    }

    public void endInput() {
    }

    public void onJoyAxisEvent(JoyAxisEvent evt) {
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
    }

    public void onMouseMotionEvent(MouseMotionEvent evt) {
    }

    public void onMouseButtonEvent(MouseButtonEvent evt) {
    }

    public void onKeyEvent(KeyInputEvent evt) {
        //
        // NETWORKING INPUT
        //
        
        if (!evt.isRepeating()) {
            KeyPressedMsg msg = new KeyPressedMsg(evt.getKeyCode(), evt.isPressed());
            msg.setReliable(true);
            this.app.client.send(msg);
        }
    }

    public void onTouchEvent(TouchEvent evt) {
    }
}
