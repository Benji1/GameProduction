package gui.dragAndDrop.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import gui.dragAndDrop.Draggable;

public class CreateDraggable extends ControlAttributes {
  public CreateDraggable() {
    setAutoId(NiftyIdCreator.generate());
    setName("custom-draggable");
  }

  public CreateDraggable(final String id) {
    setId(id);
    setName("custom-draggable");
  }

  public Draggable create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Draggable.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
