
package services;

import java.util.HashSet;

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
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ArticleServiceTest extends AbstractTest {

	@Autowired
	public ArticleService	articleService;
	@Autowired
	public ActorService		actorService;
	@Autowired
	public UserService		userService;

	@Autowired
	public NewspaperService	newspaperService;


	//******************************************Positive Methods*******************************************************************

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test
	public void testCreateAnArticle() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");

		super.authenticate("User1");
		article = this.articleService.create();
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");
		article.setBody("New body");
		article.setFinalMode(false);
		article.setFollowUps(new HashSet<FollowUp>());
		article.setSummary("New summary");
		article.setTaboo(false);

		article = this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test
	public void testCreateATabooArticlePositive() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");

		super.authenticate("User1");
		article = this.articleService.create();
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("sex");
		article.setBody("New body");
		article.setFinalMode(false);
		article.setFollowUps(new HashSet<FollowUp>());
		article.setSummary("New summary");

		article = this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);
		Assert.isTrue(savedNewspaper.isTaboo());

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testCreateAnArticleNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");

		super.authenticate("User1");
		article = this.articleService.create();
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("");
		article.setBody("");
		article.setFinalMode(false);
		article.setFollowUps(new HashSet<FollowUp>());
		article.setSummary("");
		article.setTaboo(false);

		article = this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAnArticleNotLoggedNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");

		article = this.articleService.create();
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");
		article.setBody("New body");
		article.setFinalMode(false);
		article.setFollowUps(new HashSet<FollowUp>());
		article.setSummary("New summary");
		article.setTaboo(false);

		article = this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test
	public void testEditAnArticlePositive() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");
		final int articleId = super.getEntityId("Article11");

		super.authenticate("User1");
		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		article = this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEditAnArticleNotLoggedNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		super.unauthenticate();
		final int newspaperId = super.getEntityId("Newspaper2");
		final int articleId = super.getEntityId("Article11");

		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

	}

	/**
	 * Functional requirement number 6: An actor who is authenticated as a user must be able to:
	 * 
	 * Write an article and attach it to any newspaper that has not been published, yet.
	 * 
	 * @author MJ
	 */
	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testEditAnArticleNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");
		final int articleId = super.getEntityId("Article1");

		super.authenticate("User1");
		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

}
