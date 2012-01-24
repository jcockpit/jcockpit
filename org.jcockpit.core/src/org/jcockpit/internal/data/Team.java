package org.jcockpit.internal.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.jcockpit.core.data.IActor;
import org.jcockpit.core.data.ITeam;

@Entity
public class Team extends AbstractElement implements ITeam {

	@ManyToMany(targetEntity=Actor.class)
	private List<IActor> members;

	public List<IActor> getMembers() {
		return members;
	}

	public void setMembers(List<IActor> members) {
		this.members = members;
	}
	
}
