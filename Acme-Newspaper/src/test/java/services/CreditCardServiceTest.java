
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreditCardServiceTest extends AbstractTest {

	@Autowired
	public CreditCardService	creditCardService;
	@Autowired
	public CustomerService		customerService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public NewspaperService		newspaperService;


	@Test
	public void driver() {

		final Object testingData[][] = {//Username, Newspaper, HolderName, BrandName, Number, CVV, ExpirationMonth, ExpirationYear, ExpectedException
			{
				//Positive test
				"customer1", "Newspaper3", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, null
			}, {
				//Negative test
				"customer1", "Newspaper3", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
				(Class<?>) testingData[i][8]);

	}

	protected void template(final String username, final String newspaperPopulate, final String holderName, final String brandName, final String number, final int cvv, final int expirationMonth, final int expirationYear, final Class<?> expected) {
		CreditCard creditCard;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			creditCard = this.creditCardService.create();

			final int newspaperId = super.getEntityId(newspaperPopulate);

			final Newspaper newspaper = this.newspaperService.findOne(newspaperId);

			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(number);
			creditCard.setCvv(cvv);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);

			this.creditCardService.subscribe(creditCard, newspaper);

			this.creditCardService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
