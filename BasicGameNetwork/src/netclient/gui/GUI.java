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
    
    public static final String EDITOR_SCREEN = "editor";
    public static final String START_SCREEN = "start";
    public static final String EMPTY_SCREEN = "empty";
    public static final String EXIT_OVERLAY_SCREEN = "exitOverlay";
    public static final String END_GAME_SCREEN = "endGame";
    
    private Nifty nifty;
    private WJSFClient app;
    
    private Screen editorScreen;
    private Screen startScreen;
    private Screen emptyScreen;
    private Screen exitOverlayScreen;
    private Screen endGameScreen;
    
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
        nifty.fromXml("Interface/StartScreen.xml", START_SCREEN, this.app.mainMenuState, this.app.gameRunState);
        nifty.addXml("Interface/ExitOverlayScreen.xml");
        nifty.addXml("Interface/EditorScreen.xml");
        nifty.addXml("Interface/EmptyScreen.xml"); 
        nifty.registerScreenController(this.app.endGameMenuState);
        nifty.addXml("Interface/EndGameScreen.xml");        
        
        editorScreen = nifty.getScreen(EDITOR_SCREEN);
        emptyScreen = nifty.getScreen(EMPTY_SCREEN);
        startScreen = nifty.getScreen(START_SCREEN);
        exitOverlayScreen = nifty.getScreen(EXIT_OVERLAY_SCREEN);
        endGameScreen = nifty.getScreen(END_GAME_SCREEN);
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
    public void goToEndGameScreen() {
        nifty.gotoScreen(endGameScreen.getScreenId());
    }
}
