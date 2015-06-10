/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.gui;

import netclient.WJSFClient;

import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

public class GUI {
    
    private Nifty nifty;
    private WJSFClient app;
    
    private Screen editorScreen;
    private Screen startScreen;
    private Screen emptyScreen;
    private Screen exitOverlayScreen;
    
    public GUI(WJSFClient app) {
        this.app = app;
        initNifty();
    }
    
    public void initNifty() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        app.getGuiViewPort().addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadStyleFile("Interface/CustomStyles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.loadControlFile("Interface/DragAndDrop.xml");
        nifty.loadControlFile("Interface/CustomControls.xml");
        nifty.fromXml("Interface/StartScreen.xml", "start", this.app.mainMenuState, this.app.gameRunState);
        nifty.addXml("Interface/ExitOverlayScreen.xml");
        nifty.addXml("Interface/EditorScreen.xml");
        nifty.addXml("Interface/EmptyScreen.xml");        
        
        editorScreen = nifty.getScreen("editor");
        emptyScreen = nifty.getScreen("empty");
        startScreen = nifty.getScreen("start");
        exitOverlayScreen = nifty.getScreen("exitOverlay");
    }
    
    public String getCurrentScreenId() {
        return nifty.getCurrentScreen().getScreenId();
    }
    public void goToStartScreen() {
        nifty.gotoScreen(startScreen.getScreenId());
    }
    public void goToEditorScreen() {
        nifty.gotoScreen(editorScreen.getScreenId());
    }   
    public void goToEmptyScreen() {
    	nifty.gotoScreen(emptyScreen.getScreenId());
    }
    public void goToExitOverlayScreen() {
    	nifty.gotoScreen(exitOverlayScreen.getScreenId());
    }
}
