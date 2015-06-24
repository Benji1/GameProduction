/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.network.Network;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;
import netclient.WJSFClient;
import netserver.WJSFServer;
import netutil.NetMessages;

public class EndGameMenuState extends AbstractAppState implements ScreenController, ActionListener {
    
    private WJSFClient app;
    private Node rootNode;
    private ViewPort viewPort;
    private Node guiNode;

    private Nifty nifty;
    
    public EndGameMenuState(WJSFClient app) {
        this.app = app;
        this.rootNode = app.getRootNode();
        this.viewPort = app.getViewPort();
        this.guiNode = app.getGuiNode();
    }
    
    @Override
    public void update(float tpf) {
    }
	
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        reset();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    public void reset() {
        this.app.getInputManager().setCursorVisible(true);
        this.app.gui.goToEndGameScreen();
    }
    
    @Override
    public void onAction(String arg0, boolean arg1, float arg2) {
    }

    @Override
    public void bind(Nifty nifty, Screen arg1) {
        this.nifty = nifty;
    }
    
    @Override
    public void onEndScreen() {
    }
    
    @Override
    public void onStartScreen() {
        Element niftyElement = this.nifty.getCurrentScreen().findElementByName("loginLabel");
        niftyElement.getRenderer(TextRenderer.class).setText("");
    }
    
    public void pressLogIn() {
        try {
            this.app.client = Network.connectToServer(NetMessages.IP, NetMessages.PORT);
            this.app.client.addClientStateListener(this.app);
            this.app.client.start();
            this.app.client.addMessageListener(this.app.gameRunState.msgManager);
        } catch (Exception e) {
            // log error
            Logger.getLogger(WJSFServer.class.getName()).log(Level.SEVERE, null, e);

            Element niftyElement = this.nifty.getCurrentScreen().findElementByName("loginLabel");
            niftyElement.getRenderer(TextRenderer.class).setText(e.getMessage().toString());
        }
    }
}
