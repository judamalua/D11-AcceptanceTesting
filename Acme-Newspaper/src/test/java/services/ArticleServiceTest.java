
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

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

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

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);
		Assert.isTrue(savedNewspaper.isTaboo());

		super.unauthenticate();
	}

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

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	@Test(expected = java.lang.NullPointerException.class)
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

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

	}

	@Test
	public void testEditAnArticlePositive() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");
		final int articleId = super.getEntityId("Article1");

		super.authenticate("User1");
		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();
	}

	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void testEditAnArticleNotLoggedNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper2");
		final int articleId = super.getEntityId("Article1");

		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

	}

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

	@Test(expected = IllegalAccessException.class)
	public void testEditAArticleWithPublicatedNewspaperNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;
		final int newspaperId = super.getEntityId("Newspaper1");
		final int articleId = super.getEntityId("Article1");

		super.authenticate("User1");
		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();

	}

	@Test(expected = IllegalAccessException.class)
	public void testEditANotFinalArticleNegative() {
		Article article;
		Article savedNewspaper;
		Newspaper newspaper;

		final int newspaperId = super.getEntityId("Newspaper1");
		final int articleId = super.getEntityId("Article2");

		super.authenticate("User1");
		article = this.articleService.findOne(articleId);
		newspaper = this.newspaperService.findOne(newspaperId);

		article.setTitle("Title");

		this.articleService.save(article, newspaper);
		savedNewspaper = this.articleService.findOne(article.getId());
		Assert.notNull(savedNewspaper);

		super.unauthenticate();

	}

}
