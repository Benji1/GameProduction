/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
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
        setupPartsPanel();
        nifty.gotoScreen(editorScreen.getScreenId());
    }
    
    private void setupPartsPanel() {
        Element partsPanel = editorScreen.findElementByName("parts-panel");
        
        // should be able to get this info from the inventory later on (also sprite img id)
        int numOfDifferentItems = 6;
        int[] numOfEachItem = new int[] {1, 8, 3, 20, 14, 8};        
        
        int numOfCols = 4;
        int numOfBgPanels = (int) (Math.ceil(numOfDifferentItems/(numOfCols*1f))) * numOfCols;
        final int gridItemSize = 100;  
        int gridOffset = 10;
        
        ControlBuilder partPanel = new ControlBuilder("part-panel") {{
            parameter("width", gridItemSize+"px");
            parameter("height", gridItemSize+"px");
        }};
        ControlBuilder emptyPanel = new ControlBuilder("empty-slot") {{
        }};
        
        for (int i=0; i<numOfBgPanels; i++) {
            int x = gridOffset * (i%numOfCols+1) + gridItemSize * (i%numOfCols);
            int y = gridOffset * (i/numOfCols+1) + gridItemSize * (i/numOfCols);
            
            if (i < numOfDifferentItems) {
                int tileImgId = (i%numOfDifferentItems)+2;
                String numOfItems;
                
                if (numOfEachItem[i] < 10)
                    numOfItems = "x0"+numOfEachItem[i];
                else 
                    numOfItems = "x"+numOfEachItem[i];
                
                partPanel.parameter("label", numOfItems);
                partPanel.parameter("x", x+"px");
                partPanel.parameter("y", y+"px");
                partPanel.parameter("sprite", "sprite:100,100,"+tileImgId);
                partPanel.build(nifty, editorScreen, partsPanel);
            } else {
                emptyPanel.parameter("x", x+"px");
                emptyPanel.parameter("y", y+"px");
                emptyPanel.build(nifty, editorScreen, partsPanel);
            }
        }
    }
    
}
