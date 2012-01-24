package org.jcockpit.app.internal.ui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcockpit.core.CoreActivator;
import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;
import org.jcockpit.core.data.ITeam;

public class ExplorerView extends ViewPart {
	public static final String ID = "org.jcockpit.app.explorer";

	private TreeViewer viewer;

	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ICategory)
				return ((ICategory) parentElement).getElements().toArray();
			if (parentElement instanceof ITeam)
				return ((ITeam) parentElement).getMembers().toArray();
			return null;
		}

		public Object getParent(Object element) {
			if (element instanceof IElement)
				return ((IElement) element).getCategory();
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof ICategory)
				return ((ICategory) element).getElements().size() > 0;
			if (element instanceof ITeam)
				return ((ITeam) element).getMembers().size() > 0;

			return false;
		}

		public Object[] getElements(Object inputElement) {
			return CoreActivator.getDefault().getElementStore()
					.findRootCategories().toArray();
		}
	}

	@SuppressWarnings("serial")
	class ViewLabelProvider extends LabelProvider {
		public String getText(final Object element) {

			if (element instanceof IElement)
				return ((IElement) element).getName();
			return getText(element);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}