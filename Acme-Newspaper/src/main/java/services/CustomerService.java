
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CustomerRepository;
import security.Authority;
import security.UserAccount;
import domain.Customer;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator			validator;


	// Simple CRUD methods --------------------------------------------------

	public Customer create() {
		Customer result;

		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority authority;

		result = new Customer();

		userAccount = new UserAccount();
		authorities = new HashSet<Authority>();
		authority = new Authority();

		authority.setAuthority(Authority.CUSTOMER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Customer> findAll() {

		Collection<Customer> result;

		Assert.notNull(this.customerRepository);
		result = this.customerRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Customer findOne(final int customerId) {

		Customer result;

		result = this.customerRepository.findOne(customerId);

		return result;

	}

	public Customer save(final Customer customer) {

		assert customer != null;

		Customer result;

		result = this.customerRepository.save(customer);

		return result;

	}

	public void delete(final Customer customer) {

		assert customer != null;
		assert customer.getId() != 0;

		Assert.isTrue(this.customerRepository.exists(customer.getId()));

		this.customerRepository.delete(customer);

	}

	// Other business methods --------------------------------------------------------------

	public Customer reconstruct(final UserCustomerAdminForm userCustomerAdminForm, final BindingResult binding) {
		Customer result;

		if (userCustomerAdminForm.getId() == 0) {

			result = this.create();

			result.getUserAccount().setUsername(userCustomerAdminForm.getUserAccount().getUsername());
			result.getUserAccount().setPassword(userCustomerAdminForm.getUserAccount().getPassword());
			result.setName(userCustomerAdminForm.getName());
			result.setSurname(userCustomerAdminForm.getSurname());
			result.setPostalAddress(userCustomerAdminForm.getPostalAddress());
			result.setPhoneNumber(userCustomerAdminForm.getPhoneNumber());
			result.setEmail(userCustomerAdminForm.getEmail());
			result.setBirthDate(userCustomerAdminForm.getBirthDate());

		} else {
			result = this.customerRepository.findOne(userCustomerAdminForm.getId());

			result.setName(userCustomerAdminForm.getName());
			result.setSurname(userCustomerAdminForm.getSurname());
			result.setPostalAddress(userCustomerAdminForm.getPostalAddress());
			result.setPhoneNumber(userCustomerAdminForm.getPhoneNumber());
			result.setEmail(userCustomerAdminForm.getEmail());
			result.setBirthDate(userCustomerAdminForm.getBirthDate());
		}
		this.validator.validate(result, binding);

		return result;
	}

	//Dashboard queries ----------------------------------
	/**
	 * Level A query 4
	 * 
	 * @return The ratio of subscribers per private newspaper versus the total number of customers.
	 * @author Antonio
	 */
	public String getRatioSubscribersPrivateNewspaperVSTotalCustomers() {
		String result;

		result = this.customerRepository.getRatioSubscribersPrivateNewspaperVSTotalCustomers();

		return result;
	}
}
