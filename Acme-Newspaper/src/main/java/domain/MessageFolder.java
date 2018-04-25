
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class MessageFolder extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public MessageFolder() {
	//		super();
	//		this.messageFolderChildren = new ArrayList<>();
	//	}

	// Attributes -------------------------------------------------------------
	private String	name;
	private boolean	isDefault;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(final boolean isDefault) {
		this.isDefault = isDefault;
	}


	// Relationships ----------------------------------------------------------
	private MessageFolder	messageFolderFather;


	@Valid
	@ManyToOne(optional = true)
	public MessageFolder getMessageFolderFather() {
		return this.messageFolderFather;
	}

	public void setMessageFolderFather(final MessageFolder messageFolderFather) {
		this.messageFolderFather = messageFolderFather;
	}

}
