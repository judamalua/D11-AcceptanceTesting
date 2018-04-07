
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.FollowUpService;
import controllers.AbstractController;
import domain.Article;
import domain.FollowUp;

@Controller
@RequestMapping("/followUp/admin")
public class FollowUpAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	FollowUpService	followUpService;

	@Autowired
	ArticleService	articleService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int followUpId) {
		ModelAndView result;
		FollowUp followUp;
		Article article;

		try {
			followUp = this.followUpService.findOne(followUpId);
			article = this.articleService.getArticleByFollowUp(followUp);
			this.followUpService.delete(followUp);

			result = new ModelAndView("redirect:/article/display.do?articleId=" + article.getId());

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;

	}

}
