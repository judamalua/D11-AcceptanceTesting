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
import java.util.Date;

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
import services.ArticleService;
import services.ConfigurationService;
import services.FollowUpService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Article;
import domain.Configuration;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/followUp/user")
public class FollowUpUserController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newsPaperService;

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
		result = new ModelAndView("followUp/list");
		creator = (User) this.actorService.findActorByPrincipal();

		followUps = this.followUpService.findCreatedFollowUps(creator.getId(), pageable);
		ownFollowUps = followUps.getContent();

		result.addObject("ownFollowUps", ownFollowUps);
		result.addObject("page", page);
		result.addObject("pageNum", followUps.getTotalPages());
		result.addObject("requestUri", "followUp/user/list-created.do?");

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("followUpId") final Integer followUpId) {
		ModelAndView result;
		FollowUp followUp;
		Actor actor;
		User creator;
		Integer articleId;
		final Newspaper newspaper;
		Article article;

		try {
			actor = this.actorService.findActorByPrincipal();
			followUp = this.followUpService.findOne(followUpId);
			article = this.articleService.getArticleByFollowUp(followUp);
			articleId = article.getId();
			newspaper = this.newsPaperService.findNewspaperByArticle(articleId);
			creator = followUp.getUser();
			Assert.isTrue(actor.equals(creator));
			Assert.isTrue(article.getFinalMode());
			Assert.isTrue(newspaper.getPublicNewspaper());

			result = this.createEditModelAndView(followUp);
			result.addObject("articleId", articleId);
		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}
	//create--------------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final Integer articleId) {
		ModelAndView result;
		FollowUp followUp;

		try {
			this.actorService.checkUserLogin();
			followUp = this.followUpService.create();

			result = this.createEditModelAndView(followUp);
			result.addObject("articleId", articleId);
			result.addObject("requestURI", "followUp/user/edit.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("followUp") FollowUp followUp, final BindingResult binding, @RequestParam("articleId") final Integer articleId, @RequestParam(value = "userId", defaultValue = "0") final Integer userId) {
		ModelAndView result;
		User actor;
		User creator;
		Article article;
		final Newspaper newspaper;
		FollowUp savedFollowUp;
		try {
			if (userId != null && userId != 0)
				followUp.setUser(this.userService.findOne(userId));
			followUp = this.followUpService.reconstruct(followUp, binding);
		} catch (final Throwable oops) {

		}
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(followUp, "followUp.params.error");
			result.addObject("articleId", articleId);

		} else
			try {
				actor = (User) this.actorService.findActorByPrincipal();
				article = this.articleService.findOne(articleId);
				creator = this.userService.findUserByArticle(article.getId());
				newspaper = this.newsPaperService.findNewspaperByArticle(article.getId());
				Assert.isTrue(actor == creator);
				Assert.isTrue(newspaper.getPublicationDate().before(new Date()));
				Assert.isTrue(article.getFinalMode());

				savedFollowUp = this.followUpService.save(followUp, article);
				creator = savedFollowUp.getUser();

				Assert.isTrue(actor.equals(creator));

				result = new ModelAndView("redirect:/article/display.do?articleId=" + article.getId());

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(followUp, "followUp.commit.error");
			}

		return result;
	}
	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("followUp") final FollowUp followUp) {
		ModelAndView result;
		Article article;

		try {
			article = this.articleService.getArticleByFollowUp(followUp);
			this.followUpService.delete(followUp);

			result = new ModelAndView("redirect:/article/display.do?articleId=" + article.getId());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final FollowUp followUp) {
		ModelAndView result;

		result = this.createEditModelAndView(followUp, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FollowUp followUp, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("followUp/edit");
		result.addObject("followUp", followUp);
		result.addObject("message", messageCode);

		return result;

	}

}
