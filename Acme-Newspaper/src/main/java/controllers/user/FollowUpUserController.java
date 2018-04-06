/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ActorService;
import services.ConfigurationService;
import services.FollowUpService;
import domain.Configuration;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping("/followUp/user")
public class FollowUpUserController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private FollowUpService			followUpService;


	// Constructors -----------------------------------------------------------

	public FollowUpUserController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list-created")
	public ModelAndView listCreated(@RequestParam(defaultValue = "0") final int page) {
		final ModelAndView result;
		final Page<FollowUp> followUps;
		final Pageable pageable;
		final Configuration configuration;
		Collection<FollowUp> ownFollowUps;
		final User creator;

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		ownFollowUps = new ArrayList<>();
		result = new ModelAndView("followUps/list");
		creator = (User) this.actorService.findActorByPrincipal();

		followUps = this.followUpService.findCreatedFollowUps(creator.getId(), pageable);
		ownFollowUps = followUps.getContent();

		result.addObject("ownFollowUps", ownFollowUps);
		result.addObject("page", page);
		result.addObject("pageNum", followUps.getTotalPages());
		result.addObject("requestUri", "followUp/user/list-created.do?");

		return result;
	}

}
