
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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

	public void setPublicNewspaper(final boolean publicNewspaper) {
		this.publicNewspaper = publicNewspaper;
	}


	// Relationships ----------------------------------------------------------
	private Collection<CreditCard>	creditCards;
	private Collection<Article>		articles;


	@Valid
	@ManyToMany
	public Collection<CreditCard> getCreditCards() {
		return this.creditCards;
	}

	public void setCreditCards(final Collection<CreditCard> creditCards) {
		this.creditCards = creditCards;

	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(final Collection<Article> articles) {
		this.articles = articles;
	}

}
