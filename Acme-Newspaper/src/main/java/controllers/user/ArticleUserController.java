
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
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
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

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
		Newspaper newspaper;

		try {
			actor = this.actorService.findActorByPrincipal();
			article = this.articleService.findOne(articleId);
			writer = this.userService.findUserByArticle(article.getId());
			newspaper = this.newspaperService.findNewspaperByArticle(article.getId());

			Assert.isTrue(newspaper.getPublicationDate() == null);
			Assert.isTrue(actor.equals(writer));
			Assert.isTrue(!article.getFinalMode());

			result = this.createEditModelAndView(article);
			result.addObject("newspaperId", newspaper.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Article article;
		Newspaper newspaper;

		try {
			this.actorService.checkUserLogin();
			article = this.articleService.create();
			newspaper = this.newspaperService.findOne(newspaperId);

			Assert.isTrue(newspaper.getPublicationDate() == null);

			result = this.createEditModelAndView(article);
			result.addObject("newspaperId", newspaperId);

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("newspaperId") final Integer newspaperId, @ModelAttribute("article") Article article, final BindingResult binding) {
		ModelAndView result;
		User actor;
		User writer;
		Article savedArticle;
		Newspaper newspaper;

		article = this.articleService.reconstruct(article, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(article, "article.params.error");
			result.addObject("newspaperId", newspaperId);
		} else
			try {
				actor = (User) this.actorService.findActorByPrincipal();
				newspaper = this.newspaperService.findOne(newspaperId);
				Assert.isTrue(actor.getNewspapers().contains(newspaper));
				Assert.isTrue(newspaper.getPublicationDate() == null);

				savedArticle = this.articleService.save(article, newspaper);
				writer = this.userService.findUserByArticle(savedArticle.getId());

				Assert.isTrue(actor.equals(writer));

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

		result = new ModelAndView("article/edit");
		result.addObject("article", article);
		result.addObject("message", messageCode);

		return result;

	}
}
