
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
import domain.Article;
import domain.FollowUp;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FollowUPServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public UserService			userService;
	@Autowired
	public FollowUpService		followUpService;
	@Autowired
	public NewspaperService		newspaperService;
	@Autowired
	public ArticleService		articleService;
	@Autowired
	public ConfigurationService	configurationService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * Information requirement 14.
	 * The writer of an article may write follow-ups on it. Follow-ups can be written only after an
	 * article is saved in final mode and the corresponding newspaper is published. For every follow-up,
	 * the system must store the following data: title, publication moment, summary, text
	 * and optional pictures.
	 * 
	 * That method test than a user can create a followUp correctly, according to the constraints
	 * 
	 * @author Luis
	 */
	@Test
	public void testCreateAfollowUp() {
		FollowUp followUp;
		FollowUp savedFollowUp;
		Article article;

		super.authenticate("User1");
		followUp = this.createStandarFollowUp();
		article = (Article) this.articleService.findAll().toArray()[0];

		followUp = this.followUpService.save(followUp, article);
		savedFollowUp = this.followUpService.findOne(followUp.getId());
		Assert.notNull(savedFollowUp);

		super.unauthenticate();
	}

	//******************************************Negative Methods*******************************************************************

	/**
	 * Information requirement 14.
	 * The writer of an article may write follow-ups on it. Follow-ups can be written only after an
	 * article is saved in final mode and the corresponding newspaper is published. For every follow-up,
	 * the system must store the following data: title, publication moment, summary, text
	 * and optional pictures.
	 * 
	 * 
	 * That method test that a User can created a followUp in an article of a not published newspaper
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAfollowUpNegative() {
		FollowUp followUp;
		FollowUp savedFollowUp;
		Article article;

		super.authenticate("User2");
		followUp = this.createStandarFollowUp();
		article = (Article) this.articleService.findAll().toArray()[10];

		followUp = this.followUpService.save(followUp, article);
		savedFollowUp = this.followUpService.findOne(followUp.getId());
		Assert.notNull(savedFollowUp);

		super.unauthenticate();
	}
	/**
	 * Information requirement 14.
	 * The writer of an article may write follow-ups on it. Follow-ups can be written only after an
	 * article is saved in final mode and the corresponding newspaper is published. For every follow-up,
	 * the system must store the following data: title, publication moment, summary, text
	 * and optional pictures.
	 * 
	 * That method test that a not authenticated actor can create a followUp
	 * 
	 * @author Luis
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAfollowUpNegative2() {
		FollowUp followUp;
		FollowUp savedFollowUp;
		Article article;

		followUp = this.createStandarFollowUp();
		followUp.setUser(null);
		article = (Article) this.articleService.findAll().toArray()[10];

		followUp = this.followUpService.save(followUp, article);
		savedFollowUp = this.followUpService.findOne(followUp.getId());
		Assert.notNull(savedFollowUp);

	}

	//******************************************Private Methods**************************

	private FollowUp createStandarFollowUp() {
		super.authenticate("User1");
		FollowUp followUp;
		final User creator = (User) this.actorService.findActorByPrincipal();

		followUp = this.followUpService.create();
		followUp.setText("That is a example of a simple text of a followUp");
		followUp.setTitle("FollowUp 1");
		followUp.setSummary("Hola soy un summary");
		followUp.setPublicationDate(new Date());
		followUp.setUser(creator);

		return followUp;

	}

}
