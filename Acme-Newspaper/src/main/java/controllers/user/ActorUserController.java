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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.Configuration;
import domain.User;
import forms.UserCustomerAdminForm;

@Controller
@RequestMapping("/actor/user")
public class ActorUserController extends AbstractController {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private UserService				userService;
	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ActorUserController() {
		super();
	}

	//Edit an User
	/**
	 * That method edits the profile of a user
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editUser() {
		ModelAndView result;
		User user;
		UserCustomerAdminForm actorForm;

		user = (User) this.actorService.findActorByPrincipal();
		Assert.notNull(user);
		actorForm = this.actorService.deconstruct(user);

		result = this.createEditModelAndView(actorForm);

		return result;
	}

	// Follow or unfollow users ---------------------------------------------------------

	/**
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView followOrUnfollow(@RequestParam final int userId, @RequestParam(defaultValue = "false") final boolean followedView, @RequestParam(defaultValue = "false") final boolean followersView) {
		ModelAndView result;
		User user;

		try {
			user = this.userService.findOne(userId);

			this.userService.followOrUnfollowUser(user);

			// If we come here since the standard list of users, we are redirected there
			result = new ModelAndView("redirect:/user/list.do");

			// If we come here since the list of the followed users, we are redirected there
			if (followedView && !followersView)
				result = new ModelAndView("redirect:list-followed.do");
			else if (!followedView && followersView)
				// If we come here since the list of the followers, we are redirected there
				result = new ModelAndView("redirect:list-followers.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Listing followers ---------------------------------------------------------
	/**
	 * This method returns a model and view with list of the followers of the principal
	 * 
	 * @param page
	 * 
	 * @return ModelandView
	 * @author Juanmi
	 */
	@RequestMapping("/list-followers")
	public ModelAndView listFollowers(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<User> users;
		Pageable pageable;
		Configuration configuration;
		User principal;

		try {

			principal = (User) this.actorService.findActorByPrincipal();

			result = new ModelAndView("user/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			users = this.userService.findFollowers(principal.getId(), pageable);

			result.addObject("users", users.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", users.getTotalPages());
			result.addObject("requestURI", "actor/user/list-followers.do");
			result.addObject("principal", principal);
			result.addObject("followersView", true);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Listing followed users ---------------------------------------------------------
	/**
	 * This method returns a model and view with list of the followed users of the principal
	 * 
	 * @param page
	 * 
	 * @return ModelandView
	 * @author Juanmi
	 */
	@RequestMapping("/list-followed")
	public ModelAndView listFollowed(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<User> users;
		Pageable pageable;
		Configuration configuration;
		User principal;

		try {

			principal = (User) this.actorService.findActorByPrincipal();

			result = new ModelAndView("user/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			users = this.userService.findFollowedUsers(principal.getId(), pageable);

			result.addObject("users", users.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", users.getTotalPages());
			result.addObject("requestURI", "actor/user/list-followed.do");
			result.addObject("principal", principal);
			result.addObject("followedView", true);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Listing chirps of followed users ---------------------------------------------------------
	/**
	 * This method returns a model and view with list of the followed users of the principal
	 * 
	 * @param page
	 * 
	 * @return ModelandView
	 * @author Juanmi
	 */
	@RequestMapping("/list-chirps-followed")
	public ModelAndView listChirpsFollowed(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Chirp> chirps;
		Pageable pageable;
		Configuration configuration;
		User principal;

		try {

			principal = (User) this.actorService.findActorByPrincipal();

			result = new ModelAndView("chirp/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			chirps = this.userService.findFollowedUsersChirps(principal.getId(), pageable);

			result.addObject("users", chirps.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", chirps.getTotalPages());
			result.addObject("requestURI", "user/list-chirps-followed.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	//Updating profile of a user ---------------------------------------------------------------------
	/**
	 * That method update the profile of a user.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateUser(@ModelAttribute("actor") final UserCustomerAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		User user = null;

		try {
			user = this.userService.reconstruct(actor, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(actor, "user.params.error");
		else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/user/display.do?anonymous=false");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actor, "user.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final UserCustomerAdminForm user) {
		ModelAndView result;

		result = this.createEditModelAndView(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserCustomerAdminForm user, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("message", messageCode);
		result.addObject("actor", user);

		return result;

	}

}
