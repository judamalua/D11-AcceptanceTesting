
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<User>	users;


	@Valid
	@ManyToMany
	public Collection<User> getusers() {
		return this.users;
	}

	public void setusers(final Collection<User> users) {
		this.users = users;

	}
}
