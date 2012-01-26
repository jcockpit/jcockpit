package org.jcockpit.app.internal.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.jcockpit.app.internal.ui.wizards.NewCategoryWizard;
import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;

public class NewAction extends Action {

	private final IElement element;

	private final Class destinationType;

	public NewAction(final IElement entity, final Class destinationType,
			final String text, final ImageDescriptor imageDescriptor) {
		super(text, imageDescriptor);
		this.element = entity;
		this.destinationType = destinationType;
	}

	@Override
	public void run() {
		Display display = Display.getCurrent();
		Wizard wizard = new NewCategoryWizard((ICategory) element);
		WizardDialog dlg = new WizardDialog(display.getActiveShell(), wizard);
		if (dlg.open() == Window.OK) {
		}
	}
}
