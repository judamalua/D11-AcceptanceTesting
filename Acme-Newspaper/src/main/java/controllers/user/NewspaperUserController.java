
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {//TODO: ALL

	// Services -------------------------------------------------------

	@Autowired
	NewspaperService	newspaperService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("newspaper/list");

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);

		result = this.createEditModelAndView(newspaper);
		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper, "newspaper.params.error");
		else
			try {
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:/newspaper/user/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		ModelAndView result;

		result = this.createEditModelAndView(newspaper, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("newspaper/edit");
		result.addObject("newspaper", newspaper);

		result.addObject("message", messageCode);

		return result;

	}
}
