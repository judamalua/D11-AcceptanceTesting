
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String				cookies_eng;
	private String				cookies_es;
	private String				businessNameFirst;
	private String				businessNameLast;
	private Integer				pageSize;
	private Collection<String>	tabooWords;


	@SafeHtml
	@NotBlank
	public String getCookies_eng() {
		return this.cookies_eng;
	}

	public void setCookies_eng(final String cookies_eng) {
		this.cookies_eng = cookies_eng;
	}

	@SafeHtml
	@NotBlank
	public String getCookies_es() {
		return this.cookies_es;
	}

	public void setCookies_es(final String cookies_es) {
		this.cookies_es = cookies_es;
	}

	@SafeHtml
	@NotBlank
	public String getBusinessNameFirst() {
		return this.businessNameFirst;
	}

	public void setBusinessNameFirst(final String businessNameFirst) {
		this.businessNameFirst = businessNameFirst;
	}

	@SafeHtml
	@NotBlank
	public String getBusinessNameLast() {
		return this.businessNameLast;
	}

	public void setBusinessNameLast(final String businessNameLast) {
		this.businessNameLast = businessNameLast;
	}

	@NotNull
	@Range(min = 1)
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(final Integer pageSize) {
		this.pageSize = pageSize;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getTabooWords() {
		return this.tabooWords;
	}

	public void setTabooWords(final Collection<String> tabooWords) {
		this.tabooWords = tabooWords;
	}

	// Relationships ----------------------------------------------------------

}
