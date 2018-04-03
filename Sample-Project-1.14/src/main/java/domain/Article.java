
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Article extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String				title;
	private String				summary;
	private String				body;
	private Collection<String>	pictureUrls;
	private boolean				finalMode;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@URL
	public Collection<String> getPictureUrls() {
		return this.pictureUrls;
	}

	public void setPictureUrls(final Collection<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public boolean getFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}


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
