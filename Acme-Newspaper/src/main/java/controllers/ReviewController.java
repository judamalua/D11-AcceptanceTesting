/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.ReviewService;
import domain.Configuration;
import domain.Newspaper;
import domain.Review;

@Controller
@RequestMapping("/review")
public class ReviewController extends AbstractController {

	@Autowired
	private ReviewService			reviewService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ReviewController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer reviewId) {
		ModelAndView result;
		Review review;

		try {
			result = new ModelAndView("review/display");
			if (reviewId != null) {
				review = this.reviewService.findOne(reviewId);
				result.addObject("review", review);
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = true) final Integer newspaperId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		final Page<Review> reviews;
		Configuration configuration;
		Pageable pageable;
		Newspaper newspaper;

		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			newspaper = this.newspaperService.findOne(newspaperId);
			reviews = this.reviewService.findReviewsByNewspaper(newspaperId, pageable);
			result = new ModelAndView("review/list");

			result.addObject("reviews", reviews.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", reviews.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
