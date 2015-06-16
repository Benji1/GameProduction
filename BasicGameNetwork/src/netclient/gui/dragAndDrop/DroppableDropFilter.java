package netclient.gui.dragAndDrop;


public interface DroppableDropFilter {
  boolean accept(Droppable source, Draggable draggable, Droppable target);
}
