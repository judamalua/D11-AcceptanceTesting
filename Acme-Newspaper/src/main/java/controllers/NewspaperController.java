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
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import domain.Actor;
import domain.Article;
import domain.Configuration;
import domain.CreditCard;
import domain.Customer;
import domain.DomainEntity;
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
			result.addObject("page", pageArticle);
			result.addObject("pageNum", articles.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
