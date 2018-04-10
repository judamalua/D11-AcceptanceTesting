
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

	@Test
	public void testCreateANewspaper() {
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

	//******************************************Private Methods**************************

	private FollowUp createStandarFollowUp() {
		super.authenticate("User1");
		FollowUp followUp;
		final User creator = (User) this.actorService.findActorByPrincipal();

		followUp = this.followUpService.create();
		followUp.setText("That is a example of a simple text of a followUp");
		followUp.setTitle("FollowUp 1");
		followUp.setPublicationDate(new Date());
		followUp.setUser(creator);

		return followUp;

	}

}
