package org.jcockpit.app.internal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.jcockpit.app.internal.ui.views.ExplorerView;

public class Perspective implements IPerspectiveFactory {


	public static final String ID = "org.jcockpit.app.perspective"; //$NON-NLS-1$
	
	public void createInitialLayout(IPageLayout layout) {
		
		String editorArea = layout.getEditorArea();
	    IFolderLayout topLeft = layout.createFolder( "topLeft", //$NON-NLS-1$
	                                                 IPageLayout.LEFT,
	                                                 0.25f,
	                                                 editorArea );
	    topLeft.addView( ExplorerView.ID );
	    
	    IFolderLayout bottom = layout.createFolder( "bottom", //$NON-NLS-1$
	                                                IPageLayout.BOTTOM,
	                                                0.70f,
	                                                editorArea );
	    bottom.addView( "org.eclipse.ui.views.PropertySheet" ); //$NON-NLS-1$
	}
}
