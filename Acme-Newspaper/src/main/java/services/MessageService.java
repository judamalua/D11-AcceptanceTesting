
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Service
@Transactional
public class MessageService {

	// Managed repository ---------------------------------------------------------------------------
	@Autowired
	private MessageRepository		messageRepository;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private Validator				validator;


	// Simple CRUD methods --------------------------------------------------

	public Message create() {
		Message result;
		Actor actor;
		MessageFolder messageFolder;

		result = new Message();
		actor = this.actorService.findActorByPrincipal();
		messageFolder = this.messageFolderService.findMessageFolder("out box", actor);

		actor = this.actorService.findActorByPrincipal();
		result.setSender(actor);
		messageFolder = this.messageFolderService.findMessageFolder("out box", actor);
		result.setMessageFolder(messageFolder);
		result.setReceptionDate(new Date());

		return result;
	}

	public Collection<Message> findAll() {

		Collection<Message> result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.messageRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Message findOne(final int messageId) {

		Assert.isTrue(messageId != 0);

		UserAccount userAccount;
		Actor actor;
		Message result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		Assert.isTrue(messageId != 0);
		Assert.isTrue(this.messageRepository.exists(messageId));

		result = this.messageRepository.findOne(messageId);
		Assert.notNull(result);
		Assert.isTrue(actor.getMessageFolders().contains(result.getMessageFolder()));

		return result;
	}

	public Message save(final Message message) {

		Assert.notNull(message);

		Actor actor;
		Message result;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		result = this.messageRepository.save(message);

		return result;
	}
	public void delete(final Message message) {

		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		Actor actor;
		//		MessageFolder messageFolder;
		//		MessageFolder trashBox;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));

		//		messageFolder = message.getMessageFolder();
		//		if (messageFolder.getName().equals("trash box") && messageFolder.getIsDefault() == true)
		this.messageRepository.delete(message);//TODO: BUG
		//		else {
		//			trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
		//			this.actorService.moveMessage(message, trashBox);
		//		}

	}
	// Other business methods --------------------------------------------------

	public Message copyMessage(final Message message) {

		Message result;

		result = this.create();
		result.setBody(message.getBody());
		result.setMessageFolder(message.getMessageFolder());
		result.setPriority(message.getPriority());
		result.setReceiver(message.getReceiver());
		result.setSender(message.getSender());
		result.setSubject(message.getSubject());

		return result;
	}

	// Requirement 14.5: An administrator must be able to send a broadcast notification.
	public void broadcastNotification(final Message message) {

		Message messageCopy;
		MessageFolder messageFolder, messageFolderNotification;
		final Actor actor;
		final Collection<Actor> allActors;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		allActors = this.actorService.findAll();
		Boolean first = true;

		for (final Actor receiver : allActors) {
			Assert.notNull(receiver);
			messageCopy = this.copyMessage(message);
			messageFolder = this.messageFolderService.findMessageFolder("out box", receiver);
			messageFolderNotification = this.messageFolderService.findMessageFolder("notification box", receiver);
			messageCopy.setMessageFolder(messageFolder);
			messageCopy.setReceiver(receiver);
			this.actorService.sendMessage(messageCopy, actor, receiver, messageFolderNotification, first, true);
			first = false;
		}

	}
	public Collection<Message> showMessages() {

		this.actorService.checkUserLogin();

		UserAccount userAccount;
		Actor actor;
		Collection<Message> result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		result = this.findMessagesByActorId(actor.getId());

		return result;
	}

	public Collection<Message> findMessagesByActorId(final int id) {

		final Actor actor;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		final Collection<Message> result;

		result = this.messageRepository.findMessagesByActorId(id);

		return result;
	}

	public Page<Message> findMessagesByMessageFolderId(final Integer messageFolderId, final Pageable pageable) {
		Page<Message> result;

		result = this.messageRepository.findMessagesByMessageFolderId(messageFolderId, pageable);

		return result;
	}

	public Collection<Message> findMessagesByMessageFolderId(final Integer messageFolderId) {
		Collection<Message> result;

		result = this.messageRepository.findMessagesByMessageFolderId(messageFolderId);

		return result;
	}

	public Message reconstruct(final Message message, final BindingResult binding) {

		Message result;
		Actor actor;

		result = message;

		if (message.getId() == 0) {
			actor = this.actorService.findActorByPrincipal();
			result.setReceptionDate(new Date(System.currentTimeMillis() - 1));
			result.setSender(actor);
		} else {
			result = this.findOne(message.getId());
			result.setMessageFolder(message.getMessageFolder());
		}
		this.validator.validate(result, binding);
		this.messageRepository.flush();

		return result;
	}
}
