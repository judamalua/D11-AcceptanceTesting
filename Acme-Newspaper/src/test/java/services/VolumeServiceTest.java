
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Volume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VolumeServiceTest extends AbstractTest {

	@Autowired
	private VolumeService			volumeService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;


	@Test
	public void driverCreateVolume() {

		final Object testingData[][] = {//Title, Description, , Year, Username, ExpectedException
			{
				//Positive test
				"Title", "New description", 2018, "user1", null
			}, {
				//Bad title
				"", "New description", 2018, "user1", javax.validation.ConstraintViolationException.class
			}, {
				//Bad description
				"Title", "", 2018, "user1", javax.validation.ConstraintViolationException.class
			}, {
				//Bad year
				"Title", "New description", null, "user1", javax.validation.ConstraintViolationException.class
			}, {
				//Negative year
				"Title", "New description", -200, "user1", javax.validation.ConstraintViolationException.class
			}, {
				//Null user
				"Title", "New description", 2018, null, IllegalArgumentException.class
			}, {
				//Customer insead of user
				"Title", "New description", 2018, "customer1", java.lang.ClassCastException.class
			}, {
				//Agent insead of user
				"Title", "New description", 2018, "agent1", java.lang.ClassCastException.class
			}, {
				//Admin insead of user
				"Title", "New description", 2018, "admin", java.lang.ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateVolume((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void templateCreateVolume(final String title, final String description, final Integer year, final String username, final Class<?> expected) {
		Volume volume;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			volume = this.volumeService.create();

			volume.setTitle(title);
			volume.setDescription(description);
			volume.setYear(year);

			this.volumeService.save(volume);
			this.volumeService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadUserEditsVolume() {
		Volume volume;
		Integer volumeId;

		volumeId = super.getEntityId("Volume1");
		volume = this.volumeService.findOne(volumeId);

		super.authenticate("user2"); //Bad user

		volume.setTitle("Edited title by user 2");

		this.volumeService.save(volume);

		this.volumeService.flush();

	}

	@Test
	public void testDeleteVolume() {
		Volume volume;
		Integer volumeId;

		volumeId = super.getEntityId("Volume1");
		volume = this.volumeService.findOne(volumeId);

		super.authenticate("user1");

		this.volumeService.delete(volume);
		this.volumeService.flush();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeDeleteVolume() {
		Volume volume;
		Integer volumeId;

		volumeId = super.getEntityId("Volume1");
		volume = this.volumeService.findOne(volumeId);

		super.authenticate("user2");

		this.volumeService.delete(volume);
		this.volumeService.flush();
	}

	@Test
	public void subscribeVolume() {
		CreditCard creditCard;
		Volume volume;
		Integer volumeId;

		super.authenticate("customer2");

		volumeId = super.getEntityId("Volume2");
		volume = this.volumeService.findOne(volumeId);
		creditCard = this.creditCardService.create();

		creditCard.setBrandName("VISA");
		creditCard.setHolderName("Holder name");
		creditCard.setCvv(555);
		creditCard.setExpirationMonth(12);
		creditCard.setExpirationYear(20);
		creditCard.setNumber("4800134737642547");

		this.volumeService.subscribe(creditCard, volume);

		this.volumeService.flush();
	}

}
