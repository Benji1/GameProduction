/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.inventory.InventoryCategory;
import Modules.Armor;
import Modules.BasicModule;
import Modules.Cockpit;
import Modules.EnergyGenerator;
import Modules.FacingDirection;
import Modules.LaserGun;
import Modules.Shield;
import Modules.Storage;
import Modules.Thruster;
import gui.dragAndDrop.builder.DraggableBuilder;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseWheelEvent;
import de.lessvoid.nifty.elements.render.ElementRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import gui.dragAndDrop.Draggable;
import gui.dragAndDrop.DraggableControl;
import gui.dragAndDrop.DraggableDragCanceledEvent;
import gui.dragAndDrop.DraggableDragStartedEvent;
import gui.dragAndDrop.Droppable;
import gui.dragAndDrop.DroppableControl;
import gui.dragAndDrop.DroppableDropFilter;
import gui.dragAndDrop.DroppableDroppedEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mygame.Inventory;
import services.ServiceManager;
import services.editor.EditorManager;

/**
 *
 * @author Moritz
 */
public class EditorScreenController implements ScreenController, DroppableDropFilter {
    
    //****** test keyboard mappings
    ArrayList<String> fwd;
    ArrayList<String> fwdAndLeft;
    ArrayList<String> fwdAndRight;
    ArrayList<String> fwdAndLeftAndRight;
    
    ArrayList<String> right;
    ArrayList<String> left;
    
    ArrayList<String> bckwd;
    ArrayList<String> bckwdAndLeft;
    ArrayList<String> bckwdAndRight;
    ArrayList<String> bckwdAndLeftAndRight;
    
    ArrayList<String> shield;
    ArrayList<String> weapon;
    
    ArrayList<String> testFire;
    //*****************************
    
    private class OrientedModule {
        public ModuleType moduleType;
        public FacingDirection facingDirection;
        
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
    }
    
    Nifty nifty;
    Screen screen;
    
    private int partIdCounter = 0;
    private HashMap<Point , OrientedModule> shipTiles = new HashMap<Point, OrientedModule>();
    // TODO: change ship id
    Inventory inventory = ServiceManager.getEditorManager().getShip(0).getInventory();
    private int[][] directions = new int[][] {{1,0},{0,1},{-1,0},{0,-1},{0,0}};
    //private int[][] directions = new int[][] {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1},{0,0}};
    
    private static final float DEFAULT_SLOT_SIZE = 100f;
    private static final float MAX_SCALE = 3f;
    private static final float MIN_SCALE = 0.5f;
    private float scale = 1f;
    
    private boolean realDragCancel = true;
    private boolean deleteOnDragCancel = true;
    
    public void bind(Nifty nifty, Screen screen) {
        //System.out.println("bind " + this.getClass().getSimpleName());
        this.nifty = nifty;
        this.screen = screen;
        
        setupTestKeyboardMappings();
    }

    public void onStartScreen() {
        //System.out.println("onStartScreen " + this.getClass().getSimpleName());
        partIdCounter = 0;
        
        // reload inventory       
        setupPartsPanel(InventoryCategory.COCKPIT_CAT);
        setupSlotsPanel();
        loadShipFromGame();
    }

    public void onEndScreen() {
        //System.out.println("onEndScreen " + this.getClass().getSimpleName());
        buildShip();
        
        clearPartsPanel();
        clearSlotsPanel();
        shipTiles.clear();
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
    
    private void setupTestKeyboardMappings() {
        fwd = new ArrayList<String>();
        fwd.add("Up");
        
        fwdAndLeft = new ArrayList<String>();
        fwdAndLeft.add("Up");
        fwdAndLeft.add("Left");
        
        fwdAndRight = new ArrayList<String>();
        fwdAndRight.add("Up");
        fwdAndRight.add("Right");
        
        fwdAndLeftAndRight = new ArrayList<String>();
        fwdAndLeftAndRight.add("Up");
        fwdAndLeftAndRight.add("Left");
        fwdAndLeftAndRight.add("Right");
        
        right = new ArrayList<String>();
        right.add("Right");
        
        left = new ArrayList<String>();
        left.add("Left");
        
        bckwd = new ArrayList<String>();
        bckwd.add("Down");
        
        bckwdAndLeft = new ArrayList<String>();
        bckwdAndLeft.add("Down");
        bckwdAndLeft.add("Left");
        
        bckwdAndRight = new ArrayList<String>();
        bckwdAndRight.add("Down");
        bckwdAndRight.add("Right");
        
        bckwdAndLeftAndRight = new ArrayList<String>();
        bckwdAndLeftAndRight.add("Down");
        bckwdAndLeftAndRight.add("Left");
        bckwdAndLeftAndRight.add("Right");
        
        shield = new ArrayList<String>();
        shield.add("Shield");
        
        weapon = new ArrayList<String>();
        weapon.add("Weapon");
        
        testFire = new ArrayList<String>();
        testFire.add("TestFire");
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
            // move from inventory section to editor
            if (dragControl.getOriginalParent().getId().startsWith("panel-parent")) {
                // check if modules count > 0
                final String parentNr = id.substring(11, id.lastIndexOf("-"));
                if (inventory.getModuleCountInBase(ModuleType.getType(Integer.parseInt(parentNr))) > 0) {
                    // a new draggable element has to be created since the old one was moved away                    
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
                            imageMode("sprite:100,100,"+(Integer.parseInt(parentNr)*4));
                        }});
                    }}.build(nifty, screen, parent);

                    newDraggable.setParent(parent);

                    // counter-- of that module type in inventory
                    inventory.removeItemOfType(ModuleType.getType(Integer.parseInt(parentNr)));
                    // counter-- of that module type in gui
                    TextRenderer textRenderer = screen.findElementByName(ModuleType.getType(Integer.parseInt(parentNr)).toString() + "-counter").getRenderer(TextRenderer.class);
                    int count = inventory.getModuleCountInBase(ModuleType.getType(Integer.parseInt(parentNr)));
                    textRenderer.setText("x" + (count < 10 ? "0" : "") + count);         
                    
                    SizeValue sizeValue = new SizeValue(Integer.toString((int) (DEFAULT_SLOT_SIZE * scale)));
                    Element dragged = event.getDraggable().getElement();
                    dragged.setConstraintWidth(sizeValue);
                    dragged.setConstraintHeight(sizeValue);
                    dragged.getElements().get(0).setConstraintWidth(new SizeValue("100%"));
                    dragged.getElements().get(0).setConstraintHeight(new SizeValue("100%"));
                } else {
                    // no more items in inventory --> cancel drag
                    realDragCancel = false;
                    deleteOnDragCancel = false;
                    if (event.getDraggable() instanceof DraggableControl) {
                        dragControl.dragStop();
                    } 
                }
            // move in editor field
            } else {
                String id2 = dragControl.getOriginalParent().getId();
                //System.out.println(id2);
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
        if (deleteOnDragCancel) {
            event.getDraggable().getElement().markForRemoval();
        }
        
        if (realDragCancel) {        
            // counter++ of that module type in inventory
            int parentId = Integer.parseInt(id.substring(11, id.lastIndexOf("-")));
            inventory.addItemOfType(ModuleType.getType(parentId));        
            // counter++ of that module type in gui        
            TextRenderer textRenderer = screen.findElementByName(ModuleType.getType(parentId).toString() + "-counter").getRenderer(TextRenderer.class);
            int count = inventory.getModuleCountInBase(ModuleType.getType(parentId));
            textRenderer.setText("x" + (count < 10 ? "0" : "") + count);
        }
        
        realDragCancel = true;
        deleteOnDragCancel = true;
    }
    
    @NiftyEventSubscriber(pattern="slot-.*")
    public void onDroppableDropped(String id, DroppableDroppedEvent event) {
         //System.out.println("dropped: " + event.getTarget().getElement().getConstraintX() + "/" + event.getTarget().getElement().getConstraintY());        
    }
    
    @NiftyEventSubscriber(pattern="part-panel-.*")
    public void onRightMouseButtonClicked(String id, NiftyMouseSecondaryClickedEvent event) {
        String parentId = event.getElement().getParent().getId();

        if (parentId.startsWith("slot")) {
            int x = Integer.parseInt(parentId.substring(parentId.indexOf("X") + 1, parentId.indexOf("Y")));
            int y = Integer.parseInt(parentId.substring(parentId.indexOf("Y") + 1, parentId.indexOf("#")));
            //System.out.println(x + " " + y);
            OrientedModule module = shipTiles.get(new Point(x, y));
            if (module != null) {
                module.rotateRight();

                final int spriteNumber = module.getSpriteNumber();
                event.getElement().markForRemoval();

                Element rotated = new DraggableBuilder(id) {{
                    visibleToMouse(true);
                    childLayout(ElementBuilder.ChildLayoutType.Center);
                    panel(new PanelBuilder() {{
                        backgroundImage("Interface/Images/Parts.png");
                        width("100%");
                        height("100%");
                        imageMode("sprite:100,100," + spriteNumber);
                    }});
                }}.build(nifty, screen, event.getElement().getParent());
            }
        }
    }
    
    @NiftyEventSubscriber(id="clip-container")
    public void onMouseWheelChanged(String id, NiftyMouseWheelEvent event) {
        if (event.getMouseWheel() > 0) {
            // zoomed up
            scale += 0.1f;
        } else if (event.getMouseWheel() < 0) {
            // zoomed down
            scale -= 0.1f;
        }
        scale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
        List<Element> elements = event.getElement().getElements().get(0).getElements();
        int size = (int) (scale * DEFAULT_SLOT_SIZE);
        SizeValue sizeValue = new SizeValue(Integer.toString(size));
        for (Element e : elements) {
            e.setConstraintWidth(sizeValue);
            e.setConstraintHeight(sizeValue);
            int x = Integer.parseInt(e.getId().substring(e.getId().indexOf("X") + 1, e.getId().indexOf("Y")));
            int y = Integer.parseInt(e.getId().substring(e.getId().indexOf("Y") + 1));
            e.setConstraintX(new SizeValue(Integer.toString(x * size)));
            e.setConstraintY(new SizeValue(Integer.toString(y * size)));
        }
        event.getElement().getElements().get(0).setConstraintWidth(sizeValue);
        event.getElement().getElements().get(0).setConstraintHeight(sizeValue);
        event.getElement().layoutElements();
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
    
    private void setupPartsPanel(InventoryCategory cat) {
        Element partsPanel = screen.findElementByName("parts-container");
        
        int numOfDifferentItems = cat.getTypes().length;
        int[] numOfEachItem = inventory.getNumOfItemsPerCat(cat);
        int[] tileImgIds = cat.getTileImgIds();
        int[] typeIds = cat.getTypeIds();
         
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
                //int tileImgId = (i%numOfDifferentItems)*4;
                String numOfItems;
                
                if (numOfEachItem[i] < 10)
                    numOfItems = "x0"+numOfEachItem[i];
                else 
                    numOfItems = "x"+numOfEachItem[i];
                
                partPanel.parameter("parentId", "panel-parent-"+typeIds[i]);
                partPanel.parameter("draggableId", "part-panel-"+typeIds[i]+"-"+partIdCounter++);
                partPanel.parameter("label", numOfItems);
                partPanel.parameter("x", x+"px");
                partPanel.parameter("y", y+"px");
                partPanel.parameter("sprite", "sprite:100,100,"+tileImgIds[i]);
                partPanel.parameter("counterId", ModuleType.getType(typeIds[i]).toString() + "-counter");
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
    
    private void loadShipFromGame() {
        // TODO: change ship id
        BasicModule[][] modules = ServiceManager.getEditorManager().getShip(0).getModules();
        for (int i=0; i<modules.length; i++) {
            for (int j=0; j<modules[0].length; j++) {
                if (modules[i][j] != null) {
                    int slotX = i - (modules.length / 2);
                    int slotY = j - (modules[0].length / 2);

                    // build slots
                    Element parent = buildEmptySlot(slotX, slotY).getElements().get(0);
                    buildNeighborSlots(slotX, slotY);
                    
                    // build module element
                    modules[i][j].buildGuiElement(partIdCounter++, nifty, screen, parent);
                    
                    // add background data
                    shipTiles.put(new Point(slotX, slotY), new OrientedModule(modules[i][j].getType(), modules[i][j].getOrientation()));
                }
            }
        }
    }
    
    private Element buildEmptySlot(final int x, final int y) {
        Element e = screen.findElementByName("slotX" + x + "Y" + y);
        if (e == null) {
            final int gridItemSize = (int) (DEFAULT_SLOT_SIZE * scale);
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
            
            return element;
        } else {
            return e;
        }
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
                width("100%");
                height("100%");
                imageMode("sprite:100,100,"+(Integer.parseInt(parentNr)*4));
            }});
        }}.build(nifty, screen, targetParent);
        
        dragCopy.setParent(targetParent);
        
        String id = targetParent.getId();
        int x = Integer.parseInt(id.substring(id.indexOf("X") + 1, id.indexOf("Y")));
        int y = Integer.parseInt(id.substring(id.indexOf("Y") + 1, id.indexOf("#")));
        shipTiles.put(new Point(x, y), new OrientedModule(ModuleType.getType(Integer.parseInt(parentNr))));
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
                    shipTiles.put(new Point(x, y), new OrientedModule(ModuleType.getType(Integer.parseInt(moduleId))));
                    buildNeighborSlots(x, y);

                } else {
                    // delete element at target in gui
                    Element elementToDelete = targetParent.getElements().get(1);                    
                    
                    // counter++ of that module type in inventory
                    int parentId = Integer.parseInt(elementToDelete.getId().substring(11, elementToDelete.getId().lastIndexOf("-")));
                    inventory.addItemOfType(ModuleType.getType(parentId));
                    // counter++ of that module type in gui                    
                    TextRenderer textRenderer = screen.findElementByName(ModuleType.getType(parentId).toString() + "-counter").getRenderer(TextRenderer.class);
                    int count = inventory.getModuleCountInBase(ModuleType.getType(parentId));
                    textRenderer.setText("x" + (count < 10 ? "0" : "") + count);
                    
                    elementToDelete.markForRemoval();
                }
            }
        }
        
        realDragCancel = false;
        
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
    
    private void buildShip() {
        if (!shipTiles.isEmpty()) {
            // calculate size of array
            int minX = 0, maxX = 0, minY = 0, maxY = 0;
            for (Map.Entry pair : shipTiles.entrySet()) {
                int x = ((Point)pair.getKey()).x;
                int y = ((Point)pair.getKey()).y;

                if (x < minX)   minX = x;
                if (x > maxX)   maxX = x;
                if (y < minY)   minY = y;
                if (y > maxY)   maxY = y;
            }

            // create new module array and fill it
            BasicModule[][] modules = new BasicModule[maxX-minX+1][maxY-minY+1];
            
            int xOffset = minX*-1;
            int yOffset = minY*-1;
            
            for (Map.Entry pair : shipTiles.entrySet()) {
                OrientedModule type = ((OrientedModule)pair.getValue());
                int x = ((Point)pair.getKey()).x;
                int y = ((Point)pair.getKey()).y;
                switch(type.moduleType) {
                    case COCKPIT:
                        //System.out.println("setting cockpit at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new Cockpit();
                        break;
                    case THRUSTER:
                        //System.out.println("setting thruster at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new Thruster(fwd, type.facingDirection);
                        break;
                    case ENERGY_GENERATOR:
                        //System.out.println("setting energy at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new EnergyGenerator();
                        break;
                    case ARMOR:
                        //System.out.println("setting armor at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new Armor();
                        break;
                    case WEAPON:
                        //System.out.println("setting weapon at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new LaserGun(weapon, type.facingDirection);
                        break;
                    case ARMOR_DIAGONAL:
                        //System.out.println("setting armor_dia at " + (x+xOffset)+"/"+(y+yOffset));
                        modules[x+xOffset][y+yOffset] = new Armor();
                        break;
                    case SHIELD:
                        modules[x+xOffset][y+yOffset] = new Shield(shield);
                        break;
                    case STORAGE:
                        modules[x+xOffset][y+yOffset] = new Storage();
                        break;
                    default:
                        break;
                }
            }

            // TODO: get ID of current ship, atm its 0 because the player ship gets created before all other test ships
            ServiceManager.getEditorManager().notifyShipChangedListeners(modules, 0);
        }
    }
    
    public void selectCockpit() {
        clearPartsPanel();
        setupPartsPanel(InventoryCategory.COCKPIT_CAT);
    }
    public void selectArmor() {
        clearPartsPanel();
        setupPartsPanel(InventoryCategory.ARMOR_CAT);
    }
    public void selectWeapon() {
        clearPartsPanel();
        setupPartsPanel(InventoryCategory.WEAPON_CAT);
    }
    public void selectEnergy() {
        clearPartsPanel();
        setupPartsPanel(InventoryCategory.ENERGY_CAT);
    }
    public void selectThruster() {
        clearPartsPanel();
        setupPartsPanel(InventoryCategory.THRUSTER_CAT);
    }
}
