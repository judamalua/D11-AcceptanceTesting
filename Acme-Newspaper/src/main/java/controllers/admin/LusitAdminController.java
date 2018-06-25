
package controllers.admin;

import java.util.Collection;
import java.util.Date;

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
import services.LusitService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Actor;
import domain.Admin;
import domain.Configuration;
import domain.Lusit;
import domain.Newspaper;

@Controller
@RequestMapping("/lusit/admin")
public class LusitAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private LusitService			lusitService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ConfigurationService	configurationService;


	// List taboo ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false, defaultValue = "0") final Integer page, @RequestParam final Boolean finalMode) {
		ModelAndView result;
		Page<Lusit> lusits;
		Configuration configuration;
		Pageable pageable;
		Admin admin;

		try {

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			admin = (Admin) this.actorService.findActorByPrincipal();
			lusits = this.lusitService.findLusitsByAdmin(admin.getId(), finalMode, pageable);

			result = new ModelAndView("lusit/list");

			result.addObject("lusits", lusits.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", lusits.getTotalPages());
			result.addObject("requestUri", "lusit/admin/list.do?finalMode=" + finalMode + "&page=");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Lusit lusit;

		try {
			lusit = this.lusitService.create();

			result = this.createEditModelAndView(lusit);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer lusitId) {
		ModelAndView result;
		Lusit lusit;
		final Collection<Lusit> lusits;
		Actor actor;
		try {
			actor = this.actorService.findActorByPrincipal();

			lusit = this.lusitService.findOne(lusitId);
			lusits = this.lusitService.findLusitsByAdmin(actor.getId());

			Assert.isTrue(lusits.contains(lusit));
			Assert.isTrue(!lusit.getFinalMode());

			result = this.createEditModelAndView(lusit);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("lusit") Lusit lusit, final BindingResult binding, @RequestParam("newspaper") final Integer newspaperId) {
		ModelAndView result;
		Collection<Lusit> lusits;
		Actor actor;
		Newspaper newspaper;
		try {
			lusit = this.lusitService.reconstruct(lusit, binding);
		} catch (final Throwable oops) {
		}

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(lusit, "lusit.params.error");
		} else {
			try {
				actor = this.actorService.findActorByPrincipal();
				if (lusit.getPublicationDate() != null) {
					Assert.isTrue(lusit.getPublicationDate().after(new Date()), "Future error");
				}
				if (lusit.getFinalMode()) {
					Assert.isTrue(newspaperId != 0, "newspaper error");
				}
				if (lusit.getId() != 0) {
					lusits = this.lusitService.findLusitsByAdmin(actor.getId());
					Assert.isTrue(lusits.contains(lusit));
				}
				newspaper = this.newspaperService.findOne(newspaperId);
				this.lusitService.save(lusit, newspaper);
				result = new ModelAndView("redirect:/lusit/admin/list.do?finalMode=" + lusit.getFinalMode());

			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Future error")) {
					result = this.createEditModelAndView(lusit, "lusit.future.error");
				} else if (oops.getMessage().contains("newspaper error")) {
					result = this.createEditModelAndView(lusit, "lusit.newspaper.error");
				} else {
					result = this.createEditModelAndView(lusit, "lusit.commit.error");
				}
			}
		}

		return result;
	}

	// Deleting -------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("lusit") Lusit lusit, final BindingResult binding) {
		ModelAndView result;

		try {
			lusit = this.lusitService.findOne(lusit.getId());
			Assert.isTrue(!lusit.getFinalMode());
			Assert.isTrue(lusit.getPublicationDate() != null);

			this.lusitService.delete(lusit);

			result = new ModelAndView("redirect:/lusit/admin/list.do?finalMode=" + lusit.getFinalMode());

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(lusit, "lusit.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Lusit lusit) {
		ModelAndView result;

		result = this.createEditModelAndView(lusit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Lusit lusit, final String messageCode) {
		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findPublicPublicatedNewspapers();
		result = new ModelAndView("lusit/edit");

		result.addObject("lusit", lusit);
		result.addObject("newspapers", newspapers);
		result.addObject("message", messageCode);

		return result;

	}
}
