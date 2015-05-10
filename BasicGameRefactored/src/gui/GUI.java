/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import mygame.Main;

public class GUI {
    
    private Nifty nifty;
    private Main app;
    
    private Screen editorScreen;
    private Screen startScreen;
    
    public GUI(Main app) {
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
        nifty.addXml("Interface/EditorScreen.xml");
        nifty.addXml("Interface/StartScreen.xml");
        
        editorScreen = nifty.getScreen("editor");
        startScreen = nifty.getScreen("start");
        
        goToStartScreen();
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
}
