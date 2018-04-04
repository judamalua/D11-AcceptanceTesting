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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import domain.Actor;
import domain.Article;
import domain.Configuration;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public NewspaperController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	/**
	 * That method returns a model and view with the system users list
	 * 
	 * @param page
	 * @param anonymous
	 * 
	 * @return ModelandView
	 * @author MJ
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		final ModelAndView result;
		final Page<Newspaper> newspapers;
		final Pageable pageable;
		final Configuration configuration;
		final Collection<Boolean> ownNewspapers;
		Actor actor;
		User publisher;

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		ownNewspapers = new ArrayList<>();
		result = new ModelAndView("newspaper/list");

		newspapers = this.newspaperService.findPublicNewspapers(pageable);
		if (this.actorService.getLogged()) {
			actor = this.actorService.findActorByPrincipal();
			for (final Newspaper newspaper : newspapers.getContent()) {
				publisher = this.userService.findUserByNewspaper(newspaper.getId());
				ownNewspapers.add(actor.equals(publisher));
			}
			result.addObject("ownNewspaper", ownNewspapers);
		}
		result.addObject("newspapers", newspapers.getContent());
		result.addObject("page", page);
		result.addObject("pageNum", newspapers.getTotalPages());

		return result;
	}
	/**
	 * That method returns a model and view with the display of an actor
	 * 
	 * @param actorId
	 * @param anonymous
	 * @param rsvPage
	 * @param createdRendezvousPage
	 * 
	 * @return ModelandView
	 * @author MJ
	 */
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer newspaperId, @RequestParam final Integer pageArticle) {
		ModelAndView result;
		Newspaper newspaper;
		Actor actor;
		User writer;
		final Collection<Boolean> ownArticles;
		final Page<Article> articles;
		Pageable pageable;
		Configuration configuration;
		Boolean subscriber;

		try {

			result = new ModelAndView("newspaper/display");
			subscriber = false;
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.notNull(newspaper);

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(pageArticle, configuration.getPageSize());

			ownArticles = new ArrayList<>();
			articles = this.newspaperService.findArticlesByNewspaper(newspaperId, pageable);

			if (this.actorService.getLogged()) {
				actor = this.actorService.findActorByPrincipal();
				for (final Article article : articles.getContent()) {
					writer = this.userService.findUserByArticle(article.getId());
					ownArticles.add(writer.equals(actor));
				}

				if (actor instanceof Customer)
					for (final CreditCard creditCard : newspaper.getCreditCards()) {
						subscriber = creditCard.getCustomer().equals(actor);
						if (subscriber)
							break;
					}
				result.addObject("ownArticle", ownArticles);
			}

			result.addObject("subscriber", subscriber);
			result.addObject("newspaper", newspaper);
			result.addObject("articles", articles.getContent());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
