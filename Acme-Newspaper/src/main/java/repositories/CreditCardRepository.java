
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

	/**
	 * Gets all the cookie tokens to test if the token is repeated or not
	 * 
	 * @author Daniel Diment
	 * @return The list of all the tokens
	 */
	@Query("select c.cookieToken from CreditCard c")
	Collection<String> getAllCookieTokens();

	/**
	 * Finds a credit card by it's cookie token
	 * 
	 * @author Daniel Diment
	 * @param cookieToken
	 *            The token to search for
	 * @return
	 *         The credit card itself
	 */
	@Query("select c from CreditCard c where c.cookieToken=?1")
	CreditCard findByCookieToken(String cookieToken);

	@Query("select c from Newspaper n join n.creditCards c where n.id=?1 and c.customer.id=?2")
	CreditCard creditCardSubscribed(int newspaperId, int customerId);

	@Query("select c from CreditCard c join c.volumes v where v.id = ?1")
	Collection<CreditCard> getCreditCardsByVolume(int volumeId);
}
