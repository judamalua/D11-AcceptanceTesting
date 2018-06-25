
package services;

import java.util.Collection;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewRepository;
import domain.Actor;
import domain.Admin;
import domain.Review;

@Service
@Transactional
public class ReviewService {

	// Managed repository --------------------------------------------------
	@Autowired
	private ReviewRepository	reviewRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods --------------------------------------------------

	public Review create() {
		Review result;
		Admin admin;

		admin = (Admin) this.actorService.findActorByPrincipal();
		result = new Review();
		result.setDraf(true);
		result.setAdmin(admin);

		return result;
	}

	public Collection<Review> findAll() {
		Collection<Review> result;

		result = this.reviewRepository.findAll();

		return result;
	}

	public Review findOne(final int reviewId) {
		Review result;

		result = this.reviewRepository.findOne(reviewId);

		return result;
	}

	public Review saveAsDraf(final Review review) {
		Assert.notNull(review);

		Actor principal;
		Review result;

		principal = this.actorService.findActorByPrincipal();
		if (principal instanceof Admin) {
			Assert.isTrue(principal.equals(review.getAdmin()));
		}
		result = this.reviewRepository.save(review);

		return result;
	}

	public Review saveAsFinal(final Review review) {
		Assert.notNull(review);

		Actor principal;
		Review result;

		principal = this.actorService.findActorByPrincipal();
		if (principal instanceof Admin) {
			Assert.isTrue(principal.equals(review.getAdmin()));
		}
		review.setDraf(false);
		result = this.reviewRepository.save(review);

		return result;
	}

	public void delete(final Review review) {
		Assert.notNull(review);
		Assert.isTrue(review.getId() != 0);

		Admin admin;
		Review savedReview;

		savedReview = this.findOne(review.getId());
		admin = (Admin) this.actorService.findActorByPrincipal();

		Assert.isTrue(savedReview.getAdmin().equals(admin));

		this.reviewRepository.delete(review);
	}

	public Review reconstruct(final Review review, final BindingResult binding) {
		Review result;
		Admin admin;

		if (review.getId() == 0) {
			admin = (Admin) this.actorService.findActorByPrincipal();
			result = review;
			result.setAdmin(admin);
			result.setTicker(this.generateTicker());

		} else {
			result = this.findOne(review.getId());
			result.setDescription(review.getDescription());
			result.setTitle(review.getTitle());
			result.setGauge(review.getGauge());

		}

		this.validator.validate(result, binding);
		this.reviewRepository.flush();

		return result;
	}

	public Collection<Review> findReviewsByAdmin(final int adminId) {
		Collection<Review> result;

		result = this.reviewRepository.getReviewsWrotenByAdmin(adminId);

		return result;
	}

	public Collection<Review> findReviewsByNewspaper(final int newspaperId) {
		Collection<Review> result;

		result = this.reviewRepository.getReviewsByNewspaper(newspaperId);

		return result;

	}

	public Page<Review> findReviewsByAdmin(final int adminId, final Pageable pageable) {
		Page<Review> result;

		result = this.reviewRepository.findReviewsWrotenByAdmin(adminId, pageable);

		return result;
	}

	public Page<Review> findReviewsByNewspaper(final int newspaperId, final Pageable pageable) {
		Page<Review> result;

		result = this.reviewRepository.findReviewsByNewspaper(newspaperId, pageable);

		return result;

	}

	public void flush() {
		this.reviewRepository.flush();
	}

	private String generateTicker() {
		String res = "";
		final int year, month, day;
		final LocalDate currentDate;
		Random random;
		final String alphabet;

		currentDate = new LocalDate();
		year = currentDate.getYear() % 100;
		month = currentDate.getMonthOfYear();
		day = currentDate.getDayOfMonth();
		random = new Random();
		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

		for (int i = 0; i < 4; i++) {
			res += alphabet.charAt(random.nextInt(alphabet.length()));
		}

		res += "_";
		res = (year < 10 ? "0" + year : year) + "-";
		res += (month < 10 ? "0" + month : month) + "-";
		res += (day < 10 ? "0" + day : day);

		if (this.getAllTickers().contains(res)) {
			res = this.generateTicker();
		}

		return res;
	}

	private Collection<String> getAllTickers() {
		Collection<String> reviewTickers;

		reviewTickers = this.getReviewTickers();

		return reviewTickers;

	}

	public Collection<String> getReviewTickers() {
		return this.reviewRepository.getReviewTickers();
	}

}
