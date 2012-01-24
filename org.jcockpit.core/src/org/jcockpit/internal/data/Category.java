package org.jcockpit.internal.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;

@Entity
public class Category extends AbstractElement implements ICategory {

	@OneToMany(targetEntity=AbstractElement.class,mappedBy="category")
	private List<IElement> elements;

	public List<IElement> getElements() {
		return elements;
	}

	public void setElements(List<IElement> elements) {
		this.elements = elements;
	}
}
