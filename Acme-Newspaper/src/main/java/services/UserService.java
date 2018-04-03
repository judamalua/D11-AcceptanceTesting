
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.UserAccount;
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

		result = new User();

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

	public User findUserByArticle(final int articleId) {
		User result;
		Assert.isTrue(articleId != 0);

		result = this.userRepository.findUserByArticle(articleId);

		return result;
	}
	// Other business methods

	public User reconstruct(final UserAdminForm user, final BindingResult binding) {
		User result;

		if (user.getId() == 0) {

			UserAccount userAccount;
			Collection<Authority> authorities;
			Authority authority;

			userAccount = user.getUserAccount();
			authorities = new HashSet<Authority>();
			authority = new Authority();

			result = this.create();
			//Arreglar

			authority.setAuthority(Authority.ADMIN);
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
}
