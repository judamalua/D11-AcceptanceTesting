
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chirp;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private ChirpService	chirpService;
	@Autowired
	private ActorService	actorService;


	// Tests ------------------------------------------------------------------
	/**
	 * This driver checks that chirp can be added and findAll return the new value also.
	 * A user may post a chirp. For every chirp, the system must store the moment, a title, and a
	 * description. The list or chirps are considered a part of the profile of a user
	 * 
	 * @author Alejandro
	 */
	@Test
	public void testCaseList() {
		final int prevSize = this.chirpService.findAll().size();
		// Create a new chirp with creation template
		this.templateCreate("User1", new Date(System.currentTimeMillis() - 1), "Test Title", "Test Description", null);
		Assert.isTrue(this.chirpService.findAll().size() - prevSize == 1);
	}

	/**
	 * This driver checks that chirp can be added with taboo and admin can list it.
	 * Requirement 17.4: List the chirps that contain taboo words.
	 * 
	 * @author Alejandro
	 */
	@Test
	public void testCaseListTabooChirps() {
		final int prevSize = this.chirpService.getAllTabooChirps().size();
		// Create a new chirp with creation template
		this.templateCreate("User1", new Date(System.currentTimeMillis() - 1), "Test Title Taboo - Viagra", "Test Description", null);
		Assert.isTrue(this.chirpService.getAllTabooChirps().size() - prevSize == 1);
	}

	/**
	 * This driver checks several tests regarding functional requirement number 17.5 Remove a chirp that he or she thinks is inappropriate.
	 * 
	 * @author Alejandro
	 */
	@Test
	public void driverDeleteChirp() {

		final Object testingData[][] = {
			{
				null, "Chirp1", IllegalArgumentException.class
			}, {
				"User2", "Chirp1", IllegalArgumentException.class
			}, {
				"User1", "Chirp1", IllegalArgumentException.class
			}, {
				"Admin1", "Chirp2", null
			},

			{
				null, "Chirp4", IllegalArgumentException.class
			}, {
				"User2", "Chirp4", IllegalArgumentException.class
			}, {
				"User1", "Chirp4", IllegalArgumentException.class
			}, {
				"Admin1", "Chirp5", null
			},
		};
		for (int i = 0; i < testingData.length; i++) {
			final String user = (String) testingData[i][0];
			final Integer chirp = super.getEntityId((String) testingData[i][1]);
			this.templateDelete(user, chirp, (Class<?>) testingData[i][2]);
		}
	}
	/**
	 * Functional requirement number 15: A user may post a chirp. For every chirp, the system must store the moment, a title, and a
	 * description. The list or chirps are considered a part of the profile of a user
	 * 
	 * @author Alejandro
	 */
	@Test
	public void driverCreateChirp() {
		final Date currentDate = new Date(System.currentTimeMillis() - 1); // Current date 

		final Object testingData[][] = {
			{
				// This test checks that authenticated users can create chirp
				"User1", currentDate, "Test Title", "Test Description", null
			}, {
				// This test checks that unauthenticated users cannot create chirp to a rendezvous already finished
				null, currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create chirp to a rendezvous in draft mode
				"Admin1", currentDate, "Test Title", "Test Description", ClassCastException.class
			}, {
				// This test checks that authenticated users can create chirp
				"User2", currentDate, "Test Title", "Test Description", null
			}, {
				// This test checks that chirp with empty texts cannot be saved
				"User1", currentDate, "", "", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that chirp with empty texts cannot be saved
				"User1", currentDate, "Test Title", "", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that chirp with empty texts cannot be saved
				"User1", currentDate, "", "Test Description", javax.validation.ConstraintViolationException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create chirp with empty texts to a rendezvous
				"Admin1", currentDate, "", "Test Description", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateCreate(final String username, final Date moment, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		Chirp chirp;
		User user;

		caught = null;

		try {
			super.authenticate(username);

			chirp = this.chirpService.create();
			chirp.setMoment(moment);
			chirp.setTitle(title);
			chirp.setDescription(description);
			user = (User) this.actorService.findActorByPrincipal();
			this.chirpService.save(chirp, user);
			//this.chirpService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void templateDelete(final String username, final int chirpId, final Class<?> expected) {
		Class<?> caught;
		Chirp chirp;
		caught = null;

		try {
			chirp = this.chirpService.findOne(chirpId);
			super.authenticate(username);

			this.chirpService.delete(chirp);
			//this.chirpService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
