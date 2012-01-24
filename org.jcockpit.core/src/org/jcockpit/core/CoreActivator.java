package org.jcockpit.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
//import org.eclipse.persistence.jpa.PersistenceProvider;
import org.jcockpit.core.data.IElement;
import org.jcockpit.core.data.IElementStore;
import org.jcockpit.internal.data.Category;
import org.jcockpit.internal.data.DbElementStore;
import org.osgi.framework.BundleContext;

public class CoreActivator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jcockpit.core"; //$NON-NLS-1$

	// The shared instance
	private static CoreActivator plugin;

	private IElementStore elementStore;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		elementStore = new DbElementStore();
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static CoreActivator getDefault() {
		return plugin;
	}

	public IElementStore getElementStore() {
		return elementStore;
	}

}