
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Message;
import domain.MessageFolder;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class ActorService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ActorRepository			actorRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private MessageFolderService	messageFolderService;


	// Simple CRUD methods --------------------------------------------------
	/**
	 * Get all the actors in the system
	 * 
	 * @return all the actors registered in the system
	 * @author MJ
	 */
	public Collection<Actor> findAll() {

		Collection<Actor> result;

		Assert.notNull(this.actorRepository);
		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * Get the actor with the id passed as parameter
	 * 
	 * @param actorId
	 * @return an actor with id equals to actorId
	 * 
	 * @author MJ
	 */
	public Actor findOne(final int actorId) {

		Actor result;

		result = this.actorRepository.findOne(actorId);

		return result;

	}

	/**
	 * That method create a instance of a user
	 * 
	 * @return User
	 * @author Luis
	 */
	public Admin createAdmin() {
		Admin result;

		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority authority;

		result = new Admin();

		userAccount = new UserAccount();
		authorities = new HashSet<Authority>();
		authority = new Authority();

		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);

		return result;
	}

	/**
	 * Saves the actor passed as parameter
	 * 
	 * @param actor
	 * @return The actor saved in the system
	 * @author Luis
	 */
	public Actor save(final Actor actor) {

		assert actor != null;

		Actor result;

		result = this.actorRepository.save(actor);

		return result;

	}

	/**
	 * Delete the actor passed as parameter
	 * 
	 * @param actor
	 * @author Luis
	 */
	public void delete(final Actor actor) {

		assert actor != null;
		assert actor.getId() != 0;

		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);

	}

	/**
	 * Get the actor logged in the system
	 * 
	 * @return the actor logged in the system
	 * @author MJ
	 */
	public Actor findActorByPrincipal() {
		UserAccount userAccount;
		Actor result;

		userAccount = LoginService.getPrincipal();
		result = this.findActorByUserAccount(userAccount);

		return result;
	}

	/**
	 * Get an actor with the UserAccount passed
	 * 
	 * @param userAccount
	 * @return The actor with the UserAccount
	 * @author MJ
	 */
	public Actor findActorByUserAccount(final UserAccount userAccount) {

		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findActorByUserAccountId(userAccount.getId());

		return result;
	}

	/**
	 * Checks there is an actor logged in the system
	 * 
	 * @author MJ
	 */
	public void checkUserLogin() {
		Actor actor;

		actor = this.findActorByPrincipal();

		Assert.notNull(actor);
	}

	/**
	 * This method checks if there is someone logged in the system
	 * 
	 * @return true if there is someone logged, false otherwise
	 * @author Juanmi
	 */
	public boolean getLogged() {
		boolean result;

		//		result = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		try {
			this.checkUserLogin();
			result = true;
		} catch (final Throwable oops) {
			result = false;
		}
		return result;
	}

	/**
	 * This method registers in the system the actor passed by parameters
	 * 
	 * @param actor
	 * @return the actor registered
	 * @author Luis
	 */
	public Actor registerActor(final Actor actor) {
		Actor result;
		String password;
		Md5PasswordEncoder encoder;

		Assert.notNull(actor.getUserAccount());
		Assert.isTrue(!this.actorRepository.exists((actor.getId())));

		encoder = new Md5PasswordEncoder();

		password = actor.getUserAccount().getPassword();
		password = encoder.encodePassword(password, null);
		actor.getUserAccount().setPassword(password);

		result = this.actorRepository.save(actor);

		return result;
	}

	/**
	 * This method deconstructs an User/Administrator object, that is, transforms
	 * an User/Administrator object into a UserAdminForm object to be edited
	 * 
	 * @param user
	 *            to be deconstructed into an UserAdminForm
	 * @return UserAdminForm with the data of the user given by parameters
	 * 
	 * @author Juanmi
	 */
	public UserCustomerAdminForm deconstruct(final Actor actor) {
		UserCustomerAdminForm result;

		result = new UserCustomerAdminForm();

		result.setId(actor.getId());
		result.setVersion(actor.getVersion());
		result.setName(actor.getName());
		result.setSurname(actor.getSurname());
		result.setPhoneNumber(actor.getPhoneNumber());
		result.setEmail(actor.getEmail());
		result.setBirthDate(actor.getBirthDate());
		result.setPostalAddress(actor.getPostalAddress());

		return result;
	}

	public boolean checkSpamWords(final String stringToCheck) {
		boolean result = false;

		final Collection<String> spamWords = this.configurationService.findConfiguration().getTabooWords();
		for (final String spamWord : spamWords) {
			result = stringToCheck.toLowerCase().contains(spamWord);
			if (result)
				break;
		}
		return result;
	}

	public Actor findActorByMessageFolder(final int id) {
		this.checkUserLogin();

		Actor result;

		result = this.actorRepository.findActorByMessageFolder(id);

		return result;
	}

	public Message moveMessage(final Message message, final MessageFolder folder) {

		Assert.notNull(folder);
		Assert.isTrue(folder.getId() != 0);
		this.checkUserLogin();

		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));
		Assert.isTrue(actor.getMessageFolders().contains(folder));

		final MessageFolder messageFolderOr;
		Message result;

		messageFolderOr = message.getMessageFolder();

		this.messageFolderService.save(messageFolderOr);

		message.setMessageFolder(folder);
		result = this.messageService.save(message);

		return result;
	}

	public void checkMessageFolders(final Actor a) {

		Integer count;
		Collection<MessageFolder> messageFolders;

		count = 0;
		messageFolders = a.getMessageFolders();

		for (final MessageFolder mf : messageFolders)
			if (mf.getIsDefault() == true)
				count++;
		Assert.isTrue(count == 5);

	}

	public void sendMessage(final Message message, final Actor sender, final Actor receiver, final MessageFolder messageFolderReceiver) {

		Assert.notNull(message);
		this.checkUserLogin();

		UserAccount userAccount;
		Message messageCopy;
		MessageFolder outBoxSender;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		Assert.notNull(sender);

		messageCopy = this.messageService.copyMessage(message);

		messageCopy.setMessageFolder(messageFolderReceiver);

		outBoxSender = this.messageFolderService.findMessageFolder("out box", sender);
		if (!this.messageService.findMessagesByMessageFolderId(outBoxSender.getId()).contains(message)) {
			message.setMessageFolder(outBoxSender);
			this.messageService.save(message);
		}

		this.messageService.save(messageCopy);
	}

}
