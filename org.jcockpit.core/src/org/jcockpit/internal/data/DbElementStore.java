package org.jcockpit.internal.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.jcockpit.core.data.ElementEvent;
import org.jcockpit.core.data.IActor;
import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;
import org.jcockpit.core.data.IElementStore;
import org.jcockpit.core.data.ModelListener;

public class DbElementStore implements IElementStore {

	private List<ModelListener> listeners = new ArrayList<ModelListener>();
	private EntityManagerFactory emf;

	public DbElementStore() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS,
				"jcockpit");
		properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML,
				PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML_DEFAULT);

		properties.put(PersistenceUnitProperties.DDL_GENERATION,
				PersistenceUnitProperties.CREATE_ONLY);
		properties.put(PersistenceUnitProperties.DDL_GENERATION,
				PersistenceUnitProperties.DROP_AND_CREATE);
		properties.put("eclipselink.ddl-generation.output-mode", "database");
		properties.put("eclipselink.classloader",
				Category.class.getClassLoader());
		emf = new PersistenceProvider().createEntityManagerFactory("jcockpit",
				properties);
		devSetup();
	}

	public List<ICategory> findRootCategories() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<ICategory> query = em.createQuery(
					"select c from Category c where c.category is null",
					ICategory.class);

			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<IElement> loadAll() {
		return loadAll(IElement.class);
	}

	public <T> List<T> loadAll(Class<T> clazz) {
		EntityManager em = emf.createEntityManager();
		try {
			CriteriaQuery<T> q = em.getCriteriaBuilder().createQuery(clazz);
			Root<T> e = q.from(clazz);
			TypedQuery<T> query = em.createQuery(q);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public void save(IElement element) {
		EntityManager em = emf.createEntityManager();
		try {
			em.persist(element);
		} finally {
			em.close();
		}
	}

	private void devSetup() {
		EntityManager em = emf.createEntityManager();
		try {
			Category c = em.find(Category.class, Long.valueOf(1));
			if (c == null) {
				em.getTransaction().begin();
				c = new Category();
				c.setDescription("Forge server");
				c.setName("forge");
				c.setElements(new ArrayList<IElement>());
				em.persist(c);

				Category ci = new Category();
				ci.setDescription("CI Serveur");
				ci.setName("ci");
				ci.setCategory(c);
				c.getElements().add(ci);
				em.persist(ci);

				Category qa = new Category();
				qa.setDescription("QA Serveur");
				qa.setName("qa");
				qa.setCategory(c);
				c.getElements().add(qa);
				em.persist(qa);

				Actor a1 = new Actor();
				a1.setName("dev1");
				em.persist(a1);
				Team t = new Team();
				t.setName("myTeam");
				t.setMembers(new ArrayList<IActor>());
				t.getMembers().add(a1);
				t.setCategory(c);
				c.getElements().add(t);
				em.persist(t);

				em.merge(c);
				em.getTransaction().commit();
			}
		} finally {
			em.close();
		}
	}

	public ICategory createCategory(String name, String description,
			ICategory parent) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			Category c = new Category();
			c.setDescription(description);
			c.setName(name);
			c.setCategory(parent);
			em.persist(c);
			if (parent != null) {
				parent.getElements().add(c);
				em.merge(parent);
			}
			em.getTransaction().commit();
			notifyOnCreated(c);
			return c;
		} finally {
			em.close();
		}
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	void notifyOnCreated(final IElement element) {
		ModelListener[] modelListeners = new ModelListener[listeners.size()];
		listeners.toArray(modelListeners);
		ElementEvent evt = new ElementEvent(element);
		for (int i = 0; i < modelListeners.length; i++) {
			try {
				modelListeners[i].elementCreated(evt);
			} catch (final RuntimeException re) {

				// TODO
			}
		}
	}
}
