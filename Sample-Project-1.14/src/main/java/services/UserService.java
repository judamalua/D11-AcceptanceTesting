
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
import domain.User;

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

	/**
	 * That method create a instance of a user
	 * 
	 * @return User
	 * @author Luis
	 */
	public User create() {
		User result;

		result = new User();

		final UserAccount ua = new UserAccount();
		final Collection<Authority> auth = new HashSet<Authority>();
		final Authority a = new Authority();
		a.setAuthority(Authority.USER);
		auth.add(a);
		ua.setAuthorities(auth);

		result.setUserAccount(ua);

		return result;
	}

	/**
	 * That method returns a collection with all of users of the system
	 * 
	 * @return Collection<User>
	 * @author Luis
	 */
	public Collection<User> findAll() {
		Collection<User> result;

		Assert.notNull(this.userRepository);

		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * That method return an user from his id
	 * 
	 * @param userId
	 * @return User
	 * @author Luis
	 */
	public User findOne(final int userId) {

		User result;

		result = this.userRepository.findOne(userId);

		return result;

	}

	/**
	 * 
	 * That method save a user in the system
	 * 
	 * @param user
	 * @return the saved User
	 * @author Luis
	 */
	public User save(final User user) {
		assert user != null;

		User result;

		result = this.userRepository.save(user);

		return result;

	}

	/**
	 * That method remove a user from the system
	 * 
	 * @param user
	 * @author Luis
	 */
	public void delete(final User user) {

		assert user != null;
		assert user.getId() != 0;

		Assert.isTrue(this.userRepository.exists(user.getId()));

		this.userRepository.delete(user);

	}

	// Other business methods ----------------------------------------------------------------

	public User reconstruct(final User user, final BindingResult binding) {
		User result;

		if (user.getId() == 0) {

			UserAccount userAccount;
			Collection<Authority> authorities;
			Authority authority;

			userAccount = user.getUserAccount();
			authorities = new HashSet<Authority>();
			authority = new Authority();

			result = user;

			authority.setAuthority(Authority.USER);
			authorities.add(authority);
			userAccount.setAuthorities(authorities);

		} else {
			result = this.userRepository.findOne(user.getId());

			result.setName(user.getName());
			result.setSurname(user.getSurname());
			result.setPostalAddress(user.getPostalAddress());
			result.setPhoneNumber(user.getPhoneNumber());
			result.setEmail(user.getEmail());
			result.setBirthDate(user.getBirthDate());

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
}
