
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import domain.Article;
import domain.CreditCard;
import domain.Newspaper;
import domain.User;

@Service
@Transactional
public class NewspaperService {

	// Managed repository --------------------------------------------------

	@Autowired
	private NewspaperRepository	newspaperRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private Validator			validator;

	@Autowired
	private UserService			userService;

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods --------------------------------------------------

	public Newspaper create() {
		Newspaper result;

		result = new Newspaper();

		return result;
	}

	public Collection<Newspaper> findAll() {

		Collection<Newspaper> result;

		Assert.notNull(this.newspaperRepository);
		result = this.newspaperRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Newspaper findOne(final int newspaperId) {

		Newspaper result;

		result = this.newspaperRepository.findOne(newspaperId);

		return result;

	}

	public Newspaper save(final Newspaper newspaper) {

		Assert.notNull(newspaper);

		Newspaper result;
		User publisher;

		result = this.newspaperRepository.save(newspaper);
		publisher = this.userService.findUserByNewspaper(newspaper.getId());

		publisher.getNewspapers().remove(result);
		publisher.getNewspapers().add(result);
		this.userService.save(publisher);

		return result;

	}
	public void delete(final Newspaper newspaper) {

		Assert.notNull(newspaper);
		Assert.isTrue(newspaper.getId() != 0);
		User publisher;

		Assert.isTrue(this.newspaperRepository.exists(newspaper.getId()));

		publisher = this.userService.findUserByNewspaper(newspaper.getId());
		publisher.getNewspapers().remove(newspaper);
		this.userService.save(publisher);

		this.newspaperRepository.delete(newspaper);

	}

	public Newspaper findNewspaperByArticle(final int articleId) {
		Newspaper result;
		Assert.isTrue(articleId != 0);

		result = this.newspaperRepository.findNewspaperByArticle(articleId);

		return result;
	}

	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		Newspaper result;
		Collection<Article> articles;
		final Collection<CreditCard> creditCards;

		if (newspaper.getId() == 0) {

			result = newspaper;
			articles = new HashSet<>();
			creditCards = new HashSet<>();

			result.setArticles(articles);
			result.setCreditCards(creditCards);

		} else {
			result = this.newspaperRepository.findOne(newspaper.getId());

			result.setDescription(newspaper.getDescription());
			result.setPictureUrl(newspaper.getPictureUrl());
			result.setPublicNewspaper(newspaper.getPublicNewspaper());
			result.setTitle(newspaper.getTitle());
		}
		this.validator.validate(result, binding);

		return result;
	}
}
