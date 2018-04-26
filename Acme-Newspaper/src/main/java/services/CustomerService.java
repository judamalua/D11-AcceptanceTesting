
package services;

import java.util.ArrayList;
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
import domain.MessageFolder;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CustomerRepository		customerRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private Validator				validator;


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
		final MessageFolder inbox, outbox, notificationbox, trashbox, spambox;
		final Collection<MessageFolder> messageFolders;

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

			final Collection<MessageFolder> savedMessageFolders = new ArrayList<MessageFolder>();

			for (final MessageFolder mf : messageFolders)
				savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

			result.setMessageFolders(savedMessageFolders);

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
