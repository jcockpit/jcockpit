package org.jcockpit.core.data;


public interface ModelListener {
  void elementCreated( ElementEvent evt );
  void elementChanged( ElementEvent evt );
}
