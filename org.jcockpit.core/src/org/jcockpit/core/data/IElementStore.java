package org.jcockpit.core.data;

import java.util.List;

public interface IElementStore {	
	List<ICategory> findRootCategories();
	void save(IElement  element);	
}
