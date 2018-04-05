
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {//TODO: ALL

	// Services -------------------------------------------------------

	@Autowired
	ArticleService		articleService;

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	ActorService		actorService;

	@Autowired
	UserService			userService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer articleId) {
		ModelAndView result;
		Article article;
		Actor actor;
		User writer;

		try {
			actor = this.actorService.findActorByPrincipal();
			article = this.articleService.findOne(articleId);
			writer = this.userService.findUserByArticle(article.getId());

			Assert.isTrue(actor.equals(writer));

			result = this.createEditModelAndView(article);
		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Article article, final BindingResult binding) {
		ModelAndView result;
		Actor actor;
		User writer;
		Article savedArticle;
		Newspaper newspaper;

		if (binding.hasErrors())
			result = this.createEditModelAndView(article, "article.params.error");
		else
			try {
				actor = this.actorService.findActorByPrincipal();
				writer = this.userService.findUserByArticle(article.getId());

				Assert.isTrue(actor.equals(writer));

				savedArticle = this.articleService.save(article);
				newspaper = this.newspaperService.findNewspaperByArticle(savedArticle.getId());
				result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaper.getId());

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(article, "article.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Article article) {
		ModelAndView result;

		result = this.createEditModelAndView(article, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("newspaper/edit");
		result.addObject("article", article);

		result.addObject("message", messageCode);

		return result;

	}
}
