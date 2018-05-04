
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Advertisement;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdvertisementServiceTest extends AbstractTest {

	@Autowired
	public AdvertisementService	advertisementService;
	@Autowired
	public ActorService			actorService;
	@Autowired
	public NewspaperService		newspaperService;


	/**
	 * 4. An actor who is authenticated as an agent must be able to:
	 * 2. Register an advertisement and place it in a newspaper.
	 * 3. List the newspapers in which they have placed an advertisement.
	 * 4. List the newspapers in which they have not placed any advertisements
	 */
	@Test
	public void driver() {

		final Object testingData[][] = {//Username, NewspaperIn, NewspaperOut, Title/AdvertismentId, ImageURL, MoreInfoURL, HolderName, BrandName, Number, CVV, ExpirationMonth, ExpirationYear, ExpectedException
			{
				//Positive test
				"agent1", "Newspaper3", "Newspaper3", "Advertisement1", "https://www.hola.jpg", "https://www.hola.jpg", "Valid Holder Name", "Valid Brand Name", "4485677312398507", 123, 12, 20, null, true

			}, {
				//The credit card number must be valid
				"agent1", "Newspaper3", "Newspaper3", "Advertisement1", "https://www.hola.jpg", "https://www.hola.jpg", "Valid Holder Name", "Valid Brand Name", "768", 123, 12, 20, javax.validation.ConstraintViolationException.class, true
			}, {
				//The newspaper must be public
				"agent1", "Newspaper7", "Newspaper7", "Advertisement1", "https://www.hola.jpg", "https://www.hola.jpg", "Valid Holder Name", "Valid Brand Name", "7111111111111111", 123, 12, 20, IllegalArgumentException.class, false
			}
		};
		for (int i = 0; i < testingData.length; i++) {
			if ((Boolean) testingData[i][13])
				this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Integer) testingData[i][11], (Class<?>) testingData[i][12]);
			this.template2((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Integer) testingData[i][11], (Class<?>) testingData[i][12]);

		}
	}

	protected void template(final String username, final String newspaperPopulateIn, final String newspaperPopulateOut, final String title, final String bannerURL, final String additionalInfoLink, final String holderName, final String brandName,
		final String number, final int cvv, final int expirationMonth, final int expirationYear, final Class<?> expected) {
		Advertisement advertisement;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			advertisement = this.advertisementService.create();

			advertisement.setTitle(title);
			advertisement.setBannerURL(bannerURL);
			advertisement.setAdditionalInfoLink(additionalInfoLink);
			advertisement.setHolderName(holderName);
			advertisement.setBrandName(brandName);
			advertisement.setNumber(number);
			advertisement.setCvv(cvv);
			advertisement.setExpirationMonth(expirationMonth);
			advertisement.setExpirationYear(expirationYear);

			this.advertisementService.save(advertisement);

			this.advertisementService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	protected void template2(final String username, final String newspaperPopulateIn, final String newspaperPopulateOut, final String title, final String bannerURL, final String additionalInfoLink, final String holderName, final String brandName,
		final String number, final int cvv, final int expirationMonth, final int expirationYear, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			final int advertisementId = super.getEntityId(title);

			final Advertisement advertisement = this.advertisementService.findOne(advertisementId);

			final int newspaperIdIn = super.getEntityId(newspaperPopulateIn);

			final Newspaper newspaperIn = this.newspaperService.findOne(newspaperIdIn);

			final int newspaperIdOut = super.getEntityId(newspaperPopulateOut);

			final Newspaper newspaperOut = this.newspaperService.findOne(newspaperIdOut);

			this.advertisementService.advertise(advertisement, newspaperIn);

			this.advertisementService.unadvertise(advertisement, newspaperOut);
			final Pageable pageable = new PageRequest(1, 5);

			final Page<Newspaper> withAd = this.newspaperService.findNewspapersWithAdvertisements(advertisementId, true, pageable);
			Assert.isTrue(!withAd.getContent().isEmpty());

			final Page<Newspaper> withNoAd = this.newspaperService.findNewspapersWithAdvertisements(advertisementId, false, pageable);
			Assert.isTrue(withNoAd.getContent().isEmpty());

			this.advertisementService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
