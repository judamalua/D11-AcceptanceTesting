
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Newspaper extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	title;
	private Date	publicationDate;
	private String	description;
	private String	pictureUrl;
	private boolean	publicNewspaper;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(final String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public boolean getPublicNewspaper() {
		return this.publicNewspaper;
	}

	public void setPublic(final boolean publicNewspaper) {
		this.publicNewspaper = publicNewspaper;
	}


	// Relationships ----------------------------------------------------------
	private User					user;
	private Collection<CreditCard>	creditCards;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getuser() {
		return this.user;
	}

	public void setuser(final User user) {
		this.user = user;

	}
	@Valid
	@ManyToMany
	public Collection<CreditCard> getcreditCards() {
		return this.creditCards;
	}

	public void setcreditCards(final Collection<CreditCard> creditCards) {
		this.creditCards = creditCards;

	}
}
