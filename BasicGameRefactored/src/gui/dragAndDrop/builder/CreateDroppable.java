package gui.dragAndDrop.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;
import gui.dragAndDrop.Droppable;

public class CreateDroppable extends ControlAttributes {
  public CreateDroppable() {
    setAutoId(NiftyIdCreator.generate());
    setName("custom-droppable");
  }

  public CreateDroppable(final String id) {
    setId(id);
    setName("custom-droppable");
  }

  public Droppable create(
      final Nifty nifty,
      final Screen screen,
      final Element parent) {
    nifty.addControl(screen, parent, getStandardControl());
    nifty.addControlsWithoutStartScreen();
    return parent.findNiftyControl(attributes.get("id"), Droppable.class);
  }

  @Override
  public ElementType createType() {
    return new ControlType(attributes);
  }
}
