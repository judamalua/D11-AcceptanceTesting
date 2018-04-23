
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageFolderRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Service
@Transactional
public class MessageFolderService {

	@Autowired
	private MessageFolderRepository	messageFolderRepository;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------
	public MessageFolder create() {

		MessageFolder result;
		//		UserAccount userAccount;
		//
		//		userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);

		result = new MessageFolder();
		result.setMessageFolderChildren(new ArrayList<MessageFolder>());
		result.setMessages(new ArrayList<Message>());
		result.setIsDefault(false);

		return result;
	}
	public Collection<MessageFolder> findAll() {

		Collection<MessageFolder> result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.messageFolderRepository.findAll();
		Assert.notNull(result);

		return result;
	}
	public MessageFolder findOne(final int messageFolderId) {

		Assert.isTrue(messageFolderId != 0);
		Assert.isTrue(this.messageFolderRepository.exists(messageFolderId));

		UserAccount userAccount;
		Actor actor;
		MessageFolder result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		result = this.messageFolderRepository.findOne(messageFolderId);
		Assert.notNull(result);
		//Assert.isTrue(actor.getMessageFolders().contains(result));

		return result;
	}
	public MessageFolder save(final MessageFolder messageFolder) {

		Assert.notNull(messageFolder);
		this.checkMessageFolder(messageFolder);

		// Comprobación palabras de spam
		this.actorService.checkSpamWords(messageFolder.getName());

		final Actor actor;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		//		if (this.actorService.findActorByMessageFolder(messageFolder.getId()) == null)
		//			actor = this.actorService.findActorByPrincipal();
		//		else
		//			actor = this.actorService.findActorByMessageFolder(messageFolder.getId());
		//Assert.notNull(actor);
		//Assert.isTrue(this.messageFolderRepository.exists(messageFolder.getId()) || !messageFolder.getIsDefault() || actor.getMessageFolders().size() < 6);
		//		if (messageFolder.getId() != 0)
		//			Assert.isTrue(!messageFolder.getIsDefault()); CHEQUEAR EN UN MËTODO A PARTE
		MessageFolder result;

		result = this.messageFolderRepository.save(messageFolder);
		//		if (actor.getMessageFolders().contains(messageFolder))
		//			actor.getMessageFolders().remove(messageFolder);
		//		actor.getMessageFolders().add(result);
		//this.actorService.save(actor);

		return result;
	}

	public void delete(final MessageFolder messageFolder) {

		Assert.notNull(messageFolder);
		Assert.isTrue(messageFolder.getId() != 0);
		Assert.isTrue(this.messageFolderRepository.exists(messageFolder.getId()));

		//		Collection<MessageFolder> messageFolderChildren;

		MessageFolder messageFolderFather;

		Assert.isTrue(!messageFolder.getIsDefault());
		//		messageFolderChildren = messageFolder.getMessageFolderChildren();

		//		if (this.actorService.findActorByMessageFolder(messageFolder.getId()) != null) {
		//			userAccount = LoginService.getPrincipal();
		//			Assert.notNull(userAccount);
		//			actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		//			Assert.notNull(actor);
		//
		//			Assert.isTrue(!messageFolder.getIsDefault());
		//			Assert.isTrue(actor.getMessageFolders().contains(messageFolder));
		//
		//			actor.getMessageFolders().remove(messageFolder);
		//			this.actorService.save(actor);
		//		}

		messageFolderFather = messageFolder.getMessageFolderFather();
		if (messageFolderFather != null) {
			messageFolderFather.getMessageFolderChildren().remove(messageFolder);
			this.messageFolderRepository.save(messageFolderFather);
		}

		//		for (final Message m : messages)
		//			this.messageService.delete(m);

		//		messageFolderChildren = messageFolder.getMessageFolderChildren();
		this.deleteChildrenMessageFolders(messageFolder);
		//		for (final MessageFolder messageFolderChild : messageFolderChildren)
		//			this.messageFolderRepository.delete(messageFolderChild);

		//this.messageFolderRepository.delete(messageFolder);

	}
	private void checkMessageFolder(final MessageFolder messageFolder) {

		boolean result;
		final Collection<MessageFolder> mem;

		mem = new HashSet<>();
		result = this.getRootFather(messageFolder, mem);

		Assert.isTrue(result);

	}

	private boolean getRootFather(final MessageFolder messageFolder, final Collection<MessageFolder> mem) {

		boolean result;

		mem.add(messageFolder);

		if (messageFolder.getMessageFolderFather() == null)
			result = true;
		else if (mem.contains(messageFolder.getMessageFolderFather()))
			result = false;
		else
			result = this.getRootFather(messageFolder.getMessageFolderFather(), mem);

		Assert.notNull(result);
		return result;
	}

	public MessageFolder saveDefaultMessageFolder(final MessageFolder messageFolder) {

		Assert.notNull(messageFolder);

		MessageFolder result;

		result = this.messageFolderRepository.save(messageFolder);

		return result;
	}

	public MessageFolder findMessageFolder(final String name, final Actor actor) {
		Assert.notNull(name);
		this.actorService.checkUserLogin();

		final MessageFolder result = null;

		//result = this.messageFolderRepository.findMessageFolder(name, actor.getId());

		return result;
	}

	public Collection<MessageFolder> showMessageFoldersByPrincipal() {

		this.actorService.checkUserLogin();

		final UserAccount userAccount;
		final Actor actor;
		final Collection<MessageFolder> result = null;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		//result = actor.getMessageFolders();

		return result;
	}

	public Collection<MessageFolder> findRootMessageFolders() {
		this.actorService.checkUserLogin();

		final UserAccount userAccount;
		final Actor actor;
		final Collection<MessageFolder> result = null;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		//result = this.messageFolderRepository.findRootMessageFolders(actor.getId());

		Assert.notNull(result);

		return result;

	}

	private MessageFolder deleteChildrenMessageFolders(final MessageFolder messageFolder) {
		final Collection<MessageFolder> messageFolders;
		MessageFolder result;
		Collection<Message> messages;
		Actor actor;

		result = messageFolder;
		messageFolders = new HashSet<MessageFolder>(messageFolder.getMessageFolderChildren());
		messages = messageFolder.getMessages();
		actor = this.actorService.findActorByPrincipal();

		if (messageFolders.isEmpty()) {

			for (final Message m : messages)
				this.messageService.delete(m);

			//actor.getMessageFolders().remove(messageFolder);
			this.actorService.save(actor);

			this.messageFolderRepository.delete(messageFolder);

		} else {
			for (final MessageFolder mf : messageFolders)
				this.deleteChildrenMessageFolders(mf);

			messageFolders.clear();
			messageFolder.setMessageFolderChildren(messageFolders);
			this.deleteChildrenMessageFolders(messageFolder);
		}
		return result;
	}

	public Collection<MessageFolder> getHerency(final MessageFolder messageFolder) {
		Assert.notNull(messageFolder);

		final Collection<MessageFolder> family = new ArrayList<>();

		if (messageFolder.getMessageFolderChildren().size() == 0)
			return family;
		else {
			family.addAll(messageFolder.getMessageFolderChildren());
			for (final MessageFolder mf : messageFolder.getMessageFolderChildren())
				family.addAll(this.getHerency(mf));
		}

		return family;
	}
}
