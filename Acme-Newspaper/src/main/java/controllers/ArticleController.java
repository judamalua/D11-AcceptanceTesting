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

import services.ArticleService;
import services.UserService;
import domain.Article;
import domain.User;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	@Autowired
	private ArticleService	articleService;

	@Autowired
	private UserService		userService;


	// Constructors -----------------------------------------------------------

	public ArticleController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer articleId) {
		ModelAndView result;
		Article article;
		final User writer;

		try {

			result = new ModelAndView("article/display");
			article = this.articleService.findOne(articleId);
			Assert.notNull(article);
			//writer = this.userService.findUserByArticle(articleId);
			//TODO manu haz esto

			//result.addObject("writer", writer);
			result.addObject("article", article);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
