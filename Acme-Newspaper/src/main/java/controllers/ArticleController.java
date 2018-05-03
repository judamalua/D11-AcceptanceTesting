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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ArticleService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import domain.Actor;
import domain.Admin;
import domain.Article;
import domain.Configuration;
import domain.CreditCard;
import domain.Customer;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ArticleController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer articleId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Article article;
		final User writer;
		Page<FollowUp> followUps;
		final Pageable pageable;
		Newspaper newspaper;
		final Configuration configuration;
		Actor actor = null;
		boolean validCustomer = false;
		boolean newspaperPublished = false;
		boolean owner = false;
		boolean isLogged = false;

		try {

			isLogged = this.actorService.getLogged();
			result = new ModelAndView("article/display");
			article = this.articleService.findOne(articleId);
			Assert.notNull(article);
			writer = this.userService.findUserByArticle(articleId);
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			newspaper = this.newspaperService.findNewspaperByArticle(article.getId());

			if (this.actorService.getLogged()) {
				actor = this.actorService.findActorByPrincipal();
				if (!newspaper.getPublicNewspaper())
					if (writer.getId() != actor.getId())
						if (!(actor instanceof Admin)) {
							Assert.isTrue(actor instanceof Customer);
							for (final CreditCard creditCard : newspaper.getCreditCards()) {
								validCustomer = creditCard.getCustomer().equals(actor);
								if (validCustomer)
									break;
							}
							Assert.isTrue(validCustomer);
						}
				if (!actor.equals(writer)) {
					Assert.isTrue(article.getFinalMode());
					Assert.isTrue(newspaper.getPublicationDate() != null);
				}

			} else {
				Assert.isTrue(newspaper.getPublicNewspaper());
				Assert.isTrue(newspaper.getPublicationDate() != null);
				Assert.isTrue(article.getFinalMode());
			}

			followUps = this.articleService.findFollowUpsByArticle(pageable, articleId);

			if (newspaper.getPublicationDate() != null)
				newspaperPublished = true;
			if (actor != null)
				owner = actor.equals(writer);

			result.addObject("newspaperPublished", newspaperPublished);
			result.addObject("isLogged", isLogged);
			result.addObject("writer", writer);
			result.addObject("owner", owner);
			result.addObject("article", article);
			result.addObject("followUps", followUps);
			result.addObject("page", page);
			result.addObject("pageNum", followUps.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam(value = "search", defaultValue = "") final String search, @RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		final Page<Article> articles;
		Pageable pageable;
		Configuration configuration;

		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			articles = this.articleService.findPublicPublicatedArticlessWithSearch(pageable, search);
			result = new ModelAndView("article/list");

			result.addObject("articles", articles.getContent());
			result.addObject("search", search);
			result.addObject("page", page);
			result.addObject("pageNum", articles.getTotalPages());
			result.addObject("requestUri", "article/search.do?");
			result.addObject("search", search);
			result.addObject("generalView", true);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

}
