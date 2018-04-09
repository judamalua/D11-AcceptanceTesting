
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/admin")
public class NewspaperAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	NewspaperService	newspaperService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		try {
			newspaper = this.newspaperService.findOne(newspaperId);

			this.newspaperService.delete(newspaper);

			result = new ModelAndView("redirect:/newspaper/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}

	// List taboo ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Collection<Newspaper> newspapers;

		try {
			newspapers = this.newspaperService.getAllTabooNewspapers();

			result = new ModelAndView("newspaper/list");

			result.addObject("newspapers", newspapers);
			result.addObject("requestUri", "newspaper/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}

}
