/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netclient.gui;

import com.jme3.network.serializing.Serializable;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import netclient.gui.dragAndDrop.builder.DraggableBuilder;
import netserver.modules.FacingDirection;

@Serializable
public class OrientedModule {
    public ModuleType moduleType;
    public FacingDirection facingDirection;

    public OrientedModule() {
        this(ModuleType.ARMOR, FacingDirection.FORWARD);
    }
    
    public OrientedModule(ModuleType moduleType) {
        this(moduleType, FacingDirection.FORWARD);
    }
    public OrientedModule(ModuleType moduleType, FacingDirection orientation) {
        this.moduleType = moduleType;
        this.facingDirection = orientation;
    }

    public void rotateRight() {
        facingDirection = facingDirection.next();
    }
    public void rotateLeft() {
        facingDirection = facingDirection.previous();
    }

    public int getSpriteNumber() {
        return moduleType.getValue() * 4 + facingDirection.getSpriteValue();
    }
    
    public void buildGuiElement(int idCounter, Nifty nifty, Screen screen, Element targetParent) {
        String newId = "part-panel-" + moduleType.getValue() + "-" + idCounter;
        
        Element element = new DraggableBuilder(newId) {{
            visibleToMouse(true);
            childLayout(ElementBuilder.ChildLayoutType.Center);
            panel(new PanelBuilder() {{
                backgroundImage("Interface/Images/Parts.png");
                width("100%");
                height("100");
                imageMode("sprite:100,100," + (moduleType.getValue()*4 + facingDirection.getSpriteValue()));
            }});
        }}.build(nifty, screen, targetParent);
        
        element.setParent(targetParent);
    }
}
