package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Chirp;
import domain.User;

import utilities.AbstractTest;

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
	 * Requirement 15.1 An actor who is not authenticated must be able to:
	 * List the chirps that are associated with each rendezvous.
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
	 * This driver checks several tests regarding functional requirement number 21.1: An actor who is authenticated as a user must be able to manage
	 * (add, edit, delete) the chirps that are associated with a rendezvous on draft mode that he or she has created previously, tests are explained inside
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
				"User1", "Chirp1", null
			}, {
				"Admin1", "Chirp2", null
			},

			{
				null, "Chirp4", IllegalArgumentException.class
			}, {
				"User2", "Chirp4", IllegalArgumentException.class
			}, {
				"User1", "Chirp4", null
			}, {
				"Admin1", "Chirp5", null
			},
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	/**
	 * Functional requirement number 16.3: An actor who is authenticated as a user must be able to: Create an chirp regarding
	 * one of the rendezvouses that he or she's created previously. *
	 * 
	 * @author Alejandro
	 */
	@Test
	public void driverCreateChirp() {
		final Date currentDate = new Date(System.currentTimeMillis() - 1); // Current date 

		final Object testingData[][] = {
			{
				// This test checks that authenticated users cannot create chirp to a rendezvous already finished.
				"User1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users can add an chirp to a Rendezvous that they have created in final mode
				"User1", currentDate, "Test Title", "Test Description", null
			}, {
				// This test checks that unauthenticated users cannot create chirp to a rendezvous already finished
				null, currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that unauthenticated users cannot create questions to a rendezvous not finished
				null, currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create chirp to a rendezvous in draft mode
				"Admin1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create chirp to a rendezvous in final mode
				"Admin1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated actors that are not users cannot create chirp to a rendezvous in final mode
				"Admin1", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create chirp to a draft mode rendezvous they did not create
				"User2", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create chirp to a final mode rendezvous they did not create
				"User2", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
			}, {
				// This test checks that authenticated users cannot create chirp to a final mode rendezvous they did not create
				"User2", currentDate, "Test Title", "Test Description", IllegalArgumentException.class
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
