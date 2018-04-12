
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Admin;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	// The SUT ---------------------------------------------------------------
	@Autowired
	private AdminService	adminService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverCreateAdmin() {

		final Object testingData[][] = {
			{
				"admin", "AdminUser", "adminPass", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", null
			}, {
				"admin", "AdminUser", "adminPass", "", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", DataIntegrityViolationException.class
			}, {
				"admin", "AdminUser", "", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", DataIntegrityViolationException.class
			}, {
				null, "AdminUser", "adminPass", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", DataIntegrityViolationException.class
			}, {
				"User1", "AdminUser", "adminPass", "TestName", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", DataIntegrityViolationException.class
			}, {
				"admin", "", "TestName", "adminPass", "TestSurname Test", new Date(System.currentTimeMillis() - 1), "testEmail@gmail.com", "655555555", "Test Address", DataIntegrityViolationException.class
			},

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAdmin((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void templateCreateAdmin(final String login, final String userAdmin, final String passwordAdmi, final String name, final String surname, final Date birthDate, final String email, final String phoneNumber, final String address,
		final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			Admin newAdmin;
			UserAccount usAcc;
			Authority auth;

			super.authenticate(login);

			newAdmin = this.adminService.create();

			usAcc = new UserAccount();
			usAcc.setUsername(userAdmin);
			usAcc.setPassword("admin");
			auth = new Authority();
			auth.setAuthority("ADMIN");
			usAcc.addAuthority(auth);

			newAdmin.setUserAccount(usAcc);

			newAdmin.setName(name);
			newAdmin.setSurname(surname);
			newAdmin.setBirthDate(birthDate);
			newAdmin.setEmail(email);
			newAdmin.setPhoneNumber(phoneNumber);
			newAdmin.setPostalAddress(address);

			this.adminService.save(newAdmin);
			this.adminService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
