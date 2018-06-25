
package services;

import java.util.Date;
import java.util.HashSet;

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
import domain.Article;
import domain.Configuration;
import domain.CreditCard;
import domain.Lusit;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NewspaperServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;

	@Autowired
	public UserService			userService;

	@Autowired
	public NewspaperService		newspaperService;

	@Autowired
	public ConfigurationService	configurationService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test
	public void testCreateANewspaper() {
		Newspaper newspaper;
		Newspaper savedNewspaper;

		super.authenticate("User1");
		newspaper = this.newspaperService.create();

		newspaper.setTitle("Title");
		newspaper.setArticles(new HashSet<Article>());
		newspaper.setCreditCards(new HashSet<CreditCard>());
		newspaper.setDescription("New description");
		newspaper.setPictureUrl("");
		newspaper.setAdvertisements(new HashSet<Advertisement>());
		newspaper.setLusits(new HashSet<Lusit>());
		newspaper.setTaboo(false);

		newspaper = this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test
	public void testCreateATabooNewspaperPositive() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		super.authenticate("User1");
		newspaper = this.newspaperService.create();

		newspaper.setTitle("sex");
		newspaper.setArticles(new HashSet<Article>());
		newspaper.setCreditCards(new HashSet<CreditCard>());
		newspaper.setDescription("New description");
		newspaper.setPictureUrl("");
		newspaper.setAdvertisements(new HashSet<Advertisement>());
		newspaper.setLusits(new HashSet<Lusit>());

		newspaper = this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);
		Assert.isTrue(savedNewspaper.isTaboo());

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testCreateANewspaperNegative() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		super.authenticate("User1");
		newspaper = this.newspaperService.create();

		newspaper.setTitle("");
		newspaper.setArticles(new HashSet<Article>());
		newspaper.setCreditCards(new HashSet<CreditCard>());
		newspaper.setDescription("");
		newspaper.setPictureUrl("");
		newspaper.setTaboo(false);
		newspaper.setAdvertisements(new HashSet<Advertisement>());

		this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateANewspaperNotLoggedNegative() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		super.unauthenticate();
		newspaper = this.newspaperService.create();

		newspaper.setTitle("New");
		newspaper.setArticles(new HashSet<Article>());
		newspaper.setCreditCards(new HashSet<CreditCard>());
		newspaper.setDescription("New");
		newspaper.setPictureUrl("");
		newspaper.setTaboo(false);
		newspaper.setAdvertisements(new HashSet<Advertisement>());

		newspaper = this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test
	public void testEditANewspaperPositive() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		int newspaperId;
		super.authenticate("User1");

		newspaperId = super.getEntityId("Newspaper2");

		newspaper = this.newspaperService.findOne(newspaperId);

		newspaper.setTitle("New title");

		this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);
		Assert.isTrue(savedNewspaper.getTitle().equals("New title"));

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEditANewspaperNotLoggedNegative() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		int newspaperId;

		newspaperId = super.getEntityId("Newspaper2");

		newspaper = this.newspaperService.findOne(newspaperId);

		newspaper.setTitle("New");

		newspaper = this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * Create a newspaper. A user who has created a newspaper is commonly referred to a publisher.
	 * 
	 * @author MJ
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testEditANewspaperNegative() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		int newspaperId;
		super.authenticate("User1");

		newspaperId = super.getEntityId("Newspaper2");

		newspaper = this.newspaperService.findOne(newspaperId);

		newspaper.setTitle("");

		this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();

	}

	/**
	 * Functional requirement number 4: An actor who is not authenticated must be able to:
	 * List the newspapers that are published and browse their articles.
	 * 
	 * @author MJ
	 */
	@Test
	public void listPublicatedNotLoggedNewspapersPositive() {
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;

		configuration = this.configurationService.findConfiguration();

		pageable = new PageRequest(0, configuration.getPageSize());

		newspapers = this.newspaperService.findPublicPublicatedNewspapers(pageable);

		Assert.isTrue(newspapers.getContent().size() == 5);

	}

	/**
	 * Functional requirement number 4: An actor who is not authenticated must be able to:
	 * List the newspapers that are published and browse their articles.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void listNotPublicatedNotLoggedNewspapersNegative() {
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;
		final int userId = super.getEntityId("User1");

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getPageSize());

		newspapers = this.userService.findNotPublishedNewspapersByUser(userId, pageable);

		Assert.isTrue(newspapers.getContent().size() == 5);

	}

	/**
	 * Functional requirement number 4: An actor who is not authenticated must be able to:
	 * List the newspapers that are published and browse their articles.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void listSubscribedNotLoggedNewspapersNegative() {
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;
		final int customerId = super.getEntityId("Customer1");

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getPageSize());

		newspapers = this.newspaperService.findSubscribedNewspapersByUser(customerId, pageable);

		Assert.isTrue(newspapers.getContent().size() == 5);

	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Publish a newspaper that he or she's created. Note that no newspapers can be published until each
	 * of their articles of which is composed is saved in final mode.
	 * 
	 * @author MJ
	 */
	@Test
	public void testPublishNewspaperPositive() {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		int newspaperId;
		super.authenticate("User1");

		newspaperId = super.getEntityId("Newspaper2");

		newspaper = this.newspaperService.findOne(newspaperId);

		newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1));

		this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();

	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Publish a newspaper that he or she's created. Note that no newspapers can be published until each
	 * of their articles of which is composed is saved in final mode.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPublishNewspaperNotLogged() {
		Newspaper newspaper, savedNewspaper;
		int newspaperId;

		newspaperId = super.getEntityId("Newspaper2");

		newspaper = this.newspaperService.findOne(newspaperId);

		newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1));

		newspaper = this.newspaperService.save(newspaper);
		savedNewspaper = this.newspaperService.findOne(newspaper.getId());
		Assert.notNull(savedNewspaper);

	}
}
