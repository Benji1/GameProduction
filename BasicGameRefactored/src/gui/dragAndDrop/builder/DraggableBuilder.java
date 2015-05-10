package gui.dragAndDrop.builder;

import de.lessvoid.nifty.builder.ControlBuilder;

public class DraggableBuilder extends ControlBuilder {
  public DraggableBuilder() {
    super("custom-draggable");
  }

  public DraggableBuilder(final String id) {
    super(id, "custom-draggable");
  }

  public void handle(final String handleId) {
    set("handle", handleId);
  }

  public void revert(final boolean revert) {
    set("revert", String.valueOf(revert));
  }

  public void drop(final boolean drop) {
    set("drop", String.valueOf(drop));
  }
}
