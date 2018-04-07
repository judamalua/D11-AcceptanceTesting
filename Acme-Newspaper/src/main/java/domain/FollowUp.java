
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class FollowUp extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String				title;
	private Date				publicationDate;
	private String				summary;
	private String				text;
	private Collection<String>	pictureUrls;
	private boolean				taboo;


	@SafeHtml
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
	@NotNull
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	@SafeHtml
	@NotBlank
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}
	@SafeHtml(whitelistType = WhiteListType.BASIC_WITH_IMAGES)
	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}
	@SafeHtml
	@NotNull
	@ElementCollection
	@URL
	public Collection<String> getPictureUrls() {
		return this.pictureUrls;
	}

	public void setPictureUrls(final Collection<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public boolean isTaboo() {
		return this.taboo;
	}

	public void setTaboo(final boolean taboo) {
		this.taboo = taboo;
	}


	// Relationships ----------------------------------------------------------
	private User	user;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;

	}

}
