
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.ReviewService;
import controllers.AbstractController;
import domain.Actor;
import domain.Admin;
import domain.Configuration;
import domain.Newspaper;
import domain.Review;

@Controller
@RequestMapping("/review/admin")
public class ReviewAdminController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private ReviewService			reviewService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ConfigurationService	configurationService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reviewId) {
		ModelAndView result;
		Review review;

		try {
			review = this.reviewService.findOne(reviewId);
			this.reviewService.delete(review);

			result = new ModelAndView("redirect:listCreated.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// List my own reviews ---------------------------------------------------------

	@RequestMapping(value = "/listCreated", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		final Page<Review> reviews;
		Configuration configuration;
		Pageable pageable;
		Admin admin;

		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			admin = (Admin) this.actorService.findActorByPrincipal();
			reviews = this.reviewService.findReviewsByAdmin(admin.getId(), pageable);
			result = new ModelAndView("review/listCreated");

			result.addObject("reviews", reviews.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", reviews.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam("newspaperId") final Integer newspaperId) {
		ModelAndView result;
		Review review;
		Newspaper newspaper;

		try {
			review = this.reviewService.create();
			newspaper = this.newspaperService.findOne(newspaperId);
			result = this.createEditModelAndView(review);
			result.addObject("newspaperId", newspaperId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAsDraf")
	public ModelAndView saveAsDraf(Review review, final BindingResult binding, @RequestParam("newspaperId") final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		try {
			if (review.getNewspaper() == null) {
				newspaper = this.newspaperService.findOne(newspaperId);
				review.setNewspaper(newspaper);
			}
			review = this.reviewService.reconstruct(review, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(review, "review.params.error");
			result.addObject("newspaperId", review.getNewspaper().getId());
		} else
			try {
				this.reviewService.saveAsDraf(review);
				result = new ModelAndView("redirect:listCreated.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(review, "review.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAsFinal")
	public ModelAndView saveAsFinal(Review review, final BindingResult binding, @RequestParam("newspaperId") final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		try {
			if (review.getNewspaper() == null) {
				newspaper = this.newspaperService.findOne(newspaperId);
				review.setNewspaper(newspaper);
			}
			review = this.reviewService.reconstruct(review, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(review, "review.params.error");
			result.addObject("newspaperId", review.getNewspaper().getId());
		}

		else
			try {
				this.reviewService.saveAsFinal(review);
				result = new ModelAndView("redirect:listCreated.do.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(review, "review.commit.error");
			}

		return result;
	}

	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("reviewId") final Integer reviewId, @RequestParam("newspaperId") final Integer newspaperId) {
		ModelAndView result;
		Review review;
		Actor actor;
		Integer newspId;

		try {
			actor = this.actorService.findActorByPrincipal();
			review = this.reviewService.findOne(reviewId);
			Assert.isTrue(actor.equals(review.getAdmin()));
			Assert.isTrue(review.isDraf());
			result = this.createEditModelAndView(review);
			if (newspaperId == null) {
				newspId = review.getNewspaper().getId();
				result.addObject("newspaperId", newspId);
			} else
				result.addObject("newspaperId", newspaperId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Review review) {
		ModelAndView result;

		result = this.createEditModelAndView(review, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Review review, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("review/edit");
		result.addObject("review", review);

		result.addObject("message", messageCode);

		return result;

	}

}
