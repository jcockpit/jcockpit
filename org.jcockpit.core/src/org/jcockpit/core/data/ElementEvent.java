package org.jcockpit.core.data;


public class ElementEvent {
  
  private final IElement element;

  public ElementEvent( final IElement element ) {
    this.element = element;
  }
  
  public IElement getElement() {
    return element;
  }
}
