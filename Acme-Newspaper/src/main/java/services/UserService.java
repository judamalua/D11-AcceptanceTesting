
package services;

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
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;
import forms.UserAdminForm;

@Service
@Transactional
public class UserService {

	// Managed repository --------------------------------------------------

	@Autowired
	private UserRepository	userRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator		validator;


	// Simple CRUD methods --------------------------------------------------

	public User create() {
		User result;

		final UserAccount userAccount;
		final Collection<Authority> auth;
		final Authority authority;
		Collection<Article> articles;
		Collection<Chirp> chirps;
		Collection<Newspaper> newspapers;

		articles = new HashSet<Article>();
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

		User result;

		result = this.userRepository.findOne(userId);

		return result;

	}

	public User save(final User user) {

		assert user != null;

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

	// Other business methods ------------------------------------------------------------------------------------

	public User reconstruct(final UserAdminForm userAdminForm, final BindingResult binding) {
		User result;

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

			result.setChirps(chirps);
			result.setNewspapers(newspapers);

		} else {
			result = this.userRepository.findOne(userAdminForm.getId());

			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

		}
		this.validator.validate(result, binding);

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

	private Page<Newspaper> findNewspapersByUser(final int userId, final Pageable pageable) {
		Page<Newspaper> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findNewspapersByUser(userId, pageable);

		return result;
	}

	/**
	 * This method returns the list of published articles of the user with the given id
	 * 
	 * @param userId
	 * @param pageable
	 * @return a page of articles
	 * 
	 * @author Juanmi
	 */
	public Page<Article> findUserPublishedArticles(final int userId, final Pageable pageable) {
		Page<Article> result;
		Assert.isTrue(userId != 0);
		Assert.notNull(pageable);

		result = this.userRepository.findUserPublishedArticles(userId, pageable);

		return result;
	}

	/**
	 * This method flushes the repository, this forces the cache to be saved to the database, which then forces the test data to be validated. This is only used
	 * in tests
	 * 
	 * @author Juanmi
	 */
	public void flush() {
		this.userRepository.flush();
	}
}
