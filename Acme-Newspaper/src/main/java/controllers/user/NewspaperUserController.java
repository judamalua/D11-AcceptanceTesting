
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Configuration;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {//TODO: ALL

	// Services -------------------------------------------------------

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	ActorService			actorService;

	@Autowired
	UserService				userService;

	@Autowired
	ConfigurationService	configurationService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "false") final Boolean published, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;

		result = new ModelAndView("newspaper/list");
		actor = this.actorService.findActorByPrincipal();
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());

		newspapers = this.userService.findNewspapersByUser(actor.getId(), published, pageable);

		result.addObject("newspapers", newspapers.getContent());
		result.addObject("page", page);
		result.addObject("pageNum", newspapers.getTotalPages());

		return result;
	}
	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		final User publisher;
		Actor actor;

		actor = this.actorService.findActorByPrincipal();

		newspaper = this.newspaperService.findOne(newspaperId);
		publisher = this.userService.findUserByNewspaper(newspaper.getId());

		Assert.isTrue(actor.equals(publisher));

		result = this.createEditModelAndView(newspaper);
		return result;
	}
	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Newspaper newspaper;

		this.actorService.checkUserLogin();

		newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		final User publisher;
		final Actor actor;

		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper, "newspaper.params.error");
		else
			try {

				publisher = this.userService.findUserByNewspaper(newspaper.getId());
				actor = this.actorService.findActorByPrincipal();

				Assert.isTrue(actor.equals(publisher));

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
