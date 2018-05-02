
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import domain.Actor;
import domain.Advertisement;
import domain.Article;
import domain.CreditCard;
import domain.Newspaper;
import domain.User;
import domain.Volume;

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

	@Autowired
	private VolumeService		volumeService;


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
		final boolean taboo;
		final Collection<Volume> volumes;

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof User) {
			taboo = this.actorService.checkSpamWords(newspaper.getTitle() + " " + newspaper.getDescription());
			newspaper.setTaboo(taboo);
		}

		result = this.newspaperRepository.save(newspaper);

		if (newspaper.getId() != 0)
			publisher = this.userService.findUserByNewspaper(result.getId());
		else
			publisher = (User) this.actorService.findActorByPrincipal();

		publisher.getNewspapers().remove(result);
		publisher.getNewspapers().add(result);
		this.userService.save(publisher);

		if (newspaper.getId() != 0) {

			volumes = this.volumeService.findVolumesByNewspaper(newspaper.getId());

			for (final Volume volume : volumes) {
				volume.getNewspapers().remove(newspaper);
				volume.getNewspapers().add(result);
				this.volumeService.save(volume);
			}
		}

		return result;

	}
	public void delete(final Newspaper newspaper) {

		Assert.notNull(newspaper);
		Assert.isTrue(newspaper.getId() != 0);
		User publisher;
		Actor actor;
		Collection<Volume> volumes;

		actor = this.actorService.findActorByPrincipal();
		if (actor instanceof User)
			Assert.isTrue(newspaper.getPublicationDate() == null);
		Assert.isTrue(this.newspaperRepository.exists(newspaper.getId()));

		publisher = this.userService.findUserByNewspaper(newspaper.getId());

		publisher.getNewspapers().remove(newspaper);
		this.userService.save(publisher);

		volumes = this.volumeService.findVolumesByNewspaper(newspaper.getId());

		for (final Volume volume : volumes) {
			volume.getNewspapers().remove(newspaper);
			this.volumeService.save(volume);
		}

		this.newspaperRepository.delete(newspaper);

	}

	public Newspaper findNewspaperByArticle(final int articleId) {
		Newspaper result;
		Assert.isTrue(articleId != 0);

		result = this.newspaperRepository.findNewspaperByArticle(articleId);

		return result;
	}

	public Page<Newspaper> findSubscribeNewspapers(final int customerId, final Pageable pageable) {
		Page<Newspaper> result;

		Assert.isTrue(customerId != 0);
		Assert.notNull(pageable);

		result = this.newspaperRepository.findSubscribeNewspapers(customerId, pageable);

		return result;
	}

	public Page<Article> findArticlesByNewspaper(final int newspaperId, final Pageable pageable) {
		Assert.isTrue(newspaperId != 0);
		Assert.notNull(pageable);

		Page<Article> result;

		result = this.newspaperRepository.findArticlesByNewspaper(newspaperId, pageable);

		return result;
	}

	public Page<Newspaper> findPublicPublicatedNewspapers(final Pageable pageable) {
		Page<Newspaper> result;

		Assert.notNull(pageable);

		result = this.newspaperRepository.findPublicPublicatedNewspapers(pageable);

		return result;
	}

	public Page<Newspaper> findSubscribedNewspapersByUser(final int customerId, final Pageable pageable) {
		Page<Newspaper> result;
		Assert.isTrue(customerId != 0);
		Assert.notNull(pageable);

		result = this.newspaperRepository.findNotSubscribedNewspapersByCustomer(customerId, pageable);

		return result;
	}

	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult binding) {
		Newspaper result;
		Collection<Article> articles;
		final Collection<CreditCard> creditCards;
		final Collection<Advertisement> advertisements;

		if (newspaper.getId() == 0) {

			result = newspaper;
			articles = new HashSet<>();
			creditCards = new HashSet<>();
			advertisements = new HashSet<>();

			result.setArticles(articles);
			result.setAdvertisements(advertisements);
			result.setCreditCards(creditCards);
			result.setTaboo(false);

		} else {
			result = this.newspaperRepository.findOne(newspaper.getId());

			result.setDescription(newspaper.getDescription());
			result.setPictureUrl(newspaper.getPictureUrl());
			result.setPublicNewspaper(newspaper.getPublicNewspaper());
			result.setTitle(newspaper.getTitle());
		}

		this.validator.validate(result, binding);
		this.newspaperRepository.flush();

		return result;
	}

	public Collection<Newspaper> getAllTabooNewspapers() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.getAllTabooNewspapers();

		return result;
	}

	public Page<Newspaper> getAllTabooNewspapers(final Pageable pageable) {
		Page<Newspaper> result;

		result = this.newspaperRepository.getAllTabooNewspapers(pageable);

		return result;
	}

	//Dashboard queries ------------------------
	/**
	 * Level C query 3
	 * 
	 * @return The average and the standard deviation of articles per newspaper.
	 * @author Antonio
	 */
	public String getArticlesInfoFromNewspapers() {
		String result;

		result = this.newspaperRepository.getArticlesInfoFromNewspapers();

		Assert.notNull(result);

		return result;
	}

	/**
	 * Level C query 4
	 * 
	 * @return The newspapers that have at least 10% more articles than the average.
	 * @author Antonio
	 */
	public Collection<Newspaper> getNewspaperWith10PercentMoreArticlesThanAverage() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.getNewspaperWith10PercentMoreArticlesThanAverage();

		return result;
	}

	/**
	 * Level C query 5
	 * 
	 * @return The newspapers that have at least 10% fewer articles than the average.
	 * @author Antonio
	 */
	public Collection<Newspaper> getNewspaperWith10PercentLessArticlesThanAverage() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.getNewspaperWith10PercentLessArticlesThanAverage();

		return result;
	}

	/**
	 * Level A query 1
	 * 
	 * @return The newspapers that have at least 10% fewer articles than the average.
	 * @author Antonio
	 */
	public String getRatioPublicNewspapers() {
		String result;

		result = this.newspaperRepository.getRatioPublicNewspapers();

		return result;
	}

	/**
	 * Level A query 2
	 * 
	 * @return The average number of articles per private newspapers.
	 * @author Antonio
	 */
	public String getAverageArticlesPerPrivateNewspapers() {
		String result;

		result = this.newspaperRepository.getAverageArticlesPerPrivateNewspapers();

		return result;
	}

	/**
	 * Level A query 3
	 * 
	 * @return The average number of articles per public newspapers.
	 * @author Antonio
	 */
	public String getAverageArticlesPerPublicNewspapers() {
		String result;

		result = this.newspaperRepository.getAverageArticlesPerPublicNewspapers();

		return result;
	}

	/**
	 * Level A query 5
	 * 
	 * @return The average ratio of private versus public newspapers per publisher
	 * @author Antonio
	 */
	public String getAverageRatioPrivateVSPublicNewspaperPublisher() {
		String result;

		result = this.newspaperRepository.getAverageRatioPrivateVSPublicNewspaperPublisher();

		return result;
	}

	/**
	 * Method to search a specific newspaper
	 * 
	 * @author Daniel Diment
	 * @param pageable
	 * @param search
	 * @return
	 */
	public Page<Newspaper> findPublicPublicatedNewspapersWithSearch(final Pageable pageable, final String search) {
		Page<Newspaper> result;

		Assert.notNull(pageable);

		result = this.newspaperRepository.findPublicPublicatedNewspapersWithSearch("%" + search + "%", pageable);

		return result;
	}

	public Page<Newspaper> findNewspapersByVolume(final Integer volumeId, final Pageable pageable) {
		Page<Newspaper> result;

		Assert.notNull(pageable);

		result = this.newspaperRepository.findNewspapersByVolume(volumeId, pageable);

		return result;
	}

	public Collection<Newspaper> findNewspapersByVolume(final Integer volumeId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspapersByVolume(volumeId);

		return result;
	}

	public String getRatioNewspapersAtLeastOneAdvertisementVsNoOne() {

		String result;

		result = this.newspaperRepository.getRatioNewspapersAtLeastOneAdvertisementVsNoOne();

		return result;
	}

	public Collection<Newspaper> findNewspaperByAdvertisement(final int advertisementId) {
		Collection<Newspaper> result;
		result = this.newspaperRepository.findNewspaperByAdvertisement(advertisementId);
		return result;
	}

	public Page<Newspaper> findNewspapersWithAdvertisements(final int advertisementId, final boolean hasAdvertisement, final Pageable pageable) {
		Page<Newspaper> result;
		if (hasAdvertisement)
			result = this.newspaperRepository.findNewspaperByAdvertisementPage(advertisementId, pageable);
		else
			result = this.newspaperRepository.findNewspaperByNoAdvertisementPage(advertisementId, pageable);
		return result;
	}

}
