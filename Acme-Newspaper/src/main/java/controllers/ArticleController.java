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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ConfigurationService;
import services.UserService;
import domain.Article;
import domain.Configuration;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private UserService				userService;

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
		final Configuration configuration;

		try {

			result = new ModelAndView("article/display");
			article = this.articleService.findOne(articleId);
			Assert.notNull(article);
			writer = this.userService.findUserByArticle(articleId);
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			followUps = this.articleService.findFollowUpsByArticle(pageable);

			result.addObject("writer", writer);
			result.addObject("article", article);
			result.addObject("followUps", followUps);
			result.addObject("page", page);
			result.addObject("pageNum", followUps.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
