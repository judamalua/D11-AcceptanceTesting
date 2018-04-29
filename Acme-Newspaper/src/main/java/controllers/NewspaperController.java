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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

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

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) throws IllegalArgumentException, IllegalAccessException, IOException {
		ModelAndView result;
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;
		Collection<Boolean> ownNewspapers;
		Actor actor;
		User publisher;
		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			ownNewspapers = new ArrayList<>();
			result = new ModelAndView("newspaper/list");

			newspapers = this.newspaperService.findPublicPublicatedNewspapers(pageable);

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
			result.addObject("requestUri", "newspaper/list.do?");
		} catch (final Throwable throwable) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer newspaperId, @RequestParam(required = true, defaultValue = "0") final Integer pageArticle) {
		ModelAndView result;
		Newspaper newspaper;
		Actor actor;
		User writer;
		final Collection<Boolean> ownArticles;
		final Page<Article> articles;
		Pageable pageable;
		Configuration configuration;
		Boolean subscriber;
		Random random;

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

			random = new Random();
			if (newspaper.getAdvertisements().size() > 0)
				result.addObject("advertisement", newspaper.getAdvertisements().toArray()[random.nextInt(newspaper.getAdvertisements().size())]);
			else
				result.addObject("advertisement", null);
			result.addObject("subscriber", subscriber);
			result.addObject("newspaper", newspaper);
			result.addObject("articles", articles.getContent());
			result.addObject("page", pageArticle);
			result.addObject("pageNum", articles.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam(value = "search", defaultValue = "") final String search, @RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;
		Collection<Boolean> ownNewspapers;
		Actor actor;
		User publisher;
		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			ownNewspapers = new ArrayList<>();
			result = new ModelAndView("newspaper/list");

			newspapers = this.newspaperService.findPublicPublicatedNewspapersWithSearch(pageable, search);

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
			result.addObject("requestUri", "newspaper/search.do?search=" + search);
		} catch (final Throwable throwable) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
