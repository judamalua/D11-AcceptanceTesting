
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * Level A query 4
	 * 
	 * @return The ratio of subscribers per private newspaper versus the total number of customers.
	 * @author Antonio
	 */
	//TODO
	//@Query("")
	//String getRatioSubscribersPrivateNewspaperVSTotalCustomers();
}
