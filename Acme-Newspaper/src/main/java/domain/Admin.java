
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Admin extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Lusit>	lusits;


	@NotNull
	@OneToMany
	public Collection<Lusit> getLusits() {
		return this.lusits;
	}

	public void setLusits(final Collection<Lusit> lusits) {
		this.lusits = lusits;
	}

}
