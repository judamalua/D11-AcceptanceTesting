
package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Agent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AgentServiceTest extends AbstractTest {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private AgentService	agentService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * 3.1 An actor who is not authenticated must be able to: Register to the system as an agent.
	 * 
	 * 
	 * This test checks that a not registered agent can register himself in the system,without errors
	 * 
	 */
	@Test
	public void testRegisterANewagent() {
		Agent newAgent;
		final Date birthDate = new Date();

		newAgent = this.agentService.create();

		newAgent.setName("Fernando");
		newAgent.setSurname("Gutiérrez López");
		newAgent.setBirthDate(birthDate);
		newAgent.setEmail("ferguti90@gmail.com");
		newAgent.setPhoneNumber("606587789");
		newAgent.setPostalAddress("Calle Nervion 9");

		this.agentService.save(newAgent);

	}

	/**
	 * This test checks that an agent can edit his profile correctly
	 * 
	 */
	@Test
	public void testEditProfile() {
		super.authenticate("Agent1");

		Agent agent;

		agent = (Agent) this.actorService.findActorByPrincipal();
		agent.setPhoneNumber("658877877");
		agent.setEmail("agent1newEmail@gmail.com");
		agent.setPostalAddress("Calle Capitanía 13a");

		this.agentService.save(agent);

	}

	//******************************************Negative Methods*******************************************************************
	/**
	 * 3.1 An actor who is not authenticated must be able to: Register to the system as an agent.
	 * 
	 * 
	 * This test checks that a not registered agent cannot register himself in the system,without a valid name
	 * 
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewagentWithInvalidName() {
		Agent newAgent;
		final Date birthDate = new Date();

		newAgent = this.agentService.create();

		newAgent.setName("");//Name not valid(is blank)
		newAgent.setSurname("Gutiérrez López");
		newAgent.setBirthDate(birthDate);
		newAgent.setEmail("ferguti90@gmail.com");
		newAgent.setPhoneNumber("606587789");
		newAgent.setPostalAddress("Calle Nervion 9");

		this.agentService.save(newAgent);
		this.agentService.flush();

	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a agent.
	 * 
	 * This test checks that a not registered agent cannot register himself in the system,without a valid surname
	 * 
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewagentWithInvalidSurname() {
		Agent newagent;

		final Date birthDate = new Date();

		newagent = this.agentService.create();

		newagent.setName("Fernando");
		newagent.setSurname("");//Surname not valid(is blank)
		newagent.setBirthDate(birthDate);
		newagent.setEmail("ferguti90@gmail.com");
		newagent.setPhoneNumber("606587789");
		newagent.setPostalAddress("Calle Nervion 9");

		this.agentService.save(newagent);
		this.agentService.flush();

	}
	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a agent.
	 * 
	 * This test checks that a not registered agent cannot register himself in the system,without a valid email
	 * 
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewagentWithInvalidEmail() {
		Agent newagent;

		final Date birthDate = new Date();

		newagent = this.agentService.create();

		newagent.setName("Fernando");
		newagent.setSurname("Gutiérrez López");
		newagent.setBirthDate(birthDate);
		newagent.setEmail("ferguti90");//Email not valid(don´t follow the pattern of a email )
		newagent.setPhoneNumber("606587789");
		newagent.setPostalAddress("Calle Nervion 9");

		this.agentService.save(newagent);
		this.agentService.flush();

	}

	/**
	 * 4.1 An actor who is not authenticated must be able to: Register to the system as a agent.
	 * 
	 * 
	 * This test checks that a not registered agent cannot register himself in the system,without a valid birth date
	 * 
	 * @author Luis
	 * @throws ParseException
	 * @throws java.text.ParseException
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testRegisterANewagentWithInvalidBirthDate() throws ParseException, java.text.ParseException {
		Agent newagent;
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final Date birthDate = format.parse("21/12/2030");

		newagent = this.agentService.create();

		newagent.setName("Fernando");
		newagent.setSurname("Gutiérrez López");
		newagent.setBirthDate(birthDate);//Birth date not valid(future date)
		newagent.setEmail("ferguti90@gmail.com");
		newagent.setPhoneNumber("606587789");
		newagent.setPostalAddress("Calle Nervion 9");

		this.agentService.save(newagent);
		this.agentService.flush();

	}

	/**
	 * 
	 * This test checks that an agent cannot edit the profile of other agent
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEditProfileOfOtherUser() {
		super.authenticate("Agent1");

		Agent agent;
		Integer userId;

		userId = super.getEntityId("Agent2");
		agent = this.agentService.findOne(userId);

		agent.setPhoneNumber("658877784");
		agent.setEmail("agent2newEmail@gmail.com");
		agent.setPostalAddress("Calle Alfarería 15b");

		this.agentService.save(agent);

		super.unauthenticate();

	}

	/**
	 * 
	 * 
	 * This test checks that unauthenticated agents cannot edit the profile of other agent
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoLoggedUserEditProfileOfOtherUser() {
		super.authenticate(null);

		Agent agent;
		Integer userId;

		userId = super.getEntityId("Agent2");
		agent = this.agentService.findOne(userId);

		agent.setPhoneNumber("658877784");
		agent.setEmail("agent2newEmail@gmail.com");
		agent.setPostalAddress("Calle Alfarería 15b");

		this.agentService.save(agent);

		super.unauthenticate();

	}

}
