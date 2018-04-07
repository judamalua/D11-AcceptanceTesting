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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FollowUpService;
import domain.Actor;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping("/followUp")
public class FollowUpController extends AbstractController {

	@Autowired
	private FollowUpService	followUpService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public FollowUpController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer followUpId) {
		ModelAndView result;
		final FollowUp followUp;
		User creator;
		final Actor principal;

		try {

			result = new ModelAndView("followUp/display");
			followUp = this.followUpService.findOne(followUpId);
			Assert.notNull(followUp);
			creator = followUp.getUser();
			principal = this.actorService.findActorByPrincipal();

			result.addObject("followUp", followUp);
			result.addObject("creator", creator);
			result.addObject("principal", principal);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
