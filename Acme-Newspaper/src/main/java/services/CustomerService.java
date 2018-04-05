
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import domain.Customer;

@Service
@Transactional
public class CustomerService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Customer create() {
		Customer result;

		result = new Customer();

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

	//Dashboard queries ----------------------------------
	/**
	 * Level A query 4
	 * 
	 * @return The ratio of subscribers per private newspaper versus the total number of customers.
	 * @author Antonio
	 */
	//	public String getRatioSubscribersPrivateNewspaperVSTotalCustomers() {
	//		String result;
	//
	//		result = this.customerRepository.getRatioSubscribersPrivateNewspaperVSTotalCustomers();
	//
	//		return result;
	//	}
}
