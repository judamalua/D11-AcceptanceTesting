
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	@Query("select (select avg(n.creditCards.size)*1.0/count(n)*1.0 from Newspaper n where n.publicNewspaper = FALSE)*1.0/count(c)*1.0 from Customer c")
	String getRatioSubscribersPrivateNewspaperVSTotalCustomers();

	@Query("select c.customer from CreditCard c join c.volumes v where v.id = ?1")
	Collection<Customer> getCustomersSubscribedToVolume(int volumeId);
}
