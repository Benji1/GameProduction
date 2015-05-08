/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.DraggableDragCanceledEvent;
import de.lessvoid.nifty.controls.DraggableDragStartedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Moritz
 */
public class EditorScreenController implements ScreenController
{
    Nifty nifty;
    Screen screen;
    
    public void bind(Nifty nifty, Screen screen) {
        //System.out.println("bind " + this.getClass().getSimpleName());
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        //System.out.println("onStartScreen " + this.getClass().getSimpleName());
    }

    public void onEndScreen() {
        //System.out.println("onEndScreen " + this.getClass().getSimpleName());
    }
    
    public void switchToMap() {
        System.out.println("Go to Map");
    }
    
    public void switchToShop() {
        System.out.println("Go to Shop");
    }
    
    public void switchToShipBuilder() {
        System.out.println("Go to Ship Builder");
    }
    
    public void exitMenu() {
        nifty.gotoScreen("start");
    }
    
    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onClick(String id, NiftyMousePrimaryClickedEvent event) {  
        //System.out.println("element with id [" + id + "] clicked at [" + event.getMouseX() + ", " + event.getMouseY() + "]"); 
    }

    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onDragStarted(final String id, DraggableDragStartedEvent event) {
        // a new draggable element has to be created since the old one was moved away
        String parentId = "panel-parent-"+id.substring(11);
        Element parent = screen.findElementByName(parentId);
        
        // somehow can't acces imagemode property
        //ImageMode im = event.getDraggable().getElement().getElements().get(0).getRenderer(ImageRenderer.class).getImage().getImageMode();
        //System.out.println(im);
        
        Element newDraggable = new DraggableBuilder(id) {{
            visibleToMouse(true);
            childLayout(ElementBuilder.ChildLayoutType.Center);
            panel(new PanelBuilder() {{
                backgroundImage("Interface/Images/Parts.png");
                width("80%");
                height("80%");
                // crap cause can't acces imagemode property of other panel
                imageMode("sprite:100,100,"+(Integer.parseInt(id.substring(11))+2));
            }});
        }}.build(nifty, screen, parent);
        
        newDraggable.setParent(parent);
    }
    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onDragCanceled(String id, DraggableDragCanceledEvent event) {  
        event.getDraggable().getElement().markForRemoval();
    }
    
}
