
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
				//Checks that you can't subscribe twice to the same newspaper
				"customer1", "Newspaper3", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, IllegalArgumentException.class
			}, {
				//Checks that the Brand name must not be blank.
				"customer1", "Newspaper5", "Valid Holder Name", "", "4485677312398507", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Number must not be blank.
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Number must be a valid Credit Card number.
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "1234567891234567", 123, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the CVV is not outside the minimum range (100).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 99, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the CVV is not outside the maximum range (999).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 1000, 12, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Month is not outside the minimum range (1).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 0, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Month is not outside the maximum range (12).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 13, 20, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Year is not outside the minimum range (0).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, -1, javax.validation.ConstraintViolationException.class
			}, {
				//Checks that the Expiration Year is not outside the maximum range (99).
				"customer1", "Newspaper5", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 100, javax.validation.ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++) {
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6], (Integer) testingData[i][7],
				(Class<?>) testingData[i][8]);
			System.out.println(i);
		}

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
