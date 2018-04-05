
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
	private Validator			validator;


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
			Assert.isTrue(user == followUp.getUser());
			article.getFollowUps().remove(followUp);
		}

		article.getFollowUps().add(followUp);
		savedArticle = this.articleService.save(article);
		//		user.getArticles().remove(article);
		//		user.getArticles().add(savedArticle);
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
			Assert.isTrue(principal == followUp.getUser() && followUp.getPublicationDate().after(new Date()));

		Article article;
		//final Article savedArticle;
		User user;
		article = this.articleService.getArticleByFollowUp(followUp);
		user = followUp.getUser();

		article.getFollowUps().remove(followUp);
		this.articleService.save(article);
		//		user.getArticles().remove(article);
		//		user.getArticles().add(savedArticle);
		this.userService.save(user);

		this.followUpRepository.delete(followUp);

	}

	//Other Busssiness Methods 

	/**
	 * 
	 * @author Luis
	 **/
	public FollowUp reconstruct(final FollowUp followUp, final BindingResult binding) {
		FollowUp result;
		User user;

		if (followUp.getId() == 0) {
			user = (User) this.actorService.findActorByPrincipal();
			followUp.setUser(user);
			result = followUp;
		} else {
			result = this.followUpRepository.findOne(followUp.getId());
			result.setUser(followUp.getUser());
			result.setText(followUp.getText());
			result.setPictureUrls(followUp.getPictureUrls());
			result.setPublicationDate(followUp.getPublicationDate());
			result.setTitle(followUp.getTitle());
			result.setSummary(followUp.getSummary());
		}
		this.validator.validate(result, binding);
		return result;
	}

	/**
	 * 
	 * 
	 * 
	 * @author Luis
	 */
	public void flush() {
		this.followUpRepository.flush();
	}
}
