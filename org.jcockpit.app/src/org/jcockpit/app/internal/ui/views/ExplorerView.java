package org.jcockpit.app.internal.ui.views;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.jcockpit.app.internal.ui.actions.NewAction;
import org.jcockpit.core.CoreActivator;
import org.jcockpit.core.data.ElementEvent;
import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;
import org.jcockpit.core.data.ITeam;
import org.jcockpit.core.data.ModelListener;

public class ExplorerView extends ViewPart {
	public static final String ID = "org.jcockpit.app.explorer";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;

	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		private ModelListener modelListener = new ModelListener() {

			public void elementCreated(ElementEvent evt) {
				//if (viewer.getControl().getDisplay() == Display.getCurrent()) {
					ISelection selection = new StructuredSelection(
							evt.getElement());
					viewer.setSelection(selection, true);
			        viewer.refresh(evt.getElement());
			        viewer.refresh(evt.getElement().getCategory());
			        viewer.refresh();
				//}
			}

			public void elementChanged(ElementEvent evt) {
				if (viewer.getControl().getDisplay() == Display.getCurrent()) {
					ISelection selection = new StructuredSelection(
							evt.getElement());
					viewer.setSelection(selection, true);
			        viewer.refresh(evt.getElement());
				}
			}

		};

		public ViewContentProvider() {
			CoreActivator.getDefault().getElementStore().addListener(modelListener);
		}
		
		public void dispose() {
			CoreActivator.getDefault().getElementStore().removeListener(modelListener);

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
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		hookContextMenu();
		contributeToActionBars();
		viewer.expandToLevel(2);

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				ExplorerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillContextMenu(final IMenuManager manager) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection != null) {
			if ((selection.getFirstElement() instanceof ICategory)) {
				NewAction newCategory = new NewAction(
						(IElement) selection.getFirstElement(),
						ICategory.class, "New category", PlatformUI
								.getWorkbench()
								.getSharedImages()
								.getImageDescriptor(
										ISharedImages.IMG_OBJ_FOLDER));
				manager.add(newCategory);
			} else if (selection.isEmpty()) {
				NewAction newCategory = new NewAction(null, ICategory.class,
						"New category", PlatformUI
								.getWorkbench()
								.getSharedImages()
								.getImageDescriptor(
										ISharedImages.IMG_OBJ_FOLDER));
				manager.add(newCategory);
			}
		}
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

}