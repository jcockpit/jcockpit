package org.jcockpit.internal.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.jcockpit.core.data.ICategory;
import org.jcockpit.core.data.IElement;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractElement implements IElement {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	
	@ManyToOne(optional=true,targetEntity=Category.class)
	private ICategory category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ICategory getCategory() {
		return category;
	}

	public void setCategory(ICategory category) {
		this.category = category;
	}

}
