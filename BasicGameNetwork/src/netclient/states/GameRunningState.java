package netclient.states;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import netclient.ClientNetMsgListener;
import netclient.ClientShip;
import netclient.WJSFClient;
import netclient.gui.GUI;
import netclient.universe.Background;
import netclient.universe.UniverseEntityManager;
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
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.util.HashMap;

import netclient.gui.ModuleType;
import netclient.otherGraphics.GLaserProjectile;
import netclient.otherGraphics.GraphicObject;
import netserver.weapons.Projectile;

public class GameRunningState extends AbstractAppState implements ActionListener, ScreenController, RawInputListener {

    public WJSFClient app;
    public Node localRootNode;
    public CameraNode camNode;
    private Background background;

    public ClientShip playerShip;
    public ArrayList<ClientShip> clientShips = new ArrayList<ClientShip>();
    public ClientNetMsgListener msgManager;
    public UniverseEntityManager uemanager;

    private int cameraType = 2; // 0, 1 or 2
    private float cameraMinHeight = 0f;
    private float cameraMaxHeight = 0f;
    float camXOffset = -20f; // Camera X
    float camZOffset = 20f;  // Camera Y, should at least be 0.1f so that the camera isn't inside the ship
    float camYOffset = 20f;  // Camera height
    
    boolean universeDebug = false;
    public Node chunkAreaNode = new Node("ChunkAreas");
    public int chunkX = 0, chunkZ = 0;
    
    public boolean nearStation = false;
    
    // projectiles, floating items, ... (everything that needs its pos updated)
    public HashMap<Integer , GraphicObject> graphicObjects = new HashMap<Integer, GraphicObject>();

    // Debug stuff
    public BitmapText textMessages;
    public BitmapText textPosition;
    
    public GameRunningState() {
    }

    public GameRunningState(WJSFClient app) {
        this.app = app;
        this.msgManager = new ClientNetMsgListener(app);
        this.localRootNode = new Node("GameRunningNode");
        
        // Debug text
        this.textMessages = new BitmapText(this.app.defaultFont, false);
        this.textMessages.setSize(this.app.defaultFont.getCharSet().getRenderedSize());      // font size
        this.textMessages.setColor(ColorRGBA.Green);                             // font color
        this.textMessages.setText("MESSAGE BOARD");             // the text
        this.textMessages.setLocalTranslation(0, this.app.settings.getHeight(), 0); // position

        
        this.textPosition = new BitmapText(this.app.defaultFont, false);
        this.textPosition.setSize(this.app.defaultFont.getCharSet().getRenderedSize());      // font size
        this.textPosition.setColor(ColorRGBA.Green);                             // font color
        this.textPosition.setText("CHUNK UPDATES\n");             // the text
        this.textPosition.setLocalTranslation(this.app.settings.getWidth() - 350, this.app.settings.getHeight(), 0); // position
        
        // ChunkAreas
        this.localRootNode.attachChild(this.chunkAreaNode);
        for(int x = -1; x <= 1; x++) {
        	for(int z = -1; z <= 1; z++) {
		        Box box = new Box(Universe.CHUNK_SIZE / 2,0.01f,Universe.CHUNK_SIZE / 2);
		        Spatial wall = new Geometry("Box", box );
		        Material mat_brick = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		        mat_brick.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		        mat_brick.setTexture("ColorMap", this.app.getAssetManager().loadTexture("textures/chunkarea.png"));
		        wall.setMaterial(mat_brick);
		        wall.setLocalTranslation(Universe.CHUNK_SIZE * x, 0, Universe.CHUNK_SIZE * z);
		        wall.setQueueBucket(Bucket.Transparent);
		        this.chunkAreaNode.attachChild(wall);
        	}
        }
        
        this.uemanager = new UniverseEntityManager(this);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.initLight();
        //this.initKeys();
        this.initCamera();
        this.background = new Background(this.app);
        this.background.initBackground();

        this.app.getRootNode().attachChild(this.localRootNode);

        this.app.gui.goToEmptyScreen();
        this.app.getInputManager().setCursorVisible(false);
        this.app.getInputManager().addRawInputListener(this);
        
        this.app.getGuiNode().attachChild(this.textMessages);
        this.app.getGuiNode().attachChild(this.textPosition);
    }

    public void initCamera() {        
        camNode = new CameraNode("Camera Node", this.app.getViewPort().getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        this.localRootNode.attachChild(camNode);

        cameraMinHeight = camYOffset * (app.getViewPort().getCamera().getWidth() / 1600f);
        cameraMaxHeight = cameraMinHeight * 5f;

        Vector3f tmpPos = camNode.getLocalTranslation();
        tmpPos.y = cameraMinHeight - 2f;
        currentCamPos = tmpPos;
        
        if (this.playerShip != null) {
            updateCamPos(0f);
        }
    }

    public void initKeys() {
        this.app.getInputManager().addMapping("ToggleUniverseDebug", new KeyTrigger(KeyInput.KEY_U));
        this.app.getInputManager().addMapping("ToggleEditor", new KeyTrigger(KeyInput.KEY_E));
        this.app.getInputManager().addMapping("ExitOverlay", new KeyTrigger(KeyInput.KEY_ESCAPE));

        this.app.getInputManager().addListener(this, "ToggleUniverseDebug", "ToggleEditor", "ExitOverlay");
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
                } else {
                    camNode.setLocalTranslation(new Vector3f(0, 70 * (this.app.getViewPort().getCamera().getWidth() / 1600f), 0.1f));
                    //this.u.toggleUniverseDebug();
                    universeDebug = true;
                }
            }
        } else if (name.equals("ExitOverlay") && !keyPressed) {
            if (this.app.gui.getCurrentScreenId().equals(GUI.EMPTY_SCREEN)) {
                this.app.gui.goToExitOverlayScreen();
                this.app.getInputManager().setCursorVisible(true);
            } else {
                this.app.gui.goToEmptyScreen();
                this.app.getInputManager().setCursorVisible(false);
            }
        }
    }
    
    public void toggleEditor(ModuleType[] newItemsInBase) {
        if (!this.app.gui.getCurrentScreenId().equals(GUI.EDITOR_SCREEN)) {
            if (nearStation) {
                playerShip.setItemsInBase(newItemsInBase);
                this.app.gui.goToEditorScreen();
                this.app.getInputManager().setCursorVisible(true);
                //this.app.getInputManager().removeRawInputListener(this);
            }
        } else {
            this.app.gui.goToEmptyScreen();
            this.app.getInputManager().setCursorVisible(false);
            //this.app.getInputManager().addRawInputListener(this);
        }
    }
    
    @Override
    public void update(float tpf) {
    	if(this.playerShip != null) {
	    	this.chunkX = (int)((this.playerShip.shipRoot.getLocalTranslation().x + ((this.playerShip.shipRoot.getLocalTranslation().x > 0) ? 1 : -1) * Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE);
	    	this.chunkZ = (int)((this.playerShip.shipRoot.getLocalTranslation().z + ((this.playerShip.shipRoot.getLocalTranslation().z > 0) ? 1 : -1) * Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE);
	    	
	    	this.chunkAreaNode.setLocalTranslation(this.chunkX * Universe.CHUNK_SIZE, 0, this.chunkZ * Universe.CHUNK_SIZE);
    	}
    	
        this.msgManager.update(tpf);
        
        // f.ex. for adjusting thruster particles to current velocity
        if (this.playerShip != null) {
            this.playerShip.update(tpf);
        }
        for (ClientShip clientShip : clientShips) {
            clientShip.update(tpf);
        }
        

        if (this.playerShip != null && !universeDebug) {
            updateCamPos(tpf);
        }

        
        // update debug    
        if(this.playerShip != null) {
	        String s = "PLAYERS ONLINE: " + (this.clientShips.size() + 1);
	        
	        s += "\nYOU: Sector " + this.chunkX + "/" + this.chunkZ + " | " + this.playerShip.shipRoot.getLocalTranslation().toString();
	        
	        for(int i = 0; i < this.clientShips.size(); i++)
	        	s += "\n" + this.clientShips.get(i).name + ": Sector " + (int)((this.clientShips.get(i).shipRoot.getLocalTranslation().x - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE) + "/" + (int)((this.clientShips.get(i).shipRoot.getLocalTranslation().z - Universe.CHUNK_SIZE / 2) / Universe.CHUNK_SIZE);
	        
	        this.textPosition.setText(s);
        }
        
        this.background.updateBackground();
        this.uemanager.update(tpf);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        
        this.app.getInputManager().clearMappings();
        this.app.getInputManager().removeRawInputListener(this);
        this.app.getRootNode().detachChild(this.localRootNode);
        
        this.app.getGuiNode().detachChild(this.textMessages);
        this.app.getGuiNode().detachChild(this.textPosition);
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
    
    final float cooldown = 2f;
    float cameraMoveBackCooldown = 0f;
    float camYspeed = 10f;
    float crap = 0f;
    
    public void updateCamPos(float delta) {
        if (cameraType == 0) {
            currentCamPos.x = playerShip.shipRoot.getLocalTranslation().x;
            currentCamPos.z = playerShip.shipRoot.getLocalTranslation().z + 0.1f;
            currentCamPos.y = this.cameraMinHeight * 5f;
            
            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(playerShip.shipRoot.getLocalTranslation(), Vector3f.UNIT_Y);
        } else if (cameraType == 1){
            previousCamPos = currentCamPos;
            currentCamPos = new Vector3f();
            
            float min = 0.1f;
            float max = 100f;
            float speedFactor = playerShip.getVelocity().lengthSquared() * 0.1f;
            
            speedFactor = Math.max(min, speedFactor);
            speedFactor = Math.min(speedFactor, max);
            float t = inverseLerp(0f, max + min, speedFactor);
            float offsetFactor = 1f - t;
            
            currentCamPos.x = playerShip.shipRoot.getLocalTranslation().x + camXOffset * offsetFactor;
            currentCamPos.z = playerShip.shipRoot.getLocalTranslation().z + camZOffset * offsetFactor;
            
            float newY =
                    lerp(
                    previousCamPos.y,
                    this.cameraMinHeight + speedFactor,
                    camPosChangeLerpValue);
            
            if (!playerShip.hasActivatedThruster() || newY > previousCamPos.y) {
                currentCamPos.y = newY;
            } else {
                currentCamPos.y = previousCamPos.y;
            }
            
            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(playerShip.shipRoot.getLocalTranslation(), Vector3f.UNIT_Y);
        } else if (cameraType == 2) {
            previousCamPos = currentCamPos;
            currentCamPos = new Vector3f();
            
            float newY =
                    lerp(
                    previousCamPos.y,
                    this.cameraMinHeight + crap,
                    camPosChangeLerpValue);
            
            
            if (playerShip.hasActivatedThruster()) 
                cameraMoveBackCooldown = cooldown;
            
            
            if(cameraMoveBackCooldown < 0f || 
                    (playerShip.hasActivatedThruster() && 
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
            
            currentCamPos.x = playerShip.shipRoot.getLocalTranslation().x + camXOffset;
            currentCamPos.z = playerShip.shipRoot.getLocalTranslation().z + camZOffset;
            
            cameraMoveBackCooldown -= delta;
            camNode.setLocalTranslation(currentCamPos);
            camNode.lookAt(playerShip.shipRoot.getLocalTranslation(), Vector3f.UNIT_Y);
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
