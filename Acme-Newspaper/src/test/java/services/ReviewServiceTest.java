
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Admin;
import domain.Newspaper;
import domain.Review;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ReviewServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public AdminService			adminService;
	@Autowired
	public ReviewService		reviewService;
	@Autowired
	public NewspaperService		newspaperService;
	@Autowired
	public ConfigurationService	configurationService;


	//******************************************Positive Methods*******************************************************************

	@Test
	public void testCreateAndSaveReview() {

		// Now we are creating a new Review
		Review review;
		Review savedReview;
		Review findedReview;
		Newspaper newspaper;
		Admin admin;

		super.authenticate("Admin1");
		review = this.reviewService.create();
		admin = (Admin) this.actorService.findActorByPrincipal();
		newspaper = (Newspaper) this.newspaperService.findAll().toArray()[0];
		review.setAdmin(admin);
		review.setGauge(1);
		review.setTitle("title test");
		review.setDescription("description test");
		review.setTicker("TRFE_16-10-18");
		review.setDraf(true);
		review.setNewspaper(newspaper);

		//Here we save it as draft
		savedReview = this.reviewService.saveAsDraf(review);
		findedReview = this.reviewService.findOne(savedReview.getId());
		Assert.notNull(findedReview);

		//Now administrator who created it edit and save it in final mode
		findedReview.setGauge(3);
		savedReview = this.reviewService.saveAsDraf(findedReview);
		Assert.isTrue(savedReview != null && savedReview.getGauge() == 3);

		super.unauthenticate();
	}

	//******************************************Negative Methods*******************************************************************

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSaveAndTryToEditAReviewInFinalMode() {

		// Now we are creating a new Review
		Review review;
		Review savedReview;
		Review findedReview;
		Newspaper newspaper;
		Admin admin;

		super.authenticate("Admin1");
		review = this.reviewService.create();
		admin = (Admin) this.actorService.findActorByPrincipal();
		newspaper = (Newspaper) this.newspaperService.findAll().toArray()[0];
		review.setAdmin(admin);
		review.setGauge(1);
		review.setTitle("title test");
		review.setDescription("description test");
		review.setTicker("TRFE_16-10-18");
		review.setDraf(true);
		review.setNewspaper(newspaper);

		//Here we save it as  final Mode
		savedReview = this.reviewService.saveAsFinal(review);
		findedReview = this.reviewService.findOne(savedReview.getId());
		Assert.notNull(findedReview);

		//Now administrator who created it try to edit it, but can´t cause is in saved in final mode
		findedReview.setGauge(3);
		savedReview = this.reviewService.saveAsFinal(findedReview);
		Assert.isTrue(savedReview != null && savedReview.getGauge() == 3);

		super.unauthenticate();
	}
}
