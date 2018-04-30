
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Article;
import domain.Chirp;
import domain.MessageFolder;
import domain.Newspaper;
import domain.User;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class UserService {

	// Managed repository --------------------------------------------------

	@Autowired
	private UserRepository			userRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator				validator;

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods --------------------------------------------------

	public User create() {
		User result;

		final UserAccount userAccount;
		final Collection<Authority> auth;
		final Authority authority;
		Collection<Chirp> chirps;
		Collection<Newspaper> newspapers;

		chirps = new HashSet<Chirp>();
		newspapers = new HashSet<Newspaper>();

		result = new User();

		userAccount = new UserAccount();
		auth = new HashSet<Authority>();
		authority = new Authority();
		authority.setAuthority(Authority.USER);
		auth.add(authority);
		userAccount.setAuthorities(auth);

		result.setUserAccount(userAccount);

		result.setChirps(chirps);
		result.setNewspapers(newspapers);

		return result;
	}

	public Collection<User> findAll() {

		Collection<User> result;

		Assert.notNull(this.userRepository);
		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public User findOne(final int userId) {
		Assert.isTrue(userId != 0);

		User result;

		result = this.userRepository.findOne(userId);

		Assert.notNull(result);

		return result;

	}

	public User save(final User user) {
		assert user != null;
		Actor actor = null;

		if (user.getId() != 0)
			actor = this.actorService.findActorByPrincipal();

		if (actor instanceof User && user.getId() != 0)
			Assert.isTrue(user.equals(actor));

		User result;

		result = this.userRepository.save(user);

		return result;

	}

	public void delete(final User user) {

		assert user != null;
		assert user.getId() != 0;

		Assert.isTrue(this.userRepository.exists(user.getId()));

		this.userRepository.delete(user);

	}

	public User findUserByNewspaper(final int newspaperId) {
		User result;
		Assert.isTrue(newspaperId != 0);

		result = this.userRepository.findUserByNewspaper(newspaperId);

		return result;
	}

	public User findUserByArticle(final int articleId) {
		User result;
		Assert.isTrue(articleId != 0);

		result = this.userRepository.findUserByArticle(articleId);

		return result;
	}

	// Other business methods ------------------------------------------------------------------------------------

	public User reconstruct(final UserCustomerAdminForm userAdminForm, final BindingResult binding) {
		User result;
		Actor actor;
		final MessageFolder inbox, outbox, notificationbox, trashbox, spambox;
		final Collection<MessageFolder> messageFolders, savedMessageFolders;

		if (userAdminForm.getId() == 0) {

			Collection<Chirp> chirps;
			Collection<Newspaper> newspapers;

			chirps = new HashSet<Chirp>();
			newspapers = new HashSet<Newspaper>();

			result = this.create();

			result.getUserAccount().setUsername(userAdminForm.getUserAccount().getUsername());
			result.getUserAccount().setPassword(userAdminForm.getUserAccount().getPassword());
			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

			inbox = this.messageFolderService.create();
			inbox.setIsDefault(true);
			inbox.setMessageFolderFather(null);
			inbox.setName("in box");
			outbox = this.messageFolderService.create();
			outbox.setIsDefault(true);
			outbox.setMessageFolderFather(null);
			outbox.setName("out box");
			notificationbox = this.messageFolderService.create();
			notificationbox.setIsDefault(true);
			notificationbox.setMessageFolderFather(null);
			notificationbox.setName("notification box");
			trashbox = this.messageFolderService.create();
			trashbox.setIsDefault(true);
			trashbox.setMessageFolderFather(null);
			trashbox.setName("trash box");
			spambox = this.messageFolderService.create();
			spambox.setIsDefault(true);
			spambox.setMessageFolderFather(null);
			spambox.setName("spam box");

			messageFolders = new ArrayList<MessageFolder>();
			messageFolders.add(inbox);
			messageFolders.add(outbox);
			messageFolders.add(trashbox);
			messageFolders.add(spambox);
			messageFolders.add(notificationbox);

			savedMessageFolders = new ArrayList<MessageFolder>();

			for (final MessageFolder mf : messageFolders)
				savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

			result.setMessageFolders(savedMessageFolders);

			result.setChirps(chirps);
			result.setNewspapers(newspapers);

		} else {
			actor = this.actorService.findActorByPrincipal();
			result = this.userRepository.findOne(userAdminForm.getId());

			Assert.isTrue(result.getId() == actor.getId());

			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

		}
		this.validator.validate(result, binding);
		this.userRepository.flush();

		return result;
	}

	/**
	 * That method returns a collections of users of the system with pageable
	 * 
	 * @param pageable
	 * @return Page<Users>
	 * @author Luis
	 */
	public Page<User> getUsers(final Pageable pageable) {
		Page<User> result;

		result = this.userRepository.findAll(pageable);

		return result;

	}

	public Page<Newspaper> findPublishedNewspapersByUser(final int userId, final Pageable pageable) {
		Page<Newspaper> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findPublishedNewspapersByUser(userId, pageable);

		return result;
	}

	public Page<Newspaper> findNotPublishedNewspapersByUser(final int userId, final Pageable pageable) {
		Page<Newspaper> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findNotPublishedNewspapersByUser(userId, pageable);

		return result;
	}

	public Collection<Newspaper> findNotPublishedNewspapersByUser(final int userId) {
		Collection<Newspaper> result;
		Assert.isTrue(userId != 0);

		result = this.userRepository.findNotPublishedNewspapersByUser(userId);

		return result;
	}

	public Page<Article> findUserPublishedArticles(final int userId, final Pageable pageable) {
		Page<Article> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findUserPublishedArticles(userId, pageable);

		return result;
	}

	public void flush() {
		this.userRepository.flush();
	}

	/**
	 * This method add the user given by parameters to the followed users of the principal or deletes it if was already followed
	 * 
	 * @param user
	 * @author Juanmi
	 */
	public void followOrUnfollowUser(final User user) {
		User principal;

		principal = (User) this.actorService.findActorByPrincipal();

		// Checking that a user cannot follow or unfollow him or herself
		Assert.isTrue(principal.getId() != user.getId());

		// If the principal already follows the user given, it will be deleted from the list
		if (principal.getUsers().contains(user))
			principal.getUsers().remove(user);
		else
			// If the principal does not follow the user given, it will be added to the list
			principal.getUsers().add(user);

		this.save(principal);
	}

	/**
	 * This method returns the list of followed users of the user with the id given
	 * 
	 * @param userId
	 * @param pageable
	 * @return a page of users
	 */
	public Page<User> findFollowedUsers(final int userId, final Pageable pageable) {
		Page<User> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findFollowedUsers(userId, pageable);

		return result;
	}

	/**
	 * This method returns the list of followed users of the user with the id given
	 * 
	 * @param userId
	 * @param pageable
	 * @return a page of users
	 */
	public Page<User> findFollowers(final int userId, final Pageable pageable) {
		Page<User> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findFollowers(userId, pageable);

		return result;
	}

	/**
	 * This method returns the list of chirps of the users who the user with the id given follows
	 * 
	 * @param userId
	 * @param pageable
	 * @return a page of chirps
	 */
	public Page<Chirp> findFollowedUsersChirps(final int userId, final Pageable pageable) {
		Page<Chirp> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findFollowedUsersChirps(userId, pageable);

		return result;
	}

	public Page<Chirp> findChirpsOfUser(final int userId, final Pageable pageable) {
		Page<Chirp> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findChirpsOfUser(userId, pageable);

		return result;
	}

	public User findUserByChirp(final int chirpId) {
		User result;

		result = this.userRepository.findUserByChirp(chirpId);

		return result;
	}
	//Dashboard queries ------------------------

	/**
	 * Level C query 1
	 * 
	 * @return The average and the standard deviation of newspapers created per user.
	 * @author Antonio
	 */
	public String getNewspapersInfoFromUsers() {
		String result;

		result = this.userRepository.getNewspapersInfoFromUsers();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 2
	 * 
	 * @return The average and the standard deviation of articles created per user.
	 * @author Antonio
	 */
	public String getArticlesInfoFromUsers() {
		String result;

		result = this.userRepository.getArticlesInfoFromUsers();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 6
	 * 
	 * @return The ratio of users who have ever created a newspaper.
	 * @author Antonio
	 */
	public String getRatioCreatedNewspapers() {
		String result;

		result = this.userRepository.getRatioCreatedNewspapers();

		return result;
	}

	/**
	 * Level C query 7
	 * 
	 * @return The ratio of users who have ever written an article.
	 * @author Antonio
	 */
	public String getRatioCreatedArticles() {
		String result;

		result = this.userRepository.getRatioCreatedArticles();

		return result;
	}

	/**
	 * Level B query 4
	 * 
	 * @return The average and the standard deviation of the number of chirps per user.
	 * @author Antonio
	 */
	public String getChirpsInfoFromUsers() {
		String result;

		result = this.userRepository.getChirpsInfoFromUsers();

		return result;
	}

	/**
	 * Level B query 5
	 * 
	 * @return The ratio of users who have posted above 75% the average number of chirps per user.
	 * @author Antonio
	 */
	public String getRatioUsersPostedAbove75PercentAverageChirpsPerUser() {
		String result;

		result = this.userRepository.getRatioUsersPostedAbove75PercentAverageChirpsPerUser();

		return result;
	}

	/**
	 * Level A query 5
	 * 
	 * @return The average ratio of private versus public newspapers per publisher.
	 * @author Antonio
	 */
	public String getAverageRatioPrivateNewspaperPerPublisher() {
		String result;

		result = this.userRepository.getAverageRatioPrivateNewspaperPerPublisher();

		return result;
	}

	public Collection<Newspaper> findPublishedNewspapersByUser(final int userId) {
		Collection<Newspaper> result;

		result = this.userRepository.findPublishedNewspapersByUser(userId);

		return result;
	}

}
