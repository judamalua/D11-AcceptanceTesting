
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
	private Validator				validator;


	// Simple CRUD methods --------------------------------------------------
	public MessageFolder create() {

		MessageFolder result;

		result = new MessageFolder();
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

		return result;
	}
	public MessageFolder save(final MessageFolder messageFolder) {

		Assert.notNull(messageFolder);
		this.actorService.checkUserLogin();
		this.checkMessageFolder(messageFolder);

		Actor actor;
		MessageFolder result;
		if (messageFolder.getId() == 0)
			actor = this.actorService.findActorByPrincipal();
		else
			actor = this.actorService.findActorByMessageFolder(messageFolder.getId());

		result = this.messageFolderRepository.save(messageFolder);

		actor.getMessageFolders().remove(messageFolder);
		actor.getMessageFolders().add(result);
		this.actorService.save(actor);

		return result;
	}

	public void delete(final MessageFolder messageFolder) {

		Assert.notNull(messageFolder);
		Assert.isTrue(messageFolder.getId() != 0);
		Assert.isTrue(this.messageFolderRepository.exists(messageFolder.getId()));

		Assert.isTrue(!messageFolder.getIsDefault());

		this.deleteChildrenMessageFolders(messageFolder);

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

		final MessageFolder result;

		result = this.messageFolderRepository.findMessageFolder(name, actor.getId());

		return result;
	}

	public Collection<MessageFolder> showMessageFoldersByPrincipal() {

		this.actorService.checkUserLogin();

		final UserAccount userAccount;
		final Actor actor;
		final Collection<MessageFolder> result;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		result = actor.getMessageFolders();

		return result;
	}

	public Collection<MessageFolder> findRootMessageFolders() {
		this.actorService.checkUserLogin();

		final Actor actor;
		final Collection<MessageFolder> result;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		result = this.messageFolderRepository.findRootMessageFolders(actor.getId());

		Assert.notNull(result);

		return result;

	}

	public Page<MessageFolder> findRootMessageFolders(final Pageable pageable) {
		this.actorService.checkUserLogin();

		final Actor actor;
		final Page<MessageFolder> result;

		actor = this.actorService.findActorByPrincipal();
		Assert.notNull(actor);

		result = this.messageFolderRepository.findRootMessageFolders(actor.getId(), pageable);

		Assert.notNull(result);

		return result;

	}

	public Page<MessageFolder> findMessageFolderChildren(final int messageFolderId, final Pageable pageable) {
		this.actorService.checkUserLogin();
		final Page<MessageFolder> result;

		result = this.messageFolderRepository.findMessageFolderChildren(messageFolderId, pageable);

		Assert.notNull(result);

		return result;
	}

	private MessageFolder deleteChildrenMessageFolders(final MessageFolder messageFolder) {
		final Collection<MessageFolder> messageFolders;
		MessageFolder result;
		Collection<Message> messages;
		Actor actor;

		result = messageFolder;
		messageFolders = new HashSet<MessageFolder>(this.findMessageFolderChildren(messageFolder.getId()));
		messages = this.messageService.findMessagesByMessageFolderId(messageFolder.getId());
		actor = this.actorService.findActorByPrincipal();

		if (messageFolders.isEmpty()) {

			for (final Message m : messages)
				this.messageService.delete(m);

			actor.getMessageFolders().remove(messageFolder);
			this.actorService.save(actor);

			this.messageFolderRepository.delete(messageFolder);

		} else {
			for (final MessageFolder mf : messageFolders)
				this.deleteChildrenMessageFolders(mf);

			this.deleteChildrenMessageFolders(messageFolder);
		}
		return result;
	}

	public Collection<MessageFolder> getHerency(final MessageFolder messageFolder) {
		Assert.notNull(messageFolder);

		final Collection<MessageFolder> family = new ArrayList<>();

		if (this.findMessageFolderChildren(messageFolder.getId()).size() == 0)
			return family;
		else {
			family.addAll(this.findMessageFolderChildren(messageFolder.getId()));
			for (final MessageFolder mf : this.findMessageFolderChildren(messageFolder.getId()))
				family.addAll(this.getHerency(mf));
		}

		return family;
	}

	public Collection<MessageFolder> findMessageFolderChildren(final int messageFolderId) {
		Collection<MessageFolder> result;

		result = this.messageFolderRepository.findMessageFolderChildren(messageFolderId);

		return result;
	}

	public MessageFolder reconstruct(final MessageFolder messageFolder, final BindingResult binding) {
		MessageFolder result;

		if (messageFolder.getId() == 0) {
			result = messageFolder;
			result.setIsDefault(false);

		} else {
			result = this.messageFolderRepository.findOne(messageFolder.getId());
			result.setName(messageFolder.getName());
			result.setMessageFolderFather(messageFolder.getMessageFolderFather());
		}
		this.validator.validate(result, binding);
		this.messageFolderRepository.flush();

		return result;
	}
}
