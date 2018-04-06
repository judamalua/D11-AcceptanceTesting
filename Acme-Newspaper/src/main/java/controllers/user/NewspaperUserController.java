
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class NewspaperUserController extends AbstractController {

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
	public ModelAndView list(@RequestParam final Boolean published, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;
		try {
			result = new ModelAndView("newspaper/list");
			actor = this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			if (published)
				newspapers = this.userService.findPublishedNewspapersByUser(actor.getId(), pageable);
			else
				newspapers = this.userService.findNotPublishedNewspapersByUser(actor.getId(), pageable);

			result.addObject("owner", true);
			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/user/list.do?published=" + published + "&");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}
	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		final User publisher;
		Actor actor;
		try {
			actor = this.actorService.findActorByPrincipal();

			newspaper = this.newspaperService.findOne(newspaperId);
			publisher = this.userService.findUserByNewspaper(newspaper.getId());

			Assert.isTrue(actor.equals(publisher));
			Assert.isTrue(newspaper.getPublicationDate() == null);

			result = this.createEditModelAndView(newspaper);
		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
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

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publish(@RequestParam final Integer newspaperId) {
		final ModelAndView result;
		Newspaper newspaper;
		Actor actor;
		User publisher;

		actor = this.actorService.findActorByPrincipal();

		newspaper = this.newspaperService.findOne(newspaperId);
		publisher = this.userService.findUserByNewspaper(newspaperId);

		Assert.isTrue(actor.equals(publisher));
		Assert.isTrue(newspaper.getPublicationDate() == null);

		result = new ModelAndView("redirect:/newspaper/user/list.do?published=true");

		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("newspaper") Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		final User publisher;
		final Actor actor;
		Newspaper savedNewspaper;

		newspaper = this.newspaperService.reconstruct(newspaper, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper, "newspaper.params.error");
		else
			try {

				savedNewspaper = this.newspaperService.save(newspaper);

				publisher = this.userService.findUserByNewspaper(savedNewspaper.getId());
				actor = this.actorService.findActorByPrincipal();

				Assert.isTrue(actor.equals(publisher));
				result = new ModelAndView("redirect:/newspaper/list.do");

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
