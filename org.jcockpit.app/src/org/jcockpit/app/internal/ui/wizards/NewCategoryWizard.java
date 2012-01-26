package org.jcockpit.app.internal.ui.wizards;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jcockpit.core.CoreActivator;
import org.jcockpit.core.data.ICategory;

public class NewCategoryWizard extends Wizard {

	Text categoryName;
	Text categoryDescription;
	ICategory parent;

	public NewCategoryWizard(ICategory parent) {
		this.parent = parent;
		addPage(new WizardPage("New Category", "New Category", PlatformUI
				.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER)) {

			public void createControl(Composite parent) {
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(new GridLayout(2, false));

				Label lblName = new Label(composite, SWT.LEFT);
				lblName.setText("Name :");

				categoryName = new Text(composite, SWT.SINGLE | SWT.BORDER);
				categoryName.setLayoutData(new GridData(
						GridData.FILL_HORIZONTAL));
				categoryName.addModifyListener(new ModifyListener() {
					public void modifyText(final ModifyEvent event) {
						if (categoryName.getText().length() > 0) {
							setMessage("Create new category",
									IMessageProvider.INFORMATION);
							setPageComplete(true);
						} else {
							setMessage("Category name not valide",
									IMessageProvider.WARNING);
							setPageComplete(false);
						}
					}
				});
				Label lblDescription = new Label(composite, SWT.LEFT);
				lblDescription.setText("Description :");
				categoryDescription = new Text(composite, SWT.MULTI
						| SWT.BORDER);
				categoryDescription.setLayoutData(new GridData(
						GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

				categoryName.setFocus();
				setControl(composite);
			}
		});
	}

	@Override
	public boolean performFinish() {
		CoreActivator.getDefault().getElementStore().createCategory(categoryName.getText(),categoryDescription.getText(),parent);
		return true;
	}

}
