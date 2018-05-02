
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ConfigurationService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Configuration;
import domain.Newspaper;

@Controller
@RequestMapping("/article/admin")
public class ArticleAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ConfigurationService	configurationService;


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
	public ModelAndView list(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Article> articles;
		Configuration configuration;
		Pageable pageable;

		try {

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			articles = this.articleService.getAllTabooArticles(pageable);

			result = new ModelAndView("article/list");

			result.addObject("articles", articles.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", articles.getTotalPages());
			result.addObject("requestUri", "article/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

}
