
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Article extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Newspaper	newspaper;
	private User		user;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Newspaper getnewspaper() {
		return this.newspaper;
	}

	public void setnewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;

	}
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getuser() {
		return this.user;
	}

	public void setuser(final User user) {
		this.user = user;

	}
}
