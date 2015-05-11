/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.dragAndDrop.builder.DraggableBuilder;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import gui.dragAndDrop.Draggable;
import gui.dragAndDrop.DraggableControl;
import gui.dragAndDrop.DraggableDragCanceledEvent;
import gui.dragAndDrop.DraggableDragStartedEvent;
import gui.dragAndDrop.Droppable;
import gui.dragAndDrop.DroppableControl;
import gui.dragAndDrop.DroppableDropFilter;
import gui.dragAndDrop.DroppableDroppedEvent;
import java.util.HashMap;

/**
 *
 * @author Moritz
 */
public class EditorScreenController implements ScreenController, DroppableDropFilter {
    
    Nifty nifty;
    Screen screen;
    
    private int partIdCounter = 0;
    private HashMap<Point , ModuleType> shipTiles = new HashMap<Point, ModuleType>();
    private int[][] directions = new int[][] {{1,0},{0,1},{-1,0},{0,-1},{0,0}};
    //private int[][] directions = new int[][] {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1},{0,0}};
    
    public void bind(Nifty nifty, Screen screen) {
        //System.out.println("bind " + this.getClass().getSimpleName());
        this.nifty = nifty;
        this.screen = screen;
    }

    public void onStartScreen() {
        //System.out.println("onStartScreen " + this.getClass().getSimpleName());
        partIdCounter = 0;
        // reload inventory
        clearPartsPanel();
        clearSlotsPanel();
        
        setupPartsPanel();
        setupSlotsPanel();
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
    
    public void rotatePart(String id) {
        System.out.println(id);
    }
    
    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onClick(String id, NiftyMousePrimaryClickedEvent event) {  
        //System.out.println("element with id [" + id + "] clicked at [" + event.getMouseX() + ", " + event.getMouseY() + "]"); 
    }

    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onDragStarted(final String id, DraggableDragStartedEvent event) {
        if (event.getDraggable() instanceof DraggableControl) {
            DraggableControl dragControl = (DraggableControl) event.getDraggable();
            if (dragControl.getOriginalParent().getId().startsWith("panel-parent")) {
                // a new draggable element has to be created since the old one was moved away
                final String parentNr = id.substring(11, id.lastIndexOf("-"));
                String parentId = "panel-parent-"+parentNr;
                Element parent = screen.findElementByName(parentId);

                // somehow can't acces imagemode property
                //ImageMode im = event.getDraggable().getElement().getElements().get(0).getRenderer(ImageRenderer.class).getImage().getImageMode();
                //System.out.println(im);

                String newDragId = "part-panel-"+parentNr+"-"+partIdCounter++;

                Element newDraggable = new DraggableBuilder(newDragId) {{
                    visibleToMouse(true);
                    childLayout(ElementBuilder.ChildLayoutType.Center);
                    panel(new PanelBuilder() {{
                        backgroundImage("Interface/Images/Parts.png");
                        width("80%");
                        height("80%");
                        // crap cause can't acces imagemode property of other panel
                        imageMode("sprite:100,100,"+(Integer.parseInt(parentNr)+2));
                    }});
                }}.build(nifty, screen, parent);

                newDraggable.setParent(parent);

                // TODO: counter-- of that module type in gui
            } else {
                String id2 = dragControl.getOriginalParent().getId();
                System.out.println(id2);
                int x = Integer.parseInt(id2.substring(id2.indexOf("X") + 1, id2.indexOf("Y")));
                int y = Integer.parseInt(id2.substring(id2.indexOf("Y") + 1, id2.indexOf("#")));
                shipTiles.remove(new Point(x, y));
                
                for (int i = 0; i < directions.length; ++i) {
                    int gridX = x + directions[i][0];
                    int gridY = y + directions[i][1];
                    Element element = screen.findElementByName("slotX" + gridX + "Y" + gridY);
                    if (element != null) {
                        if (!shipTiles.containsKey(new Point(gridX, gridY))) {
                            boolean hasNeighbor = false;
                            for (int j = 0; j < directions.length; ++j) {
                                int neighborX = gridX + directions[j][0];
                                int neighborY = gridY + directions[j][1];
                                Element neighbor = screen.findElementByName("slotX" + neighborX + "Y" + neighborY);
                                if (neighbor != null && shipTiles.containsKey(new Point(neighborX, neighborY))) {
                                    hasNeighbor = true;
                                    break;
                                }
                            }
                            if (i == directions.length - 1 && shipTiles.isEmpty())
                                break;
                            if (!hasNeighbor) {
                                element.markForRemoval();
                            }
                        }
                    }
                }
            }
        }
    }
    
    @NiftyEventSubscriber(pattern="part-panel-.*") 
    public void onDragCanceled(String id, DraggableDragCanceledEvent event) {  
        event.getDraggable().getElement().markForRemoval();
        // TODO: ++ to counter of that module type in gui
    }
    
    @NiftyEventSubscriber(pattern="slot-.*")
    public void onDroppableDropped(String id, DroppableDroppedEvent event) {
         //System.out.println("dropped: " + event.getTarget().getElement().getConstraintX() + "/" + event.getTarget().getElement().getConstraintY());        
    }
    
    private void clearPartsPanel() {
        Element partsPanel = screen.findElementByName("parts-container");
        for (Element part : partsPanel.getElements()) {
            part.markForRemoval();
        }
    }
    
    private void clearSlotsPanel() {
        Element slotPanel = screen.findElementByName("slots-container");
        for (Element slot : slotPanel.getElements()) {
            slot.markForRemoval();
        }
    }
    
    private void setupPartsPanel() {
        Element partsPanel = screen.findElementByName("parts-container");
        
        // should be able to get this info from the inventory later on
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
        ControlBuilder emptyPanel = new ControlBuilder("empty-panel") {{
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
                
                partPanel.parameter("parentId", "panel-parent-"+i);
                partPanel.parameter("draggableId", "part-panel-"+i+"-"+partIdCounter++);
                partPanel.parameter("label", numOfItems);
                partPanel.parameter("x", x+"px");
                partPanel.parameter("y", y+"px");
                partPanel.parameter("sprite", "sprite:100,100,"+tileImgId);
                partPanel.build(nifty, screen, partsPanel);
            } else {
                emptyPanel.parameter("x", x+"px");
                emptyPanel.parameter("y", y+"px");
                emptyPanel.build(nifty, screen, partsPanel);
            }
        }
    }
    
    private void setupSlotsPanel() {
        buildEmptySlot(0, 0);
    }
    
    private void buildEmptySlot(final int x, final int y) {
        final int gridItemSize = 100;
        Element slotsPanel = screen.findElementByName("slots-container");
        ControlBuilder slotPanel = new ControlBuilder("empty-slot") {{
            parameter("droppableId", "slotX"+x+"Y"+y);
            parameter("width", gridItemSize+"px");
            parameter("height", gridItemSize+"px");
            parameter("x", x*gridItemSize+"px");
            parameter("y", y*gridItemSize+"px");
        }};
        Element element = slotPanel.build(nifty, screen, slotsPanel);
        DroppableControl dropable = element.getControl(DroppableControl.class);
        dropable.addFilter(this);
    }

    public boolean accept(Droppable dropSource, Draggable draggedItem, Droppable dropTarget) {
        // custom drag&drop handling since nifty seems to have some bugs with setting the parents of objects
        final Element targetParent = dropTarget.getElement().getElements().get(0);
        final Element draggedElement = draggedItem.getElement(); 

        final String parentNr = draggedElement.getId().substring(11, draggedElement.getId().lastIndexOf("-"));
        final String newId = draggedElement.getId().substring(0, draggedElement.getId().lastIndexOf("-")) + "-" + partIdCounter++;
        
        // copy dragged element and move it to the target parent
        Element dragCopy = new DraggableBuilder(newId) {{
            visibleToMouse(true);
            childLayout(ElementBuilder.ChildLayoutType.Center);
            panel(new PanelBuilder() {{
                backgroundImage("Interface/Images/Parts.png");
                width("80%");
                height("80%");
                imageMode("sprite:100,100,"+(Integer.parseInt(parentNr)+2));
            }});
        }}.build(nifty, screen, targetParent);
        
        dragCopy.setParent(targetParent);
        
        String id = targetParent.getId();
        int x = Integer.parseInt(id.substring(id.indexOf("X") + 1, id.indexOf("Y")));
        int y = Integer.parseInt(id.substring(id.indexOf("Y") + 1, id.indexOf("#")));
        shipTiles.put(new Point(x, y), ModuleType.getType(Integer.parseInt(parentNr)+2));
        buildNeighborSlots(x, y);
        
        // if there was already an element, move it to the source parent
        if (targetParent.getElements().size() > 2) {    // getElements always contains one element that is null, for some strange reason; + the element that was just moved

            if (draggedItem instanceof DraggableControl) {
                DraggableControl dragControl = (DraggableControl) draggedItem;
                if (dragControl.getOriginalParent().getId().startsWith("slot")) {
                    // switch elements in gui
                    Element sourceParent = dragControl.getOriginalParent();
                    Element elementToMove = targetParent.getElements().get(1);

                    elementToMove.markForMove(sourceParent);
                    String id2 = sourceParent.getId();
                    x = Integer.parseInt(id2.substring(id2.indexOf("X") + 1, id2.indexOf("Y")));
                    y = Integer.parseInt(id2.substring(id2.indexOf("Y") + 1, id2.indexOf("#")));
                    final String moduleId = elementToMove.getId().substring(11, elementToMove.getId().lastIndexOf("-"));
                    shipTiles.put(new Point(x, y), ModuleType.getType(Integer.parseInt(moduleId)+2));
                    buildNeighborSlots(x, y);

                } else {
                    // delete element at target in gui
                    targetParent.getElements().get(1).markForRemoval();
                    // TODO: inventory++
                }
            }
        }
        
        return false;   // prevent default drag&drop
    }

    private void buildNeighborSlots(int x, int y) {
        for (int i = 0; i < directions.length - 1; ++i) {
            int gridX = x + directions[i][0];
            int gridY = y + directions[i][1];
            Element element = screen.findElementByName("slotX" + gridX + "Y" + gridY);
            if (element == null) {
                buildEmptySlot(x + directions[i][0], y + directions[i][1]);
            }
        }
    }
}
