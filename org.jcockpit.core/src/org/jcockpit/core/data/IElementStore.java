package org.jcockpit.core.data;

import java.util.List;

public interface IElementStore {	
	
	void addListener(ModelListener listener);
	void removeListener(ModelListener listener);
	List<ICategory> findRootCategories();
	ICategory createCategory(String name, String description,ICategory parent);
	void save(IElement  element);	
}
