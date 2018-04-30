
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/article/admin")
public class ArticleAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ArticleService		articleService;

	@Autowired
	NewspaperService	newspaperService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;
		Article article;
		Newspaper newspaper;

		try {
			article = this.articleService.findOne(articleId);
			newspaper = this.newspaperService.findNewspaperByArticle(articleId);

			this.articleService.delete(article);

			result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaper.getId());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

	// List taboo ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Collection<Article> articles;

		try {
			articles = this.articleService.getAllTabooArticles();

			result = new ModelAndView("article/list");

			result.addObject("articles", articles);
			result.addObject("requestUri", "article/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

}
