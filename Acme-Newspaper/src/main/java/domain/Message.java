
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	// Constructors -----------------------------------------------------------
	//	public Message() {
	//		super();
	//	}

	// Attributes -------------------------------------------------------------
	private String	priority;
	private String	subject;
	private String	body;
	private Date	receptionDate;


	@Pattern(regexp = "HIGH|NEUTRAL|LOW")
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	public Date getReceptionDate() {
		return this.receptionDate;
	}

	public void setReceptionDate(final Date receptionDate) {
		this.receptionDate = receptionDate;
	}


	// Relationships ----------------------------------------------------------

	private Actor			sender;
	private Actor			receiver;
	private MessageFolder	messageFolder;


	@Valid
	@ManyToOne(optional = false)
	@NotNull
	public Actor getSender() {
		return this.sender;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@Valid
	@ManyToOne
	public Actor getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final Actor receiver) {
		this.receiver = receiver;
	}

	@Valid
	@ManyToOne(optional = false)
	public MessageFolder getMessageFolder() {
		return this.messageFolder;
	}

	public void setMessageFolder(final MessageFolder messageFolder) {
		this.messageFolder = messageFolder;
	}

}
