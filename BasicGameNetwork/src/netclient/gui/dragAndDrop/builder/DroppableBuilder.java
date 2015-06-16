package netclient.gui.dragAndDrop.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DroppableBuilder extends ControlBuilder {
  public DroppableBuilder() {
    super("custom-droppable");
  }

  public DroppableBuilder(final String id) {
    super(id, "custom-droppable");
  }
}
