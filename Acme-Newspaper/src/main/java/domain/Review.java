
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Review extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	title;
	private String	description;
	private Integer	gauge;
	private String	ticker;
	private boolean	draf;


	@NotBlank
	@SafeHtml
	@Length(max = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	@Length(max = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@SafeHtml
	public Integer getGauge() {
		return this.gauge;
	}

	public void setGauge(final Integer gauge) {
		this.gauge = gauge;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^[A-Z][a-z][0-9]{4}_[0-9]{2}-[0-9]{2}-[0-9]{2}")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}


	// Relationships ----------------------------------------------------------
	private Admin		admin;
	private Newspaper	newspaper;


	@NotNull
	@Valid
	@ManyToOne
	public Newspaper getNewspapers() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(final Admin admin) {
		this.admin = admin;
	}

	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public boolean isDraf() {
		return this.draf;
	}

	public void setDraf(final boolean draf) {
		this.draf = draf;
	}

}
