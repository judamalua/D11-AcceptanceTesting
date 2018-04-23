
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
	private MessageFolder				messageFolderFather;
	private Collection<MessageFolder>	messageFolderChildren;
	private Collection<Message>			messages;


	@Valid
	@ManyToOne(optional = true)
	public MessageFolder getMessageFolderFather() {
		return this.messageFolderFather;
	}

	public void setMessageFolderFather(final MessageFolder messageFolderFather) {
		this.messageFolderFather = messageFolderFather;
	}

	@NotNull
	@OneToMany(mappedBy = "messageFolderFather")
	public Collection<MessageFolder> getMessageFolderChildren() {
		return this.messageFolderChildren;
	}

	public void setMessageFolderChildren(final Collection<MessageFolder> messageFolderChildren) {
		this.messageFolderChildren = messageFolderChildren;
	}

	@NotNull
	@OneToMany(mappedBy = "messageFolder")
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

}
