
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FollowUpRepository;
import domain.Actor;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Service
@Transactional
public class FollowUpService {

	// Managed repository --------------------------------------------------

	@Autowired
	private FollowUpRepository	followUpRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	public ActorService			actorService;

	@Autowired
	public ArticleService		articleService;

	@Autowired
	public UserService			userService;

	@Autowired
	public NewspaperService		newsPaperService;


	// Simple CRUD methods --------------------------------------------------

	/**
	 * 
	 * 
	 * @author Luis
	 **/
	public FollowUp create() {
		FollowUp result;

		result = new FollowUp();

		return result;
	}

	/**
	 * 
	 * @author Luis
	 **/
	public Collection<FollowUp> findAll() {

		Collection<FollowUp> result;

		Assert.notNull(this.followUpRepository);
		result = this.followUpRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	/**
	 * 
	 * @author Luis
	 **/
	public FollowUp findOne(final int followUpId) {

		FollowUp result;

		result = this.followUpRepository.findOne(followUpId);

		return result;

	}

	/**
	 * 
	 * @author Luis
	 **/
	public FollowUp save(final FollowUp followUp) {
		assert followUp != null;
		Assert.isTrue(this.articleService.getArticleByFollowUp(followUp).getFinalMode());//Comprueba que el article esta guardado en final mode
		Assert.isTrue(this.newsPaperService.findNewspaperByArticle(this.articleService.getArticleByFollowUp(followUp).getId()).getPublicationDate().before(new Date()));//Comprueba que el periódico  ha sido publicado

		FollowUp result;
		Article article;
		Article savedArticle;
		User user;

		result = this.followUpRepository.save(followUp);
		article = this.articleService.getArticleByFollowUp(followUp);
		user = (User) this.actorService.findActorByPrincipal();

		if (followUp.getId() != 0) {
			Assert.isTrue(user == this.findFollowUpCreator(followUp));
			article.getFollowUps().remove(followUp);
		}

		article.getFollowUps().add(followUp);
		savedArticle = this.articleService.save(article);
		user.getArticles().remove(article);
		user.getArticles().add(savedArticle);
		this.userService.save(user);

		return result;

	}
	/**
	 * 
	 * @author Luis
	 **/
	public void delete(final FollowUp followUp) {
		assert followUp != null;
		assert followUp.getId() != 0;
		Assert.isTrue(this.followUpRepository.exists(followUp.getId()));
		final Actor principal = this.actorService.findActorByPrincipal();
		;

		if (principal instanceof User)
			Assert.isTrue(principal == this.findFollowUpCreator(followUp) && followUp.getPublicationDate().after(new Date()));

		Article article;
		final Article savedArticle;
		User user;
		article = this.articleService.getArticleByFollowUp(followUp);
		user = this.findFollowUpCreator(followUp);

		article.getFollowUps().remove(followUp);
		savedArticle = this.articleService.save(article);
		user.getArticles().remove(article);
		user.getArticles().add(savedArticle);
		this.userService.save(user);

		this.followUpRepository.delete(followUp);

	}
	/**
	 * Returns the creator of a followUp
	 * 
	 * @author Luis
	 **/
	private User findFollowUpCreator(final FollowUp followUp) {
		User result;
		result = (this.userService.findUserByArticle(this.articleService.getArticleByFollowUp(followUp).getId()));
		return result;
	}
}
