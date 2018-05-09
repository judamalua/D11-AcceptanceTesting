
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
import domain.Configuration;
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
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;


	/**
	 * This test checks many things, which are commented above of each row
	 * 
	 * Functional requirement:
	 * An actor who is authenticated as a user must be able to:
	 * Create a volume with as many published newspapers as he or she wishes.
	 * Note that the newspapers in a volume can be added or removed at any time.
	 * The same newspaper may be used to create different volumes.
	 */
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

	/**
	 * This test checks that only the creator of the Volume can edit it
	 * 
	 * Functional requirement:
	 * An actor who is authenticated as a user must be able to:
	 * Create a volume with as many published newspapers as he or she wishes.
	 * Note that the newspapers in a volume can be added or removed at any time.
	 * The same newspaper may be used to create different volumes.
	 */
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
	/**
	 * This test checks that only the creator of the Volume can delete it
	 * 
	 * Functional requirement:
	 * An actor who is authenticated as a user must be able to:
	 * Create a volume with as many published newspapers as he or she wishes.
	 * Note that the newspapers in a volume can be added or removed at any time.
	 * The same newspaper may be used to create different volumes.
	 */
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

	/**
	 * This test checks that only the creator of the Volume can delete it
	 * 
	 * Functional requirement:
	 * An actor who is authenticated as a user must be able to:
	 * Create a volume with as many published newspapers as he or she wishes.
	 * Note that the newspapers in a volume can be added or removed at any time.
	 * The same newspaper may be used to create different volumes.
	 */
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

	/**
	 * This test checks many things, which are commented above of each row
	 * 
	 * Functional requirement:
	 * An actor who is authenticated as a customer must be able to:
	 * Subscribe to a volume by providing a credit card.
	 * Note that subscribing to a volume implies subscribing automatically to all of the newspapers of which it is composed,
	 * including newspapers that might be published after the subscription takes place.
	 */
	@Test
	public void driverSubscribeVolume() {

		final Object testingData[][] = {//Brand name, Holder name, CVV, Expiration month, Expiration year, Number, username, EntityId,ExpectedException
			{
				//No customer authenticated
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", null, "Volume2", IllegalArgumentException.class
			}, {
				//Authenticated as user
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", "user1", "Volume2", ClassCastException.class
			}, {
				//Authenticated as admin
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", "admin1", "Volume2", ClassCastException.class
			}, {
				//Authenticated as agent
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", "agent1", "Volume2", ClassCastException.class
			}, {
				//Credit card about to expire or expired
				"Brand name", "Holder name", 555, 05, 18, "4800134737642547", "customer2", "Volume2", IllegalArgumentException.class
			}, {
				//Positive test
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", "customer2", "Volume2", null
			}, {
				//Positive test
				"Brand name", "Holder name", 555, 12, 20, "4800134737642547", "customer2", "Volume1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSubscribeVolume((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateSubscribeVolume(final String brandName, final String holderName, final Integer cvv, final Integer expMonth, final Integer expYear, final String number, final String username, final String entityId, final Class<?> expected) {
		CreditCard creditCard;
		Volume volume;
		Integer volumeId;
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);

			volumeId = super.getEntityId(entityId);
			volume = this.volumeService.findOne(volumeId);
			creditCard = this.creditCardService.create();

			creditCard.setBrandName(brandName);
			creditCard.setHolderName(holderName);
			creditCard.setCvv(cvv);
			creditCard.setExpirationMonth(expMonth);
			creditCard.setExpirationYear(expYear);
			creditCard.setNumber(number);

			this.volumeService.subscribe(creditCard, volume);

			this.volumeService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * This test cheks that any user can list the volumes in the system.
	 * 
	 * Functional requirement:
	 * An actor who is not authenticated must be able to:
	 * List the volumes in the system and browse their newspapers as long as
	 * they are authorised (for instance, a private newspaper cannot be fully displayed
	 * to unauthenticated actors).
	 */
	@Test
	public void listNotLoggedVolumesPositive() {
		Page<Volume> volumes;
		Pageable pageable;
		Configuration configuration;

		configuration = this.configurationService.findConfiguration();

		pageable = new PageRequest(0, configuration.getPageSize());

		super.authenticate(null);

		volumes = this.volumeService.findVolumes(pageable);

		Assert.isTrue(volumes.getContent().size() > 0);

		super.authenticate("user1");

		volumes = this.volumeService.findVolumes(pageable);

		Assert.isTrue(volumes.getContent().size() > 0);

		super.authenticate("agent1");

		volumes = this.volumeService.findVolumes(pageable);

		Assert.isTrue(volumes.getContent().size() > 0);

		super.authenticate("customer1");

		volumes = this.volumeService.findVolumes(pageable);

		Assert.isTrue(volumes.getContent().size() > 0);
	}

}
