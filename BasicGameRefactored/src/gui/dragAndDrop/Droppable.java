package gui.dragAndDrop;

import de.lessvoid.nifty.controls.NiftyControl;

/**
 * The Droppable NiftyControl interface.
 * @author void
 */
public interface Droppable extends NiftyControl {

  /**
   * Add the given filter to this Droppable. A DroppableDropFilter will be asked if
   * a certain Draggable is able to be dropped on this Droppable.
   * @param droppableDropFilter the filter
   */
  void addFilter(DroppableDropFilter droppableDropFilter);

  /**
   * Remove the given filter.
   * @param filter the filter to be removed
   */
  void removeFilter(DroppableDropFilter filter);

  /**
   * Remove all Filters.
   */
  void removeAllFilters();
}
